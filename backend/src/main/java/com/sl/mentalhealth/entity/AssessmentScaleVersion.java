package com.sl.mentalhealth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "assessment_scale_version")
public class AssessmentScaleVersion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Column(name = "scale_id", nullable = false)
  private Long scaleId;

  @Setter
  @Column(name = "version_no", nullable = false)
  private Integer versionNo;

  @Setter
  @Column(name = "version_status", nullable = false, length = 20)
  private String versionStatus;

  @Setter
  @Column(name = "source_question_file_name", length = 255)
  private String sourceQuestionFileName;

  @Setter
  @Column(name = "source_rule_file_name", length = 255)
  private String sourceRuleFileName;

  @Setter
  @Column(name = "version_remark", length = 255)
  private String versionRemark;

  @Setter
  @Column(name = "created_by", length = 50)
  private String createdBy;

  @Column(name = "created_at", insertable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", insertable = false, updatable = false)
  private LocalDateTime updatedAt;

}