package com.sl.mentalhealth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "counselor")
public class Counselor {

  @Id
  @Column(name = "account")
  private String account;

  @Column(name = "name")
  private String name;

  @Column(name = "password")
  private String password;

  @Column(name = "college")
  private String college;

  @Column(name = "grade")
  private String grade;

  @Column(name = "phone")
  private String phone;

  public Counselor() {
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getCollege() {
    return college;
  }

  public void setCollege(String college) {
    this.college = college;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }
}