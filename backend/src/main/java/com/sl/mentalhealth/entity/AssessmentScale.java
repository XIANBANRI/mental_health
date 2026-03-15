package com.sl.mentalhealth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_scale")
public class AssessmentScale {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "scale_code")
  private String scaleCode;

  @Column(name = "scale_name")
  private String scaleName;

  @Column(name = "scale_type")
  private String scaleType;

  @Column(name = "description")
  private String description;

  @Column(name = "question_count")
  private Integer questionCount;

  @Column(name = "score_min")
  private Integer scoreMin;

  @Column(name = "score_max")
  private Integer scoreMax;

  @Column(name = "status")
  private Integer status;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getScaleType() {
    return scaleType;
  }

  public void setScaleType(String scaleType) {
    this.scaleType = scaleType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getQuestionCount() {
    return questionCount;
  }

  public void setQuestionCount(Integer questionCount) {
    this.questionCount = questionCount;
  }

  public Integer getScoreMin() {
    return scoreMin;
  }

  public void setScoreMin(Integer scoreMin) {
    this.scoreMin = scoreMin;
  }

  public Integer getScoreMax() {
    return scoreMax;
  }

  public void setScoreMax(Integer scoreMax) {
    this.scoreMax = scoreMax;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}