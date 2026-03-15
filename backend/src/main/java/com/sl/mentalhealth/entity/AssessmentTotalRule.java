package com.sl.mentalhealth.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "assessment_total_rule")
public class AssessmentTotalRule {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "min_score")
  private Integer minScore;

  @Column(name = "max_score")
  private Integer maxScore;

  @Column(name = "health_status")
  private String healthStatus;

  @Column(name = "health_summary")
  private String healthSummary;

  @Column(name = "suggestion")
  private String suggestion;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getHealthStatus() {
    return healthStatus;
  }

  public void setHealthStatus(String healthStatus) {
    this.healthStatus = healthStatus;
  }

  public String getHealthSummary() {
    return healthSummary;
  }

  public void setHealthSummary(String healthSummary) {
    this.healthSummary = healthSummary;
  }

  public String getSuggestion() {
    return suggestion;
  }

  public void setSuggestion(String suggestion) {
    this.suggestion = suggestion;
  }
}
