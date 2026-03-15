package com.sl.mentalhealth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "student")
public class Student {

  @Id
  @Column(name = "student_id")
  private String studentId;

  @Column(name = "name")
  private String name;

  @Column(name = "college")
  private String college;

  @Column(name = "class_name")
  private String className;

  @Column(name = "password")
  private String password;

  @Column(name = "phone")
  private String phone;

  @Column(name = "score")
  private BigDecimal score;

  public Student() {
  }

  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public String getCollege() {
    return college;
  }

  public void setCollege(String college) {
    this.college = college;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public BigDecimal getScore() {
    return score;
  }

  public void setScore(BigDecimal score) {
    this.score = score;
  }

  public String getName(){ return name;}
}