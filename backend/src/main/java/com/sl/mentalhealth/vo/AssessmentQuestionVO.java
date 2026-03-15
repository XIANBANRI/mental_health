package com.sl.mentalhealth.vo;

import java.util.List;

public class AssessmentQuestionVO {

  private Long id;
  private Integer questionNo;
  private String questionText;
  private List<AssessmentOptionVO> options;

  public AssessmentQuestionVO() {
  }

  public AssessmentQuestionVO(Long id, Integer questionNo, String questionText,
      List<AssessmentOptionVO> options) {
    this.id = id;
    this.questionNo = questionNo;
    this.questionText = questionText;
    this.options = options;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getQuestionNo() {
    return questionNo;
  }

  public void setQuestionNo(Integer questionNo) {
    this.questionNo = questionNo;
  }

  public String getQuestionText() {
    return questionText;
  }

  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }

  public List<AssessmentOptionVO> getOptions() {
    return options;
  }

  public void setOptions(List<AssessmentOptionVO> options) {
    this.options = options;
  }
}