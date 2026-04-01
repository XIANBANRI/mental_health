package com.sl.mentalhealth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "assessment_scale")
public class AssessmentScale {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Column(name = "scale_code", nullable = false, unique = true, length = 50)
  private String scaleCode;

  @Setter
  @Column(name = "scale_name", nullable = false, length = 100)
  private String scaleName;

  @Setter
  @Column(name = "scale_type", nullable = false, length = 50)
  private String scaleType;

  @Setter
  @Column(name = "description", length = 255)
  private String description;

  @Setter
  @Column(name = "question_count", nullable = false)
  private Integer questionCount;

  @Setter
  @Column(name = "score_min", nullable = false)
  private Integer scoreMin;

  @Setter
  @Column(name = "score_max", nullable = false)
  private Integer scoreMax;

  @Setter
  @Column(name = "status", nullable = false)
  private Integer status = 1;

  @Setter
  @Column(name = "deleted_flag", nullable = false)
  private Integer deletedFlag = 0;

  @Setter
  @Column(name = "current_version_id")
  private Long currentVersionId;

  @Setter
  @Column(name = "created_by", length = 50)
  private String createdBy;

  @Column(name = "created_at", insertable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", insertable = false, updatable = false)
  private LocalDateTime updatedAt;

}