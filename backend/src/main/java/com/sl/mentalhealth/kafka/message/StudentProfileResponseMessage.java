package com.sl.mentalhealth.kafka.message;

public class StudentProfileResponseMessage {

  private String requestId;
  private Boolean success;
  private String message;
  private String studentId;
  private String name;
  private String className;
  private String college;
  private String phone;

  public StudentProfileResponseMessage() {
  }

  public StudentProfileResponseMessage(String requestId, Boolean success, String message,
      String studentId, String name, String className, String college, String phone) {
    this.requestId = requestId;
    this.success = success;
    this.message = message;
    this.studentId = studentId;
    this.name = name;
    this.className = className;
    this.college = college;
    this.phone = phone;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getCollege() {
    return college;
  }

  public void setCollege(String college) {
    this.college = college;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }
}