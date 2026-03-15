package com.sl.mentalhealth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_question")
public class AssessmentQuestion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "scale_id")
  private Long scaleId;

  @Column(name = "question_no")
  private Integer questionNo;

  @Column(name = "question_text")
  private String questionText;

  @Column(name = "required_flag")
  private Integer requiredFlag;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getScaleId() {
    return scaleId;
  }

  public void setScaleId(Long scaleId) {
    this.scaleId = scaleId;
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

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}