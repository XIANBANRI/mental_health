package com.sl.mentalhealth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_option")
public class AssessmentOption {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "question_id")
  private Long questionId;

  @Column(name = "option_no")
  private Integer optionNo;

  @Column(name = "option_text")
  private String optionText;

  @Column(name = "option_score")
  private Integer optionScore;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getQuestionId() {
    return questionId;
  }

  public void setQuestionId(Long questionId) {
    this.questionId = questionId;
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

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}