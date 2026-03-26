package com.sl.mentalhealth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_assessment_answer")
public class StudentAssessmentAnswer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "record_id", nullable = false)
  private Long recordId;

  @Column(name = "version_question_id", nullable = false)
  private Long versionQuestionId;

  @Column(name = "question_no", nullable = false)
  private Integer questionNo;

  @Column(name = "question_text", nullable = false, length = 500)
  private String questionText;

  @Column(name = "version_option_id", nullable = false)
  private Long versionOptionId;

  @Column(name = "option_no", nullable = false)
  private Integer optionNo;

  @Column(name = "option_text", nullable = false, length = 255)
  private String optionText;

  @Column(name = "answer_score", nullable = false)
  private Integer answerScore;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public Long getRecordId() { return recordId; }
  public void setRecordId(Long recordId) { this.recordId = recordId; }
  public Long getVersionQuestionId() { return versionQuestionId; }
  public void setVersionQuestionId(Long versionQuestionId) { this.versionQuestionId = versionQuestionId; }
  public Integer getQuestionNo() { return questionNo; }
  public void setQuestionNo(Integer questionNo) { this.questionNo = questionNo; }
  public String getQuestionText() { return questionText; }
  public void setQuestionText(String questionText) { this.questionText = questionText; }
  public Long getVersionOptionId() { return versionOptionId; }
  public void setVersionOptionId(Long versionOptionId) { this.versionOptionId = versionOptionId; }
  public Integer getOptionNo() { return optionNo; }
  public void setOptionNo(Integer optionNo) { this.optionNo = optionNo; }
  public String getOptionText() { return optionText; }
  public void setOptionText(String optionText) { this.optionText = optionText; }
  public Integer getAnswerScore() { return answerScore; }
  public void setAnswerScore(Integer answerScore) { this.answerScore = answerScore; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
