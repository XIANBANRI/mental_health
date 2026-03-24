package com.sl.mentalhealth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_version_option")
public class AssessmentVersionOption {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "version_question_id", nullable = false)
  private Long versionQuestionId;

  @Column(name = "option_no", nullable = false)
  private Integer optionNo;

  @Column(name = "option_text", nullable = false, length = 255)
  private String optionText;

  @Column(name = "option_score", nullable = false)
  private Integer optionScore;

  @Column(name = "created_at", insertable = false, updatable = false)
  private LocalDateTime createdAt;

  public Long getId() {
    return id;
  }

  public Long getVersionQuestionId() {
    return versionQuestionId;
  }

  public void setVersionQuestionId(Long versionQuestionId) {
    this.versionQuestionId = versionQuestionId;
  }

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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}