package com.sl.mentalhealth.vo;

import java.time.LocalDateTime;

public class AssessmentRecordVO {

  private Long recordId;
  private String semester;

  private Integer k10Score;
  private String k10Status;
  private String k10Level;
  private String k10Summary;

  private Integer who5Score;
  private String who5Status;
  private String who5Level;
  private String who5Summary;

  private Integer phq9Score;
  private String phq9Status;
  private String phq9Level;
  private String phq9Summary;

  private Integer gad7Score;
  private String gad7Status;
  private String gad7Level;
  private String gad7Summary;

  private Integer healthTotalScore;
  private String healthStatus;
  private String healthSummary;

  private LocalDateTime submittedAt;

  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }

  public String getSemester() {
    return semester;
  }

  public void setSemester(String semester) {
    this.semester = semester;
  }

  public Integer getK10Score() {
    return k10Score;
  }

  public void setK10Score(Integer k10Score) {
    this.k10Score = k10Score;
  }

  public String getK10Status() {
    return k10Status;
  }

  public void setK10Status(String k10Status) {
    this.k10Status = k10Status;
  }

  public String getK10Level() {
    return k10Level;
  }

  public void setK10Level(String k10Level) {
    this.k10Level = k10Level;
  }

  public String getK10Summary() {
    return k10Summary;
  }

  public void setK10Summary(String k10Summary) {
    this.k10Summary = k10Summary;
  }

  public Integer getWho5Score() {
    return who5Score;
  }

  public void setWho5Score(Integer who5Score) {
    this.who5Score = who5Score;
  }

  public String getWho5Status() {
    return who5Status;
  }

  public void setWho5Status(String who5Status) {
    this.who5Status = who5Status;
  }

  public String getWho5Level() {
    return who5Level;
  }

  public void setWho5Level(String who5Level) {
    this.who5Level = who5Level;
  }

  public String getWho5Summary() {
    return who5Summary;
  }

  public void setWho5Summary(String who5Summary) {
    this.who5Summary = who5Summary;
  }

  public Integer getPhq9Score() {
    return phq9Score;
  }

  public void setPhq9Score(Integer phq9Score) {
    this.phq9Score = phq9Score;
  }

  public String getPhq9Status() {
    return phq9Status;
  }

  public void setPhq9Status(String phq9Status) {
    this.phq9Status = phq9Status;
  }

  public String getPhq9Level() {
    return phq9Level;
  }

  public void setPhq9Level(String phq9Level) {
    this.phq9Level = phq9Level;
  }

  public String getPhq9Summary() {
    return phq9Summary;
  }

  public void setPhq9Summary(String phq9Summary) {
    this.phq9Summary = phq9Summary;
  }

  public Integer getGad7Score() {
    return gad7Score;
  }

  public void setGad7Score(Integer gad7Score) {
    this.gad7Score = gad7Score;
  }

  public String getGad7Status() {
    return gad7Status;
  }

  public void setGad7Status(String gad7Status) {
    this.gad7Status = gad7Status;
  }

  public String getGad7Level() {
    return gad7Level;
  }

  public void setGad7Level(String gad7Level) {
    this.gad7Level = gad7Level;
  }

  public String getGad7Summary() {
    return gad7Summary;
  }

  public void setGad7Summary(String gad7Summary) {
    this.gad7Summary = gad7Summary;
  }

  public Integer getHealthTotalScore() {
    return healthTotalScore;
  }

  public void setHealthTotalScore(Integer healthTotalScore) {
    this.healthTotalScore = healthTotalScore;
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

  public LocalDateTime getSubmittedAt() {
    return submittedAt;
  }

  public void setSubmittedAt(LocalDateTime submittedAt) {
    this.submittedAt = submittedAt;
  }
}
