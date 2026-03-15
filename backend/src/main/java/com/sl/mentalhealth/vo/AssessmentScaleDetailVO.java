package com.sl.mentalhealth.vo;

import java.util.List;

public class AssessmentScaleDetailVO {

  private Long scaleId;
  private String scaleCode;
  private String scaleName;
  private String description;
  private List<AssessmentQuestionVO> questions;

  public AssessmentScaleDetailVO() {
  }

  public AssessmentScaleDetailVO(Long scaleId, String scaleCode, String scaleName,
      String description, List<AssessmentQuestionVO> questions) {
    this.scaleId = scaleId;
    this.scaleCode = scaleCode;
    this.scaleName = scaleName;
    this.description = description;
    this.questions = questions;
  }

  public Long getScaleId() {
    return scaleId;
  }

  public void setScaleId(Long scaleId) {
    this.scaleId = scaleId;
  }

  public String getScaleCode() {
    return scaleCode;
  }

  public void setScaleCode(String scaleCode) {
    this.scaleCode = scaleCode;
  }

  public String getScaleName() {
    return scaleName;
  }

  public void setScaleName(String scaleName) {
    this.scaleName = scaleName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<AssessmentQuestionVO> getQuestions() {
    return questions;
  }

  public void setQuestions(List<AssessmentQuestionVO> questions) {
    this.questions = questions;
  }
}