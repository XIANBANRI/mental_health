package com.sl.mentalhealth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_version_rule")
public class AssessmentVersionRule {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "version_id", nullable = false)
  private Long versionId;

  @Column(name = "min_score", nullable = false)
  private Integer minScore;

  @Column(name = "max_score", nullable = false)
  private Integer maxScore;

  @Column(name = "result_level", nullable = false, length = 50)
  private String resultLevel;

  @Column(name = "result_summary", nullable = false, length = 255)
  private String resultSummary;

  @Column(name = "suggestion", length = 500)
  private String suggestion;

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

  public Integer getMinScore() {
    return minScore;
  }

  public void setMinScore(Integer minScore) {
    this.minScore = minScore;
  }

  public Integer getMaxScore() {
    return maxScore;
  }

  public void setMaxScore(Integer maxScore) {
    this.maxScore = maxScore;
  }

  public String getResultLevel() {
    return resultLevel;
  }

  public void setResultLevel(String resultLevel) {
    this.resultLevel = resultLevel;
  }

  public String getResultSummary() {
    return resultSummary;
  }

  public void setResultSummary(String resultSummary) {
    this.resultSummary = resultSummary;
  }

  public String getSuggestion() {
    return suggestion;
  }

  public void setSuggestion(String suggestion) {
    this.suggestion = suggestion;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}