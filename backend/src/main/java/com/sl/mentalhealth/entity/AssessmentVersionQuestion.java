package com.sl.mentalhealth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "assessment_version_question")
public class AssessmentVersionQuestion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Column(name = "version_id", nullable = false)
  private Long versionId;

  @Setter
  @Column(name = "question_no", nullable = false)
  private Integer questionNo;

  @Setter
  @Column(name = "question_text", nullable = false, length = 500)
  private String questionText;

  @Setter
  @Column(name = "required_flag", nullable = false)
  private Integer requiredFlag = 1;

  @Column(name = "created_at", insertable = false, updatable = false)
  private LocalDateTime createdAt;

}