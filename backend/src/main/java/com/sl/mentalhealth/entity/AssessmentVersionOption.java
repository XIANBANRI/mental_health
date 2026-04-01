package com.sl.mentalhealth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "assessment_version_option")
public class AssessmentVersionOption {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Column(name = "version_question_id", nullable = false)
  private Long versionQuestionId;

  @Setter
  @Column(name = "option_no", nullable = false)
  private Integer optionNo;

  @Setter
  @Column(name = "option_text", nullable = false, length = 255)
  private String optionText;

  @Setter
  @Column(name = "option_score", nullable = false)
  private Integer optionScore;

  @Column(name = "created_at", insertable = false, updatable = false)
  private LocalDateTime createdAt;

}