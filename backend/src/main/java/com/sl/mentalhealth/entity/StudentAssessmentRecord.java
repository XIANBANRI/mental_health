package com.sl.mentalhealth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_assessment_record")
public class StudentAssessmentRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "student_id", nullable = false, length = 50)
  private String studentId;

  @Column(name = "semester", nullable = false, length = 20)
  private String semester;

  @Column(name = "scale_id", nullable = false)
  private Long scaleId;

  @Column(name = "scale_version_id", nullable = false)
  private Long scaleVersionId;

  @Column(name = "scale_code", nullable = false, length = 50)
  private String scaleCode;

  @Column(name = "scale_name", nullable = false, length = 100)
  private String scaleName;

  @Column(name = "raw_score", nullable = false)
  private Integer rawScore;

  @Column(name = "result_level", length = 50)
  private String resultLevel;

  @Column(name = "result_summary", length = 255)
  private String resultSummary;

  @Column(name = "suggestion", length = 500)
  private String suggestion;

  @Column(name = "status", nullable = false, length = 20)
  private String status;

  @Column(name = "submitted_at")
  private LocalDateTime submittedAt;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getStudentId() { return studentId; }
  public void setStudentId(String studentId) { this.studentId = studentId; }
  public String getSemester() { return semester; }
  public void setSemester(String semester) { this.semester = semester; }
  public Long getScaleId() { return scaleId; }
  public void setScaleId(Long scaleId) { this.scaleId = scaleId; }
  public Long getScaleVersionId() { return scaleVersionId; }
  public void setScaleVersionId(Long scaleVersionId) { this.scaleVersionId = scaleVersionId; }
  public String getScaleCode() { return scaleCode; }
  public void setScaleCode(String scaleCode) { this.scaleCode = scaleCode; }
  public String getScaleName() { return scaleName; }
  public void setScaleName(String scaleName) { this.scaleName = scaleName; }
  public Integer getRawScore() { return rawScore; }
  public void setRawScore(Integer rawScore) { this.rawScore = rawScore; }
  public String getResultLevel() { return resultLevel; }
  public void setResultLevel(String resultLevel) { this.resultLevel = resultLevel; }
  public String getResultSummary() { return resultSummary; }
  public void setResultSummary(String resultSummary) { this.resultSummary = resultSummary; }
  public String getSuggestion() { return suggestion; }
  public void setSuggestion(String suggestion) { this.suggestion = suggestion; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
  public LocalDateTime getSubmittedAt() { return submittedAt; }
  public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
