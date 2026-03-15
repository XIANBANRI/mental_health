package com.sl.mentalhealth.kafka.message;

public class StudentProfileRequestMessage {

  private String requestId;
  private String studentId;

  public StudentProfileRequestMessage() {
  }

  public StudentProfileRequestMessage(String requestId, String studentId) {
    this.requestId = requestId;
    this.studentId = studentId;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }
}