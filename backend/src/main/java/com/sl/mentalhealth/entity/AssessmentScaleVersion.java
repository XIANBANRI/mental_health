package com.sl.mentalhealth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_scale_version")
public class AssessmentScaleVersion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "scale_id", nullable = false)
  private Long scaleId;

  @Column(name = "version_no", nullable = false)
  private Integer versionNo;

  @Column(name = "version_status", nullable = false, length = 20)
  private String versionStatus;

  @Column(name = "source_question_file_name", length = 255)
  private String sourceQuestionFileName;

  @Column(name = "source_rule_file_name", length = 255)
  private String sourceRuleFileName;

  @Column(name = "version_remark", length = 255)
  private String versionRemark;

  @Column(name = "created_by", length = 50)
  private String createdBy;

  @Column(name = "created_at", insertable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", insertable = false, updatable = false)
  private LocalDateTime updatedAt;

  public Long getId() {
    return id;
  }

  public Long getScaleId() {
    return scaleId;
  }

  public void setScaleId(Long scaleId) {
    this.scaleId = scaleId;
  }

  public Integer getVersionNo() {
    return versionNo;
  }

  public void setVersionNo(Integer versionNo) {
    this.versionNo = versionNo;
  }

  public String getVersionStatus() {
    return versionStatus;
  }

  public void setVersionStatus(String versionStatus) {
    this.versionStatus = versionStatus;
  }

  public String getSourceQuestionFileName() {
    return sourceQuestionFileName;
  }

  public void setSourceQuestionFileName(String sourceQuestionFileName) {
    this.sourceQuestionFileName = sourceQuestionFileName;
  }

  public String getSourceRuleFileName() {
    return sourceRuleFileName;
  }

  public void setSourceRuleFileName(String sourceRuleFileName) {
    this.sourceRuleFileName = sourceRuleFileName;
  }

  public String getVersionRemark() {
    return versionRemark;
  }

  public void setVersionRemark(String versionRemark) {
    this.versionRemark = versionRemark;
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