package com.sl.mentalhealth.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_answer")
public class AssessmentAnswer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "record_id")
  private Long recordId;

  @Column(name = "question_id")
  private Long questionId;

  @Column(name = "option_id")
  private Long optionId;

  @Column(name = "answer_score")
  private Integer answerScore;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }

  public Long getQuestionId() {
    return questionId;
  }

  public void setQuestionId(Long questionId) {
    this.questionId = questionId;
  }

  public Long getOptionId() {
    return optionId;
  }

  public void setOptionId(Long optionId) {
    this.optionId = optionId;
  }

  public Integer getAnswerScore() {
    return answerScore;
  }

  public void setAnswerScore(Integer answerScore) {
    this.answerScore = answerScore;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
