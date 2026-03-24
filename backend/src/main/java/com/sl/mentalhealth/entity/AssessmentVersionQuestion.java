package com.sl.mentalhealth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_version_question")
public class AssessmentVersionQuestion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "version_id", nullable = false)
  private Long versionId;

  @Column(name = "question_no", nullable = false)
  private Integer questionNo;

  @Column(name = "question_text", nullable = false, length = 500)
  private String questionText;

  @Column(name = "required_flag", nullable = false)
  private Integer requiredFlag = 1;

  @Column(name = "created_at", insertable = false, updatable = false)
  private LocalDateTime createdAt;

  public Long getId() {
    return id;
  }

  public Long getVersionId() {
    return versionId;
  }

  public void setVersionId(Long versionId) {
    this.versionId = versionId;
  }

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

  public Integer getRequiredFlag() {
    return requiredFlag;
  }

  public void setRequiredFlag(Integer requiredFlag) {
    this.requiredFlag = requiredFlag;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}