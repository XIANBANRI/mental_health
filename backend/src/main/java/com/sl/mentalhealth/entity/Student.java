package com.sl.mentalhealth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
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

  @Column(name = "grade")
  private String grade;

  @Column(name = "avatar_url")
  private String avatarUrl;

  public Student() {
  }

}