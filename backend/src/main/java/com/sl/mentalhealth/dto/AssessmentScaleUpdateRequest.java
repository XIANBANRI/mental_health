package com.sl.mentalhealth.dto;

import java.util.List;

public class AssessmentScaleUpdateRequest {

  private Long scaleId;
  private String scaleName;
  private String scaleType;
  private String description;
  private String versionRemark;
  private String operator;
  private List<QuestionDTO> questions;
  private List<RuleDTO> rules;

  public static class QuestionDTO {
    private Integer questionNo;
    private String questionText;
    private List<OptionDTO> options;

    public Integer getQuestionNo() {
      return questionNo;
    }

    public void setQuestionNo(Integer questionNo) {
      this.questionNo = questionNo;
    }

    public String getQuestionText() {
      return questionText;
    }

    public void setQuestionText(String questionText) {
      this.questionText = questionText;
    }

    public List<OptionDTO> getOptions() {
      return options;
    }

    public void setOptions(List<OptionDTO> options) {
      this.options = options;
    }
  }

  public static class OptionDTO {
    private Integer optionNo;
    private String optionText;
    private Integer optionScore;

    public Integer getOptionNo() {
      return optionNo;
    }

    public void setOptionNo(Integer optionNo) {
      this.optionNo = optionNo;
    }

    public String getOptionText() {
      return optionText;
    }

    public void setOptionText(String optionText) {
      this.optionText = optionText;
    }

    public Integer getOptionScore() {
      return optionScore;
    }

    public void setOptionScore(Integer optionScore) {
      this.optionScore = optionScore;
    }
  }

  public static class RuleDTO {
    private Integer minScore;
    private Integer maxScore;
    private String resultLevel;
    private String resultSummary;
    private String suggestion;

    public Integer getMinScore() {
      return minScore;
    }

    public void setMinScore(Integer minScore) {
      this.minScore = minScore;
    }

    public Integer getMaxScore() {
      return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
      this.maxScore = maxScore;
    }

    public String getResultLevel() {
      return resultLevel;
    }

    public void setResultLevel(String resultLevel) {
      this.resultLevel = resultLevel;
    }

    public String getResultSummary() {
      return resultSummary;
    }

    public void setResultSummary(String resultSummary) {
      this.resultSummary = resultSummary;
    }

    public String getSuggestion() {
      return suggestion;
    }

    public void setSuggestion(String suggestion) {
      this.suggestion = suggestion;
    }
  }

  public Long getScaleId() {
    return scaleId;
  }

  public void setScaleId(Long scaleId) {
    this.scaleId = scaleId;
  }

  public String getScaleName() {
    return scaleName;
  }

  public void setScaleName(String scaleName) {
    this.scaleName = scaleName;
  }

  public String getScaleType() {
    return scaleType;
  }

  public void setScaleType(String scaleType) {
    this.scaleType = scaleType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getVersionRemark() {
    return versionRemark;
  }

  public void setVersionRemark(String versionRemark) {
    this.versionRemark = versionRemark;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public List<QuestionDTO> getQuestions() {
    return questions;
  }

  public void setQuestions(List<QuestionDTO> questions) {
    this.questions = questions;
  }

  public List<RuleDTO> getRules() {
    return rules;
  }

  public void setRules(List<RuleDTO> rules) {
    this.rules = rules;
  }
}