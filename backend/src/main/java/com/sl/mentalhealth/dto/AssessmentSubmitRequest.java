package com.sl.mentalhealth.dto;

import java.util.List;

public class AssessmentSubmitRequest {

  private String studentId;
  private String semester;
  private Long scaleId;
  private List<AssessmentSubmitAnswerDTO> answers;

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

  public Long getScaleId() {
    return scaleId;
  }

  public void setScaleId(Long scaleId) {
    this.scaleId = scaleId;
  }

  public List<AssessmentSubmitAnswerDTO> getAnswers() {
    return answers;
  }

  public void setAnswers(List<AssessmentSubmitAnswerDTO> answers) {
    this.answers = answers;
  }
}
