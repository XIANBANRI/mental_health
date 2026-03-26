package com.sl.mentalhealth.dto;

public class AssessmentSubmitAnswer {

  private Long versionQuestionId;
  private Long versionOptionId;

  public Long getVersionQuestionId() {
    return versionQuestionId;
  }

  public void setVersionQuestionId(Long versionQuestionId) {
    this.versionQuestionId = versionQuestionId;
  }

  public Long getVersionOptionId() {
    return versionOptionId;
  }

  public void setVersionOptionId(Long versionOptionId) {
    this.versionOptionId = versionOptionId;
  }
}
