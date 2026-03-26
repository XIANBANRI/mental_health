package com.sl.mentalhealth.service;

import com.sl.mentalhealth.dto.AssessmentSubmitAnswer;
import com.sl.mentalhealth.entity.AssessmentScale;
import com.sl.mentalhealth.entity.AssessmentScaleVersion;
import com.sl.mentalhealth.entity.AssessmentVersionOption;
import com.sl.mentalhealth.entity.AssessmentVersionQuestion;
import com.sl.mentalhealth.entity.AssessmentVersionRule;
import com.sl.mentalhealth.entity.StudentAssessmentAnswer;
import com.sl.mentalhealth.entity.StudentAssessmentRecord;
import com.sl.mentalhealth.entity.StudentAssessmentSemesterSummary;
import com.sl.mentalhealth.kafka.message.AssessmentRequestMessage;
import com.sl.mentalhealth.kafka.message.AssessmentResponseMessage;
import com.sl.mentalhealth.repository.AssessmentScaleRepository;
import com.sl.mentalhealth.repository.AssessmentScaleVersionRepository;
import com.sl.mentalhealth.repository.AssessmentVersionOptionRepository;
import com.sl.mentalhealth.repository.AssessmentVersionQuestionRepository;
import com.sl.mentalhealth.repository.AssessmentVersionRuleRepository;
import com.sl.mentalhealth.repository.StudentAssessmentAnswerRepository;
import com.sl.mentalhealth.repository.StudentAssessmentRecordRepository;
import com.sl.mentalhealth.repository.StudentAssessmentSemesterSummaryRepository;
import com.sl.mentalhealth.vo.AssessmentOptionVO;
import com.sl.mentalhealth.vo.AssessmentQuestionVO;
import com.sl.mentalhealth.vo.AssessmentRecordDetailVO;
import com.sl.mentalhealth.vo.AssessmentRecordVO;
import com.sl.mentalhealth.vo.AssessmentScaleDetailVO;
import com.sl.mentalhealth.vo.AssessmentScaleVO;
import com.sl.mentalhealth.vo.AssessmentSubmitResultVO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocalAssessmentService {

  public static final String ACTION_LIST_SCALES = "LIST_SCALES";
  public static final String ACTION_GET_DETAIL = "GET_DETAIL";
  public static final String ACTION_SUBMIT = "SUBMIT";
  public static final String ACTION_GET_RECORDS = "GET_RECORDS";

  private static final String DEFAULT_SEMESTER = "第1学期";
  private static final String STATUS_COMPLETED = "COMPLETED";
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private final AssessmentScaleRepository scaleRepository;
  private final AssessmentScaleVersionRepository scaleVersionRepository;
  private final AssessmentVersionQuestionRepository versionQuestionRepository;
  private final AssessmentVersionOptionRepository versionOptionRepository;
  private final AssessmentVersionRuleRepository versionRuleRepository;
  private final StudentAssessmentRecordRepository studentAssessmentRecordRepository;
  private final StudentAssessmentAnswerRepository studentAssessmentAnswerRepository;
  private final StudentAssessmentSemesterSummaryRepository studentAssessmentSemesterSummaryRepository;

  public LocalAssessmentService(AssessmentScaleRepository scaleRepository,
      AssessmentScaleVersionRepository scaleVersionRepository,
      AssessmentVersionQuestionRepository versionQuestionRepository,
      AssessmentVersionOptionRepository versionOptionRepository,
      AssessmentVersionRuleRepository versionRuleRepository,
      StudentAssessmentRecordRepository studentAssessmentRecordRepository,
      StudentAssessmentAnswerRepository studentAssessmentAnswerRepository,
      StudentAssessmentSemesterSummaryRepository studentAssessmentSemesterSummaryRepository) {
    this.scaleRepository = scaleRepository;
    this.scaleVersionRepository = scaleVersionRepository;
    this.versionQuestionRepository = versionQuestionRepository;
    this.versionOptionRepository = versionOptionRepository;
    this.versionRuleRepository = versionRuleRepository;
    this.studentAssessmentRecordRepository = studentAssessmentRecordRepository;
    this.studentAssessmentAnswerRepository = studentAssessmentAnswerRepository;
    this.studentAssessmentSemesterSummaryRepository = studentAssessmentSemesterSummaryRepository;
  }

  @Transactional
  public AssessmentResponseMessage handle(AssessmentRequestMessage request) {
    return switch (request.getAction()) {
      case ACTION_LIST_SCALES -> listScales(request.getRequestId());
      case ACTION_GET_DETAIL -> getDetail(request.getRequestId(), request.getScaleId());
      case ACTION_SUBMIT -> submit(request);
      case ACTION_GET_RECORDS -> getRecords(request.getRequestId(), request.getStudentId());
      default -> fail(request.getRequestId(), "不支持的操作");
    };
  }

  private AssessmentResponseMessage listScales(String requestId) {
    List<AssessmentScaleVO> list = scaleRepository.findByStatusOrderByIdAsc(1).stream()
        .filter(scale -> !Objects.equals(scale.getDeletedFlag(), 1))
        .filter(scale -> scale.getCurrentVersionId() != null)
        .map(this::buildScaleVO)
        .collect(Collectors.toList());

    AssessmentResponseMessage response = success(requestId, "查询成功");
    response.setScales(list);
    return response;
  }

  private AssessmentScaleVO buildScaleVO(AssessmentScale scale) {
    Integer versionNo = null;
    if (scale.getCurrentVersionId() != null) {
      versionNo = scaleVersionRepository.findById(scale.getCurrentVersionId())
          .map(AssessmentScaleVersion::getVersionNo)
          .orElse(null);
    }
    return new AssessmentScaleVO(
        scale.getId(),
        scale.getScaleCode(),
        scale.getScaleName(),
        scale.getScaleType(),
        scale.getDescription(),
        scale.getQuestionCount(),
        scale.getCurrentVersionId(),
        versionNo
    );
  }

  private AssessmentResponseMessage getDetail(String requestId, Long scaleId) {
    if (scaleId == null) {
      return fail(requestId, "量表ID不能为空");
    }

    Optional<AssessmentScale> optionalScale = scaleRepository.findById(scaleId);
    if (optionalScale.isEmpty() || Objects.equals(optionalScale.get().getDeletedFlag(), 1)
        || !Objects.equals(optionalScale.get().getStatus(), 1)) {
      return fail(requestId, "量表不存在或已停用");
    }

    AssessmentScale scale = optionalScale.get();
    Long versionId = scale.getCurrentVersionId();
    if (versionId == null) {
      return fail(requestId, "当前量表没有可用版本");
    }

    Optional<AssessmentScaleVersion> optionalVersion = scaleVersionRepository.findById(versionId);
    if (optionalVersion.isEmpty()) {
      return fail(requestId, "量表当前版本不存在");
    }

    AssessmentScaleVersion version = optionalVersion.get();
    List<AssessmentVersionQuestion> questions =
        versionQuestionRepository.findByVersionIdOrderByQuestionNoAsc(versionId);
    if (questions.isEmpty()) {
      return fail(requestId, "量表题目不存在");
    }

    List<Long> questionIds = questions.stream().map(AssessmentVersionQuestion::getId).toList();
    List<AssessmentVersionOption> options = questionIds.isEmpty()
        ? Collections.emptyList()
        : versionOptionRepository.findByVersionQuestionIdInOrderByVersionQuestionIdAscOptionNoAsc(questionIds);

    Map<Long, List<AssessmentOptionVO>> optionMap = options.stream()
        .collect(Collectors.groupingBy(
            AssessmentVersionOption::getVersionQuestionId,
            LinkedHashMap::new,
            Collectors.mapping(
                option -> new AssessmentOptionVO(
                    option.getId(),
                    option.getOptionNo(),
                    option.getOptionText(),
                    option.getOptionScore()
                ),
                Collectors.toList()
            )
        ));

    List<AssessmentQuestionVO> questionVOList = questions.stream()
        .map(question -> new AssessmentQuestionVO(
            question.getId(),
            question.getQuestionNo(),
            question.getQuestionText(),
            question.getRequiredFlag(),
            optionMap.getOrDefault(question.getId(), new ArrayList<>())
        ))
        .collect(Collectors.toList());

    AssessmentScaleDetailVO detail = new AssessmentScaleDetailVO(
        scale.getId(),
        scale.getScaleCode(),
        scale.getScaleName(),
        scale.getDescription(),
        version.getId(),
        version.getVersionNo(),
        questionVOList
    );

    AssessmentResponseMessage response = success(requestId, "查询成功");
    response.setDetail(detail);
    return response;
  }

  private AssessmentResponseMessage submit(AssessmentRequestMessage request) {
    String requestId = request.getRequestId();
    String studentId = trimToNull(request.getStudentId());
    String semester = trimToNull(request.getSemester());
    Long scaleId = request.getScaleId();
    Long versionId = request.getVersionId();
    List<AssessmentSubmitAnswer> answers = request.getAnswers();

    if (studentId == null) {
      return fail(requestId, "学号不能为空");
    }
    if (scaleId == null) {
      return fail(requestId, "量表ID不能为空");
    }
    if (versionId == null) {
      return fail(requestId, "版本ID不能为空");
    }
    if (answers == null || answers.isEmpty()) {
      return fail(requestId, "答案不能为空");
    }
    if (semester == null) {
      semester = DEFAULT_SEMESTER;
    }

    Optional<AssessmentScale> optionalScale = scaleRepository.findById(scaleId);
    if (optionalScale.isEmpty() || Objects.equals(optionalScale.get().getDeletedFlag(), 1)
        || !Objects.equals(optionalScale.get().getStatus(), 1)) {
      return fail(requestId, "量表不存在或已停用");
    }
    AssessmentScale scale = optionalScale.get();

    if (!Objects.equals(scale.getCurrentVersionId(), versionId)) {
      return fail(requestId, "提交的量表版本不是当前版本，请刷新后重试");
    }

    Optional<AssessmentScaleVersion> optionalVersion = scaleVersionRepository.findById(versionId);
    if (optionalVersion.isEmpty()) {
      return fail(requestId, "量表版本不存在");
    }
    AssessmentScaleVersion version = optionalVersion.get();
    if (!Objects.equals(version.getScaleId(), scaleId)) {
      return fail(requestId, "量表与版本不匹配");
    }
    if (!"ACTIVE".equalsIgnoreCase(version.getVersionStatus())) {
      return fail(requestId, "量表版本未启用");
    }

    List<AssessmentVersionQuestion> questions =
        versionQuestionRepository.findByVersionIdOrderByQuestionNoAsc(versionId);
    if (questions.isEmpty()) {
      return fail(requestId, "量表题目不存在");
    }

    Map<Long, AssessmentSubmitAnswer> submitAnswerMap = new HashMap<>();
    for (AssessmentSubmitAnswer answer : answers) {
      if (answer.getVersionQuestionId() == null || answer.getVersionOptionId() == null) {
        return fail(requestId, "答案数据不完整");
      }
      if (submitAnswerMap.containsKey(answer.getVersionQuestionId())) {
        return fail(requestId, "存在重复题目提交");
      }
      submitAnswerMap.put(answer.getVersionQuestionId(), answer);
    }

    long requiredCount = questions.stream()
        .filter(question -> !Objects.equals(question.getRequiredFlag(), 0))
        .count();
    if (submitAnswerMap.size() < requiredCount) {
      return fail(requestId, "题目未全部完成");
    }

    List<Long> questionIds = questions.stream().map(AssessmentVersionQuestion::getId).toList();
    List<AssessmentVersionOption> optionList = versionOptionRepository
        .findByVersionQuestionIdInOrderByVersionQuestionIdAscOptionNoAsc(questionIds);
    Map<Long, AssessmentVersionOption> optionMap = optionList.stream()
        .collect(Collectors.toMap(AssessmentVersionOption::getId, option -> option));

    int totalScore = 0;
    List<StudentAssessmentAnswer> answerEntities = new ArrayList<>();
    LocalDateTime now = LocalDateTime.now();

    for (AssessmentVersionQuestion question : questions) {
      AssessmentSubmitAnswer submitAnswer = submitAnswerMap.get(question.getId());
      if (submitAnswer == null) {
        if (Objects.equals(question.getRequiredFlag(), 0)) {
          continue;
        }
        return fail(requestId, "题目未全部完成");
      }

      AssessmentVersionOption option = optionMap.get(submitAnswer.getVersionOptionId());
      if (option == null) {
        return fail(requestId, "选项不存在");
      }
      if (!Objects.equals(option.getVersionQuestionId(), question.getId())) {
        return fail(requestId, "选项与题目不匹配");
      }

      totalScore += option.getOptionScore();

      StudentAssessmentAnswer answerEntity = new StudentAssessmentAnswer();
      answerEntity.setVersionQuestionId(question.getId());
      answerEntity.setQuestionNo(question.getQuestionNo());
      answerEntity.setQuestionText(question.getQuestionText());
      answerEntity.setVersionOptionId(option.getId());
      answerEntity.setOptionNo(option.getOptionNo());
      answerEntity.setOptionText(option.getOptionText());
      answerEntity.setAnswerScore(option.getOptionScore());
      answerEntity.setCreatedAt(now);
      answerEntities.add(answerEntity);
    }

    Optional<AssessmentVersionRule> optionalRule = versionRuleRepository
        .findFirstByVersionIdAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(
            versionId, totalScore, totalScore);

    String resultLevel = optionalRule.map(AssessmentVersionRule::getResultLevel).orElse("未分级");
    String resultSummary = optionalRule.map(AssessmentVersionRule::getResultSummary).orElse("暂无结果说明");
    String suggestion = optionalRule.map(AssessmentVersionRule::getSuggestion).orElse("");

    StudentAssessmentRecord record = studentAssessmentRecordRepository
        .findFirstByStudentIdAndSemesterAndScaleId(studentId, semester, scale.getId())
        .orElseGet(StudentAssessmentRecord::new);

    boolean isNewRecord = (record.getId() == null);

    if (isNewRecord) {
      record.setStudentId(studentId);
      record.setSemester(semester);
      record.setScaleId(scale.getId());
      record.setCreatedAt(now);
    }

    record.setScaleVersionId(version.getId());
    record.setScaleCode(scale.getScaleCode());
    record.setScaleName(scale.getScaleName());
    record.setRawScore(totalScore);
    record.setResultLevel(resultLevel);
    record.setResultSummary(resultSummary);
    record.setSuggestion(suggestion);
    record.setStatus(STATUS_COMPLETED);
    record.setSubmittedAt(now);
    record.setUpdatedAt(now);

    record = studentAssessmentRecordRepository.save(record);

    if (!isNewRecord) {
      studentAssessmentAnswerRepository.deleteByRecordId(record.getId());
    }

    for (StudentAssessmentAnswer answerEntity : answerEntities) {
      answerEntity.setRecordId(record.getId());
    }
    studentAssessmentAnswerRepository.saveAll(answerEntities);

    refreshSemesterSummary(studentId, semester, now);

    AssessmentSubmitResultVO submitResultVO = buildSubmitResultVO(record, version.getVersionNo());
    AssessmentResponseMessage response = success(requestId, "提交成功");
    response.setSubmitResult(submitResultVO);
    return response;
  }

  private void refreshSemesterSummary(String studentId, String semester, LocalDateTime now) {
    List<StudentAssessmentRecord> records = studentAssessmentRecordRepository
        .findByStudentIdAndSemesterOrderBySubmittedAtAscIdAsc(studentId, semester);

    StudentAssessmentSemesterSummary summary = studentAssessmentSemesterSummaryRepository
        .findByStudentIdAndSemester(studentId, semester)
        .orElseGet(StudentAssessmentSemesterSummary::new);

    summary.setStudentId(studentId);
    summary.setSemester(semester);
    summary.setTestedCount(records.size());
    summary.setScoreSummary(buildScoreSummary(records));

    int publishedScaleCount = getPublishedScaleCount();
    summary.setSemesterLevel(calculateSemesterLevel(records, publishedScaleCount));

    summary.setLastTestedAt(records.stream()
        .map(StudentAssessmentRecord::getSubmittedAt)
        .filter(Objects::nonNull)
        .max(LocalDateTime::compareTo)
        .orElse(now));

    if (summary.getCreatedAt() == null) {
      summary.setCreatedAt(now);
    }
    summary.setUpdatedAt(now);

    studentAssessmentSemesterSummaryRepository.save(summary);
  }

  private int getPublishedScaleCount() {
    return (int) scaleRepository.findByStatusOrderByIdAsc(1).stream()
        .filter(scale -> !Objects.equals(scale.getDeletedFlag(), 1))
        .filter(scale -> scale.getCurrentVersionId() != null)
        .count();
  }

  private String buildScoreSummary(List<StudentAssessmentRecord> records) {
    return records.stream()
        .sorted(Comparator.comparing(
            StudentAssessmentRecord::getSubmittedAt,
            Comparator.nullsLast(LocalDateTime::compareTo)
        ))
        .map(record -> record.getScaleCode() + ":" + record.getRawScore())
        .collect(Collectors.joining(", "));
  }

  private String calculateSemesterLevel(List<StudentAssessmentRecord> records, int publishedScaleCount) {
    if (records == null || records.isEmpty()) {
      return "未完成";
    }

    if (records.size() < publishedScaleCount) {
      return "未完成";
    }

    boolean hasDanger = records.stream()
        .map(StudentAssessmentRecord::getResultLevel)
        .filter(Objects::nonNull)
        .anyMatch(this::isDangerLevel);

    if (hasDanger) {
      return "危险";
    }

    boolean hasLightRisk = records.stream()
        .map(StudentAssessmentRecord::getResultLevel)
        .filter(Objects::nonNull)
        .anyMatch(this::isLightRiskLevel);

    if (hasLightRisk) {
      return "轻危";
    }

    return "无";
  }

  private boolean isDangerLevel(String level) {
    return level.contains("中度") || level.contains("重度") || level.contains("严重");
  }

  private boolean isLightRiskLevel(String level) {
    return level.contains("轻度") || level.contains("轻微");
  }

  private AssessmentResponseMessage getRecords(String requestId, String studentId) {
    String finalStudentId = trimToNull(studentId);
    if (finalStudentId == null) {
      return fail(requestId, "学号不能为空");
    }

    List<StudentAssessmentSemesterSummary> summaries = studentAssessmentSemesterSummaryRepository
        .findByStudentIdOrderByLastTestedAtDescIdDesc(finalStudentId);

    List<AssessmentRecordVO> records = summaries.stream()
        .map(summary -> buildSemesterRecordVO(
            summary,
            studentAssessmentRecordRepository.findByStudentIdAndSemesterOrderBySubmittedAtDescIdDesc(
                finalStudentId, summary.getSemester())
        ))
        .collect(Collectors.toList());

    AssessmentResponseMessage response = success(requestId, "查询成功");
    response.setRecords(records);
    return response;
  }

  private AssessmentRecordVO buildSemesterRecordVO(StudentAssessmentSemesterSummary summary,
      List<StudentAssessmentRecord> detailRecords) {
    AssessmentRecordVO vo = new AssessmentRecordVO();
    vo.setSummaryId(summary.getId());
    vo.setSemester(summary.getSemester());
    vo.setTestedCount(summary.getTestedCount());
    vo.setScoreSummary(summary.getScoreSummary());
    vo.setSemesterLevel(summary.getSemesterLevel());
    vo.setLastTestedAt(summary.getLastTestedAt() == null
        ? null
        : DATE_TIME_FORMATTER.format(summary.getLastTestedAt()));
    vo.setDetails(detailRecords.stream().map(this::buildRecordDetailVO).collect(Collectors.toList()));
    return vo;
  }

  private AssessmentRecordDetailVO buildRecordDetailVO(StudentAssessmentRecord record) {
    AssessmentRecordDetailVO vo = new AssessmentRecordDetailVO();
    vo.setRecordId(record.getId());
    vo.setScaleCode(record.getScaleCode());
    vo.setScaleName(record.getScaleName());
    vo.setRawScore(record.getRawScore());
    vo.setResultLevel(record.getResultLevel());
    vo.setResultSummary(record.getResultSummary());
    vo.setSuggestion(record.getSuggestion());
    vo.setSubmittedAt(record.getSubmittedAt() == null
        ? null
        : DATE_TIME_FORMATTER.format(record.getSubmittedAt()));
    return vo;
  }

  private AssessmentSubmitResultVO buildSubmitResultVO(StudentAssessmentRecord record, Integer versionNo) {
    AssessmentSubmitResultVO vo = new AssessmentSubmitResultVO();
    vo.setRecordId(record.getId());
    vo.setSemester(record.getSemester());
    vo.setScaleId(record.getScaleId());
    vo.setScaleCode(record.getScaleCode());
    vo.setScaleName(record.getScaleName());
    vo.setVersionId(record.getScaleVersionId());
    vo.setVersionNo(versionNo);
    vo.setRawScore(record.getRawScore());
    vo.setResultLevel(record.getResultLevel());
    vo.setResultSummary(record.getResultSummary());
    vo.setSuggestion(record.getSuggestion());
    vo.setSubmittedAt(record.getSubmittedAt() == null
        ? null
        : DATE_TIME_FORMATTER.format(record.getSubmittedAt()));
    return vo;
  }

  private String trimToNull(String value) {
    if (value == null) {
      return null;
    }
    String trimmed = value.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }

  private AssessmentResponseMessage success(String requestId, String message) {
    AssessmentResponseMessage response = new AssessmentResponseMessage();
    response.setRequestId(requestId);
    response.setSuccess(true);
    response.setMessage(message);
    return response;
  }

  private AssessmentResponseMessage fail(String requestId, String message) {
    AssessmentResponseMessage response = new AssessmentResponseMessage();
    response.setRequestId(requestId);
    response.setSuccess(false);
    response.setMessage(message);
    return response;
  }
}