package com.sl.mentalhealth.service;

import com.sl.mentalhealth.dto.AssessmentSubmitAnswerDTO;
import com.sl.mentalhealth.entity.AssessmentAnswer;
import com.sl.mentalhealth.entity.AssessmentOption;
import com.sl.mentalhealth.entity.AssessmentQuestion;
import com.sl.mentalhealth.entity.AssessmentRecord;
import com.sl.mentalhealth.entity.AssessmentResultRule;
import com.sl.mentalhealth.entity.AssessmentScale;
import com.sl.mentalhealth.entity.AssessmentTotalRule;
import com.sl.mentalhealth.kafka.message.AssessmentRequestMessage;
import com.sl.mentalhealth.kafka.message.AssessmentResponseMessage;
import com.sl.mentalhealth.repository.AssessmentAnswerRepository;
import com.sl.mentalhealth.repository.AssessmentOptionRepository;
import com.sl.mentalhealth.repository.AssessmentQuestionRepository;
import com.sl.mentalhealth.repository.AssessmentRecordRepository;
import com.sl.mentalhealth.repository.AssessmentResultRuleRepository;
import com.sl.mentalhealth.repository.AssessmentScaleRepository;
import com.sl.mentalhealth.repository.AssessmentTotalRuleRepository;
import com.sl.mentalhealth.vo.AssessmentOptionVO;
import com.sl.mentalhealth.vo.AssessmentQuestionVO;
import com.sl.mentalhealth.vo.AssessmentRecordVO;
import com.sl.mentalhealth.vo.AssessmentScaleDetailVO;
import com.sl.mentalhealth.vo.AssessmentScaleVO;
import com.sl.mentalhealth.vo.AssessmentSubmitResultVO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
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
  private static final String STATUS_DONE = "已完成";
  private static final String STATUS_UNDONE = "未完成";

  private final AssessmentScaleRepository scaleRepository;
  private final AssessmentQuestionRepository questionRepository;
  private final AssessmentOptionRepository optionRepository;
  private final AssessmentRecordRepository recordRepository;
  private final AssessmentAnswerRepository answerRepository;
  private final AssessmentResultRuleRepository resultRuleRepository;
  private final AssessmentTotalRuleRepository totalRuleRepository;

  public LocalAssessmentService(AssessmentScaleRepository scaleRepository,
      AssessmentQuestionRepository questionRepository,
      AssessmentOptionRepository optionRepository,
      AssessmentRecordRepository recordRepository,
      AssessmentAnswerRepository answerRepository,
      AssessmentResultRuleRepository resultRuleRepository,
      AssessmentTotalRuleRepository totalRuleRepository) {
    this.scaleRepository = scaleRepository;
    this.questionRepository = questionRepository;
    this.optionRepository = optionRepository;
    this.recordRepository = recordRepository;
    this.answerRepository = answerRepository;
    this.resultRuleRepository = resultRuleRepository;
    this.totalRuleRepository = totalRuleRepository;
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
        .map(s -> new AssessmentScaleVO(
            s.getId(),
            s.getScaleCode(),
            s.getScaleName(),
            s.getScaleType(),
            s.getDescription(),
            s.getQuestionCount()
        ))
        .collect(Collectors.toList());

    AssessmentResponseMessage response = success(requestId, "查询成功");
    response.setScales(list);
    return response;
  }

  private AssessmentResponseMessage getDetail(String requestId, Long scaleId) {
    if (scaleId == null) {
      return fail(requestId, "量表ID不能为空");
    }

    Optional<AssessmentScale> optionalScale = scaleRepository.findById(scaleId);
    if (optionalScale.isEmpty()) {
      return fail(requestId, "量表不存在");
    }

    AssessmentScale scale = optionalScale.get();
    List<AssessmentQuestion> questions = questionRepository.findByScaleIdOrderByQuestionNoAsc(scaleId);

    List<Long> questionIds = questions.stream().map(AssessmentQuestion::getId).toList();
    List<AssessmentOption> options = questionIds.isEmpty()
        ? Collections.emptyList()
        : optionRepository.findByQuestionIdInOrderByQuestionIdAscOptionNoAsc(questionIds);

    Map<Long, List<AssessmentOptionVO>> optionMap = options.stream()
        .collect(Collectors.groupingBy(
            AssessmentOption::getQuestionId,
            LinkedHashMap::new,
            Collectors.mapping(
                o -> new AssessmentOptionVO(
                    o.getId(),
                    o.getOptionNo(),
                    o.getOptionText(),
                    o.getOptionScore()
                ),
                Collectors.toList()
            )
        ));

    List<AssessmentQuestionVO> questionVOList = questions.stream()
        .map(q -> new AssessmentQuestionVO(
            q.getId(),
            q.getQuestionNo(),
            q.getQuestionText(),
            optionMap.getOrDefault(q.getId(), new ArrayList<>())
        ))
        .collect(Collectors.toList());

    AssessmentScaleDetailVO detail = new AssessmentScaleDetailVO(
        scale.getId(),
        scale.getScaleCode(),
        scale.getScaleName(),
        scale.getDescription(),
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
    List<AssessmentSubmitAnswerDTO> answers = request.getAnswers();

    if (studentId == null) {
      return fail(requestId, "学号不能为空");
    }
    if (scaleId == null) {
      return fail(requestId, "量表ID不能为空");
    }
    if (answers == null || answers.isEmpty()) {
      return fail(requestId, "答案不能为空");
    }
    if (semester == null) {
      semester = DEFAULT_SEMESTER;
    }

    Optional<AssessmentScale> optionalScale = scaleRepository.findById(scaleId);
    if (optionalScale.isEmpty()) {
      return fail(requestId, "量表不存在");
    }

    AssessmentScale scale = optionalScale.get();
    String scaleCode = normalizeScaleCode(scale.getScaleCode());
    if (!isSupportedScale(scaleCode)) {
      return fail(requestId, "当前仅支持K10、WHO5、PHQ9、GAD7四个量表");
    }

    List<AssessmentQuestion> questions = questionRepository.findByScaleIdOrderByQuestionNoAsc(scaleId);
    if (questions.isEmpty()) {
      return fail(requestId, "量表题目不存在");
    }

    Map<Long, AssessmentQuestion> questionMap = questions.stream()
        .collect(Collectors.toMap(AssessmentQuestion::getId, q -> q));

    Map<Long, AssessmentSubmitAnswerDTO> submitAnswerMap = new HashMap<>();
    for (AssessmentSubmitAnswerDTO item : answers) {
      if (item.getQuestionId() == null || item.getOptionId() == null) {
        return fail(requestId, "答案数据不完整");
      }
      if (submitAnswerMap.containsKey(item.getQuestionId())) {
        return fail(requestId, "存在重复题目提交");
      }
      submitAnswerMap.put(item.getQuestionId(), item);
    }

    if (submitAnswerMap.size() != questions.size()) {
      return fail(requestId, "题目未全部完成");
    }

    List<Long> questionIds = questions.stream().map(AssessmentQuestion::getId).toList();
    List<AssessmentOption> optionList = optionRepository.findByQuestionIdInOrderByQuestionIdAscOptionNoAsc(questionIds);
    Map<Long, AssessmentOption> optionMap = optionList.stream()
        .collect(Collectors.toMap(AssessmentOption::getId, o -> o));

    int totalScore = 0;
    List<AssessmentAnswer> answerEntities = new ArrayList<>();

    for (AssessmentQuestion question : questions) {
      AssessmentSubmitAnswerDTO item = submitAnswerMap.get(question.getId());
      if (item == null) {
        return fail(requestId, "题目未全部完成");
      }

      AssessmentOption option = optionMap.get(item.getOptionId());
      if (option == null) {
        return fail(requestId, "选项不存在");
      }

      if (!Objects.equals(option.getQuestionId(), question.getId())) {
        return fail(requestId, "选项与题目不匹配");
      }

      totalScore += option.getOptionScore();

      AssessmentAnswer answer = new AssessmentAnswer();
      answer.setQuestionId(question.getId());
      answer.setOptionId(option.getId());
      answer.setAnswerScore(option.getOptionScore());
      answer.setCreatedAt(LocalDateTime.now());
      answerEntities.add(answer);
    }

    Optional<AssessmentResultRule> optionalRule =
        resultRuleRepository.findFirstByScaleIdAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(
            scaleId, totalScore, totalScore
        );

    String resultLevel = optionalRule.map(AssessmentResultRule::getResultLevel).orElse("未分级");
    String resultSummary = optionalRule.map(AssessmentResultRule::getResultSummary).orElse("暂无结果说明");
    String suggestion = optionalRule.map(AssessmentResultRule::getSuggestion).orElse("");

    Optional<AssessmentRecord> optionalRecord = recordRepository.findByStudentIdAndSemester(studentId, semester);
    AssessmentRecord record;
    if (optionalRecord.isPresent()) {
      record = optionalRecord.get();
    } else {
      record = buildEmptyRecord(studentId, semester);
    }

    LocalDateTime now = LocalDateTime.now();

    if (record.getId() == null) {
      record.setSubmittedAt(now);
      record.setUpdatedAt(now);
      record = recordRepository.save(record);
    }

    answerRepository.deleteByRecordIdAndQuestionIdIn(record.getId(), questionIds);

    applyScaleResult(record, scaleCode, totalScore, resultLevel, resultSummary);
    recalculateHealthResult(record);

    record.setSubmittedAt(now);
    record.setUpdatedAt(now);
    record = recordRepository.save(record);

    for (AssessmentAnswer answer : answerEntities) {
      answer.setRecordId(record.getId());
    }
    answerRepository.saveAll(answerEntities);

    AssessmentSubmitResultVO result = buildSubmitResultVO(
        record, scale, totalScore, resultLevel, resultSummary, suggestion
    );

    AssessmentResponseMessage response = success(requestId, "提交成功");
    response.setSubmitResult(result);
    return response;
  }

  private AssessmentResponseMessage getRecords(String requestId, String studentId) {
    studentId = trimToNull(studentId);
    if (studentId == null) {
      return fail(requestId, "学号不能为空");
    }

    List<AssessmentRecord> records = recordRepository.findByStudentIdOrderBySubmittedAtDescIdDesc(studentId);
    List<AssessmentRecordVO> list = records.stream()
        .map(this::buildRecordVO)
        .collect(Collectors.toList());

    AssessmentResponseMessage response = success(requestId, "查询成功");
    response.setRecords(list);
    return response;
  }

  private AssessmentRecord buildEmptyRecord(String studentId, String semester) {
    AssessmentRecord record = new AssessmentRecord();
    record.setStudentId(studentId);
    record.setSemester(semester);

    record.setK10Status(STATUS_UNDONE);
    record.setWho5Status(STATUS_UNDONE);
    record.setPhq9Status(STATUS_UNDONE);
    record.setGad7Status(STATUS_UNDONE);

    record.setHealthStatus(STATUS_UNDONE);
    record.setHealthSummary("四项量表未全部完成，暂不生成综合总分");
    return record;
  }

  private void applyScaleResult(AssessmentRecord record, String scaleCode,
      Integer totalScore, String resultLevel, String resultSummary) {
    switch (scaleCode) {
      case "K10" -> {
        record.setK10Score(totalScore);
        record.setK10Status(STATUS_DONE);
        record.setK10Level(resultLevel);
        record.setK10Summary(resultSummary);
      }
      case "WHO5" -> {
        record.setWho5Score(totalScore);
        record.setWho5Status(STATUS_DONE);
        record.setWho5Level(resultLevel);
        record.setWho5Summary(resultSummary);
      }
      case "PHQ9" -> {
        record.setPhq9Score(totalScore);
        record.setPhq9Status(STATUS_DONE);
        record.setPhq9Level(resultLevel);
        record.setPhq9Summary(resultSummary);
      }
      case "GAD7" -> {
        record.setGad7Score(totalScore);
        record.setGad7Status(STATUS_DONE);
        record.setGad7Level(resultLevel);
        record.setGad7Summary(resultSummary);
      }
      default -> throw new IllegalArgumentException("不支持的量表编码");
    }
  }

  private void recalculateHealthResult(AssessmentRecord record) {
    if (!allScalesCompleted(record)) {
      record.setHealthTotalScore(null);
      record.setHealthStatus(STATUS_UNDONE);
      record.setHealthSummary("四项量表未全部完成，暂不生成综合总分");
      return;
    }

    double k10Risk = ((record.getK10Score() - 10) / 40.0) * 100.0;
    double who5Risk = ((25 - record.getWho5Score()) / 25.0) * 100.0;
    double phq9Risk = (record.getPhq9Score() / 27.0) * 100.0;
    double gad7Risk = (record.getGad7Score() / 21.0) * 100.0;

    int total = (int) Math.round((k10Risk + who5Risk + phq9Risk + gad7Risk) / 4.0);
    if (total < 0) {
      total = 0;
    }
    if (total > 100) {
      total = 100;
    }

    record.setHealthTotalScore(total);

    Optional<AssessmentTotalRule> ruleOptional =
        totalRuleRepository.findFirstByMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(total, total);

    if (ruleOptional.isPresent()) {
      AssessmentTotalRule rule = ruleOptional.get();
      record.setHealthStatus(rule.getHealthStatus());
      record.setHealthSummary(rule.getHealthSummary());
    } else {
      record.setHealthStatus("未分级");
      record.setHealthSummary("暂无综合结果说明");
    }
  }

  private boolean allScalesCompleted(AssessmentRecord record) {
    return STATUS_DONE.equals(record.getK10Status())
        && STATUS_DONE.equals(record.getWho5Status())
        && STATUS_DONE.equals(record.getPhq9Status())
        && STATUS_DONE.equals(record.getGad7Status())
        && record.getK10Score() != null
        && record.getWho5Score() != null
        && record.getPhq9Score() != null
        && record.getGad7Score() != null;
  }

  private int countCompletedScales(AssessmentRecord record) {
    int count = 0;
    if (STATUS_DONE.equals(record.getK10Status())) {
      count++;
    }
    if (STATUS_DONE.equals(record.getWho5Status())) {
      count++;
    }
    if (STATUS_DONE.equals(record.getPhq9Status())) {
      count++;
    }
    if (STATUS_DONE.equals(record.getGad7Status())) {
      count++;
    }
    return count;
  }

  private AssessmentSubmitResultVO buildSubmitResultVO(AssessmentRecord record,
      AssessmentScale scale, Integer scaleScore, String resultLevel,
      String resultSummary, String suggestion) {
    AssessmentSubmitResultVO vo = new AssessmentSubmitResultVO();
    vo.setRecordId(record.getId());
    vo.setSemester(record.getSemester());

    vo.setScaleId(scale.getId());
    vo.setScaleCode(scale.getScaleCode());
    vo.setScaleName(scale.getScaleName());
    vo.setScaleScore(scaleScore);
    vo.setScaleStatus(STATUS_DONE);
    vo.setScaleResultLevel(resultLevel);
    vo.setScaleResultSummary(resultSummary);
    vo.setSuggestion(suggestion);

    vo.setCompletedCount(countCompletedScales(record));
    vo.setTotalScaleCount(4);

    vo.setHealthTotalScore(record.getHealthTotalScore());
    vo.setHealthStatus(record.getHealthStatus());
    vo.setHealthSummary(record.getHealthSummary());
    return vo;
  }

  private AssessmentRecordVO buildRecordVO(AssessmentRecord record) {
    AssessmentRecordVO vo = new AssessmentRecordVO();
    vo.setRecordId(record.getId());
    vo.setSemester(record.getSemester());

    vo.setK10Score(record.getK10Score());
    vo.setK10Status(defaultStatus(record.getK10Status()));
    vo.setK10Level(record.getK10Level());
    vo.setK10Summary(record.getK10Summary());

    vo.setWho5Score(record.getWho5Score());
    vo.setWho5Status(defaultStatus(record.getWho5Status()));
    vo.setWho5Level(record.getWho5Level());
    vo.setWho5Summary(record.getWho5Summary());

    vo.setPhq9Score(record.getPhq9Score());
    vo.setPhq9Status(defaultStatus(record.getPhq9Status()));
    vo.setPhq9Level(record.getPhq9Level());
    vo.setPhq9Summary(record.getPhq9Summary());

    vo.setGad7Score(record.getGad7Score());
    vo.setGad7Status(defaultStatus(record.getGad7Status()));
    vo.setGad7Level(record.getGad7Level());
    vo.setGad7Summary(record.getGad7Summary());

    vo.setHealthTotalScore(record.getHealthTotalScore());
    vo.setHealthStatus(defaultStatus(record.getHealthStatus()));
    vo.setHealthSummary(record.getHealthSummary());
    vo.setSubmittedAt(record.getSubmittedAt());
    return vo;
  }

  private String defaultStatus(String status) {
    return status == null || status.trim().isEmpty() ? STATUS_UNDONE : status;
  }

  private boolean isSupportedScale(String scaleCode) {
    return "K10".equals(scaleCode)
        || "WHO5".equals(scaleCode)
        || "PHQ9".equals(scaleCode)
        || "GAD7".equals(scaleCode);
  }

  private String normalizeScaleCode(String scaleCode) {
    return scaleCode == null ? "" : scaleCode.trim().toUpperCase(Locale.ROOT);
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
