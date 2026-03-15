package com.sl.mentalhealth.vo;

public class StudentProfileResponseVO {

  private String studentId;
  private String name;
  private String className;
  private String college;
  private String phone;

  public StudentProfileResponseVO() {
  }

  public StudentProfileResponseVO(String studentId, String name, String className,
      String college, String phone) {
    this.studentId = studentId;
    this.name = name;
    this.className = className;
    this.college = college;
    this.phone = phone;
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