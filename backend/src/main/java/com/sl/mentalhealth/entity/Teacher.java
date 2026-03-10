package com.sl.mentalhealth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "teacher")
public class Teacher {

  @Id
  @Column(name = "account")
  private String account;

  @Column(name = "password")
  private String password;

  @Column(name = "office_location")
  private String officeLocation;

  @Column(name = "phone")
  private String phone;

  public Teacher() {
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getOfficeLocation() {
    return officeLocation;
  }

  public void setOfficeLocation(String officeLocation) {
    this.officeLocation = officeLocation;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }
}