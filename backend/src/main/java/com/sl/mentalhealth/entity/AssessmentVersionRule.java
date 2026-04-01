package com.sl.mentalhealth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "assessment_version_rule")
public class AssessmentVersionRule {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Column(name = "version_id", nullable = false)
  private Long versionId;

  @Setter
  @Column(name = "min_score", nullable = false)
  private Integer minScore;

  @Setter
  @Column(name = "max_score", nullable = false)
  private Integer maxScore;

  @Setter
  @Column(name = "result_level", nullable = false, length = 50)
  private String resultLevel;

  @Setter
  @Column(name = "result_summary", nullable = false, length = 255)
  private String resultSummary;

  @Setter
  @Column(name = "suggestion", length = 500)
  private String suggestion;

  @Column(name = "created_at", insertable = false, updatable = false)
  private LocalDateTime createdAt;

}