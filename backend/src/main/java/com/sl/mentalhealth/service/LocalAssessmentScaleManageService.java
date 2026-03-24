package com.sl.mentalhealth.service;

import com.sl.mentalhealth.dto.AssessmentScaleUpdateRequest;
import com.sl.mentalhealth.entity.AssessmentScale;
import com.sl.mentalhealth.entity.AssessmentScaleVersion;
import com.sl.mentalhealth.entity.AssessmentVersionOption;
import com.sl.mentalhealth.entity.AssessmentVersionQuestion;
import com.sl.mentalhealth.entity.AssessmentVersionRule;
import com.sl.mentalhealth.kafka.message.AssessmentScaleManageRequestMessage;
import com.sl.mentalhealth.repository.AssessmentScaleRepository;
import com.sl.mentalhealth.repository.AssessmentScaleVersionRepository;
import com.sl.mentalhealth.repository.AssessmentVersionOptionRepository;
import com.sl.mentalhealth.repository.AssessmentVersionQuestionRepository;
import com.sl.mentalhealth.repository.AssessmentVersionRuleRepository;
import com.sl.mentalhealth.vo.AssessmentScaleManageVO;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocalAssessmentScaleManageService {

  private final AssessmentScaleRepository assessmentScaleRepository;
  private final AssessmentScaleVersionRepository assessmentScaleVersionRepository;
  private final AssessmentVersionQuestionRepository assessmentVersionQuestionRepository;
  private final AssessmentVersionOptionRepository assessmentVersionOptionRepository;
  private final AssessmentVersionRuleRepository assessmentVersionRuleRepository;

  public LocalAssessmentScaleManageService(
      AssessmentScaleRepository assessmentScaleRepository,
      AssessmentScaleVersionRepository assessmentScaleVersionRepository,
      AssessmentVersionQuestionRepository assessmentVersionQuestionRepository,
      AssessmentVersionOptionRepository assessmentVersionOptionRepository,
      AssessmentVersionRuleRepository assessmentVersionRuleRepository) {
    this.assessmentScaleRepository = assessmentScaleRepository;
    this.assessmentScaleVersionRepository = assessmentScaleVersionRepository;
    this.assessmentVersionQuestionRepository = assessmentVersionQuestionRepository;
    this.assessmentVersionOptionRepository = assessmentVersionOptionRepository;
    this.assessmentVersionRuleRepository = assessmentVersionRuleRepository;
  }

  @Transactional
  public String importScale(AssessmentScaleManageRequestMessage request) {
    if (assessmentScaleRepository.findByScaleCode(request.getScaleCode()).isPresent()) {
      throw new RuntimeException("量表编码已存在");
    }
    if (request.getQuestions() == null || request.getQuestions().isEmpty()) {
      throw new RuntimeException("题目不能为空");
    }
    if (request.getRules() == null || request.getRules().isEmpty()) {
      throw new RuntimeException("规则不能为空");
    }

    int scoreMin = request.getRules().stream().mapToInt(AssessmentScaleUpdateRequest.RuleDTO::getMinScore).min().orElse(0);
    int scoreMax = request.getRules().stream().mapToInt(AssessmentScaleUpdateRequest.RuleDTO::getMaxScore).max().orElse(0);

    AssessmentScale scale = new AssessmentScale();
    scale.setScaleCode(request.getScaleCode());
    scale.setScaleName(request.getScaleName());
    scale.setScaleType(request.getScaleType());
    scale.setDescription(request.getDescription());
    scale.setQuestionCount(request.getQuestions().size());
    scale.setScoreMin(scoreMin);
    scale.setScoreMax(scoreMax);
    scale.setStatus(1);
    scale.setDeletedFlag(0);
    scale.setCreatedBy(request.getOperator());
    scale = assessmentScaleRepository.save(scale);

    AssessmentScaleVersion version = new AssessmentScaleVersion();
    version.setScaleId(scale.getId());
    version.setVersionNo(1);
    version.setVersionStatus("ACTIVE");
    version.setSourceQuestionFileName(request.getQuestionFileName());
    version.setSourceRuleFileName(request.getRuleFileName());
    version.setVersionRemark("首次导入");
    version.setCreatedBy(request.getOperator());
    version = assessmentScaleVersionRepository.save(version);

    saveQuestionsAndRules(version.getId(), request.getQuestions(), request.getRules());

    scale.setCurrentVersionId(version.getId());
    assessmentScaleRepository.save(scale);

    return "导入成功";
  }

  public List<AssessmentScaleManageVO> listAll() {
    List<AssessmentScale> scales = assessmentScaleRepository.findByDeletedFlagOrderByIdDesc(0);

    Map<Long, AssessmentScaleVersion> versionMap = assessmentScaleVersionRepository.findAll().stream()
        .collect(Collectors.toMap(AssessmentScaleVersion::getId, v -> v, (a, b) -> a));

    List<AssessmentScaleManageVO> result = new ArrayList<>();
    for (AssessmentScale scale : scales) {
      AssessmentScaleManageVO vo = new AssessmentScaleManageVO();
      vo.setScaleId(scale.getId());
      vo.setScaleCode(scale.getScaleCode());
      vo.setScaleName(scale.getScaleName());
      vo.setScaleType(scale.getScaleType());
      vo.setQuestionCount(scale.getQuestionCount());
      vo.setScoreMin(scale.getScoreMin());
      vo.setScoreMax(scale.getScoreMax());
      vo.setStatus(scale.getStatus());
      vo.setDeletedFlag(scale.getDeletedFlag());
      if (scale.getCurrentVersionId() != null && versionMap.containsKey(scale.getCurrentVersionId())) {
        vo.setCurrentVersionNo(versionMap.get(scale.getCurrentVersionId()).getVersionNo());
      }
      result.add(vo);
    }
    return result;
  }

  public Map<String, Object> getScaleDetail(Long scaleId) {
    AssessmentScale scale = assessmentScaleRepository.findById(scaleId)
        .orElseThrow(() -> new RuntimeException("量表不存在"));

    if (scale.getCurrentVersionId() == null) {
      throw new RuntimeException("量表当前版本不存在");
    }

    AssessmentScaleVersion version = assessmentScaleVersionRepository.findById(scale.getCurrentVersionId())
        .orElseThrow(() -> new RuntimeException("版本不存在"));

    List<AssessmentVersionQuestion> questions =
        assessmentVersionQuestionRepository.findByVersionIdOrderByQuestionNoAsc(version.getId());

    List<Long> questionIds = questions.stream().map(AssessmentVersionQuestion::getId).toList();
    List<AssessmentVersionOption> options = questionIds.isEmpty()
        ? Collections.emptyList()
        : assessmentVersionOptionRepository.findByVersionQuestionIdIn(questionIds);

    Map<Long, List<AssessmentVersionOption>> optionMap = options.stream()
        .collect(Collectors.groupingBy(AssessmentVersionOption::getVersionQuestionId));

    List<Map<String, Object>> questionList = new ArrayList<>();
    for (AssessmentVersionQuestion question : questions) {
      Map<String, Object> item = new LinkedHashMap<>();
      item.put("id", question.getId());
      item.put("questionNo", question.getQuestionNo());
      item.put("questionText", question.getQuestionText());
      item.put(
          "options",
          optionMap.getOrDefault(question.getId(), Collections.emptyList())
              .stream()
              .sorted(Comparator.comparing(AssessmentVersionOption::getOptionNo))
              .toList()
      );
      questionList.add(item);
    }

    List<AssessmentVersionRule> rules =
        assessmentVersionRuleRepository.findByVersionIdOrderByMinScoreAsc(version.getId());

    Map<String, Object> result = new LinkedHashMap<>();
    result.put("scale", scale);
    result.put("version", version);
    result.put("questions", questionList);
    result.put("rules", rules);
    return result;
  }

  @Transactional
  public String updateScale(AssessmentScaleManageRequestMessage request) {
    AssessmentScale scale = assessmentScaleRepository.findById(request.getScaleId())
        .orElseThrow(() -> new RuntimeException("量表不存在"));

    if (request.getQuestions() == null || request.getQuestions().isEmpty()) {
      throw new RuntimeException("题目不能为空");
    }
    if (request.getRules() == null || request.getRules().isEmpty()) {
      throw new RuntimeException("规则不能为空");
    }

    AssessmentScaleVersion latestVersion = assessmentScaleVersionRepository
        .findFirstByScaleIdOrderByVersionNoDesc(scale.getId())
        .orElseThrow(() -> new RuntimeException("旧版本不存在"));

    int nextVersionNo = latestVersion.getVersionNo() + 1;

    AssessmentScaleVersion newVersion = new AssessmentScaleVersion();
    newVersion.setScaleId(scale.getId());
    newVersion.setVersionNo(nextVersionNo);
    newVersion.setVersionStatus(scale.getStatus() == 1 ? "ACTIVE" : "INACTIVE");
    newVersion.setVersionRemark(request.getVersionRemark());
    newVersion.setCreatedBy(request.getOperator());
    newVersion = assessmentScaleVersionRepository.save(newVersion);

    saveQuestionsAndRules(newVersion.getId(), request.getQuestions(), request.getRules());

    int scoreMin = request.getRules().stream().mapToInt(AssessmentScaleUpdateRequest.RuleDTO::getMinScore).min().orElse(0);
    int scoreMax = request.getRules().stream().mapToInt(AssessmentScaleUpdateRequest.RuleDTO::getMaxScore).max().orElse(0);

    scale.setScaleName(request.getScaleName());
    scale.setScaleType(request.getScaleType());
    scale.setDescription(request.getDescription());
    scale.setQuestionCount(request.getQuestions().size());
    scale.setScoreMin(scoreMin);
    scale.setScoreMax(scoreMax);
    scale.setCurrentVersionId(newVersion.getId());
    assessmentScaleRepository.save(scale);

    return "修改成功，已生成新版本";
  }

  @Transactional
  public String enableScale(Long scaleId) {
    AssessmentScale scale = assessmentScaleRepository.findById(scaleId)
        .orElseThrow(() -> new RuntimeException("量表不存在"));

    if (scale.getDeletedFlag() == 1) {
      throw new RuntimeException("量表已删除，不能启用");
    }

    scale.setStatus(1);
    assessmentScaleRepository.save(scale);

    if (scale.getCurrentVersionId() != null) {
      AssessmentScaleVersion version = assessmentScaleVersionRepository.findById(scale.getCurrentVersionId())
          .orElseThrow(() -> new RuntimeException("当前版本不存在"));
      version.setVersionStatus("ACTIVE");
      assessmentScaleVersionRepository.save(version);
    }

    return "启用成功";
  }

  @Transactional
  public String disableScale(Long scaleId) {
    AssessmentScale scale = assessmentScaleRepository.findById(scaleId)
        .orElseThrow(() -> new RuntimeException("量表不存在"));

    scale.setStatus(0);
    assessmentScaleRepository.save(scale);

    if (scale.getCurrentVersionId() != null) {
      AssessmentScaleVersion version = assessmentScaleVersionRepository.findById(scale.getCurrentVersionId())
          .orElseThrow(() -> new RuntimeException("当前版本不存在"));
      version.setVersionStatus("INACTIVE");
      assessmentScaleVersionRepository.save(version);
    }

    return "停用成功";
  }

  @Transactional
  public String deleteScale(Long scaleId) {
    AssessmentScale scale = assessmentScaleRepository.findById(scaleId)
        .orElseThrow(() -> new RuntimeException("量表不存在"));

    scale.setDeletedFlag(1);
    scale.setStatus(0);
    assessmentScaleRepository.save(scale);

    if (scale.getCurrentVersionId() != null) {
      AssessmentScaleVersion version = assessmentScaleVersionRepository.findById(scale.getCurrentVersionId())
          .orElseThrow(() -> new RuntimeException("当前版本不存在"));
      version.setVersionStatus("INACTIVE");
      assessmentScaleVersionRepository.save(version);
    }

    return "删除成功（逻辑删除）";
  }

  private void saveQuestionsAndRules(
      Long versionId,
      List<AssessmentScaleUpdateRequest.QuestionDTO> questionDTOList,
      List<AssessmentScaleUpdateRequest.RuleDTO> ruleDTOList) {

    for (AssessmentScaleUpdateRequest.QuestionDTO questionDTO : questionDTOList) {
      AssessmentVersionQuestion question = new AssessmentVersionQuestion();
      question.setVersionId(versionId);
      question.setQuestionNo(questionDTO.getQuestionNo());
      question.setQuestionText(questionDTO.getQuestionText());
      question.setRequiredFlag(1);
      question = assessmentVersionQuestionRepository.save(question);

      if (questionDTO.getOptions() != null) {
        for (AssessmentScaleUpdateRequest.OptionDTO optionDTO : questionDTO.getOptions()) {
          AssessmentVersionOption option = new AssessmentVersionOption();
          option.setVersionQuestionId(question.getId());
          option.setOptionNo(optionDTO.getOptionNo());
          option.setOptionText(optionDTO.getOptionText());
          option.setOptionScore(optionDTO.getOptionScore());
          assessmentVersionOptionRepository.save(option);
        }
      }
    }

    for (AssessmentScaleUpdateRequest.RuleDTO ruleDTO : ruleDTOList) {
      AssessmentVersionRule rule = new AssessmentVersionRule();
      rule.setVersionId(versionId);
      rule.setMinScore(ruleDTO.getMinScore());
      rule.setMaxScore(ruleDTO.getMaxScore());
      rule.setResultLevel(ruleDTO.getResultLevel());
      rule.setResultSummary(ruleDTO.getResultSummary());
      rule.setSuggestion(ruleDTO.getSuggestion());
      assessmentVersionRuleRepository.save(rule);
    }
  }
}