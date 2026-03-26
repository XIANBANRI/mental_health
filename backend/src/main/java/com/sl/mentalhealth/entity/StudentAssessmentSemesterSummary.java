package com.sl.mentalhealth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "student_assessment_semester_summary",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_student_semester", columnNames = {"student_id", "semester"})
    }
)
public class StudentAssessmentSemesterSummary {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "student_id", nullable = false, length = 50)
  private String studentId;

  @Column(name = "semester", nullable = false, length = 20)
  private String semester;

  @Column(name = "tested_count", nullable = false)
  private Integer testedCount;

  @Column(name = "score_summary", length = 2000)
  private String scoreSummary;

  @Column(name = "semester_level", length = 20)
  private String semesterLevel;

  @Column(name = "last_tested_at")
  private LocalDateTime lastTestedAt;

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

  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public String getSemester() {
    return semester;
  }

  public void setSemester(String semester) {
    this.semester = semester;
  }

  public Integer getTestedCount() {
    return testedCount;
  }

  public void setTestedCount(Integer testedCount) {
    this.testedCount = testedCount;
  }

  public String getScoreSummary() {
    return scoreSummary;
  }

  public void setScoreSummary(String scoreSummary) {
    this.scoreSummary = scoreSummary;
  }

  public String getSemesterLevel() {
    return semesterLevel;
  }

  public void setSemesterLevel(String semesterLevel) {
    this.semesterLevel = semesterLevel;
  }

  public LocalDateTime getLastTestedAt() {
    return lastTestedAt;
  }

  public void setLastTestedAt(LocalDateTime lastTestedAt) {
    this.lastTestedAt = lastTestedAt;
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