package com.sl.mentalhealth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_scale")
public class AssessmentScale {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "scale_code", nullable = false, unique = true, length = 50)
  private String scaleCode;

  @Column(name = "scale_name", nullable = false, length = 100)
  private String scaleName;

  @Column(name = "scale_type", nullable = false, length = 50)
  private String scaleType;

  @Column(name = "description", length = 255)
  private String description;

  @Column(name = "question_count", nullable = false)
  private Integer questionCount;

  @Column(name = "score_min", nullable = false)
  private Integer scoreMin;

  @Column(name = "score_max", nullable = false)
  private Integer scoreMax;

  @Column(name = "status", nullable = false)
  private Integer status = 1;

  @Column(name = "deleted_flag", nullable = false)
  private Integer deletedFlag = 0;

  @Column(name = "current_version_id")
  private Long currentVersionId;

  @Column(name = "created_by", length = 50)
  private String createdBy;

  @Column(name = "created_at", insertable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", insertable = false, updatable = false)
  private LocalDateTime updatedAt;

  public Long getId() {
    return id;
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

  public Integer getDeletedFlag() {
    return deletedFlag;
  }

  public void setDeletedFlag(Integer deletedFlag) {
    this.deletedFlag = deletedFlag;
  }

  public Long getCurrentVersionId() {
    return currentVersionId;
  }

  public void setCurrentVersionId(Long currentVersionId) {
    this.currentVersionId = currentVersionId;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }
}