package com.sl.mentalhealth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherAssessmentRecordVO {
  private Long id;
  private String studentId;
  private String studentName;
  private String college;
  private String className;
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

  private String submittedAt;
}