package com.sl.mentalhealth.vo;

public class AssessmentSubmitResultVO {

  private Long recordId;
  private String semester;

  private Long scaleId;
  private String scaleCode;
  private String scaleName;
  private Integer scaleScore;
  private String scaleStatus;
  private String scaleResultLevel;
  private String scaleResultSummary;
  private String suggestion;

  private Integer completedCount;
  private Integer totalScaleCount;

  private Integer healthTotalScore;
  private String healthStatus;
  private String healthSummary;

  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }

  public String getSemester() {
    return semester;
  }

  public void setSemester(String semester) {
    this.semester = semester;
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

  public Integer getScaleScore() {
    return scaleScore;
  }

  public void setScaleScore(Integer scaleScore) {
    this.scaleScore = scaleScore;
  }

  public String getScaleStatus() {
    return scaleStatus;
  }

  public void setScaleStatus(String scaleStatus) {
    this.scaleStatus = scaleStatus;
  }

  public String getScaleResultLevel() {
    return scaleResultLevel;
  }

  public void setScaleResultLevel(String scaleResultLevel) {
    this.scaleResultLevel = scaleResultLevel;
  }

  public String getScaleResultSummary() {
    return scaleResultSummary;
  }

  public void setScaleResultSummary(String scaleResultSummary) {
    this.scaleResultSummary = scaleResultSummary;
  }

  public String getSuggestion() {
    return suggestion;
  }

  public void setSuggestion(String suggestion) {
    this.suggestion = suggestion;
  }

  public Integer getCompletedCount() {
    return completedCount;
  }

  public void setCompletedCount(Integer completedCount) {
    this.completedCount = completedCount;
  }

  public Integer getTotalScaleCount() {
    return totalScaleCount;
  }

  public void setTotalScaleCount(Integer totalScaleCount) {
    this.totalScaleCount = totalScaleCount;
  }

  public Integer getHealthTotalScore() {
    return healthTotalScore;
  }

  public void setHealthTotalScore(Integer healthTotalScore) {
    this.healthTotalScore = healthTotalScore;
  }

  public String getHealthStatus() {
    return healthStatus;
  }

  public void setHealthStatus(String healthStatus) {
    this.healthStatus = healthStatus;
  }

  public String getHealthSummary() {
    return healthSummary;
  }

  public void setHealthSummary(String healthSummary) {
    this.healthSummary = healthSummary;
  }
}
