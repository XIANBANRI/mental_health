package com.sl.mentalhealth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "counselor_class_mapping")
public class CounselorClassMapping {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "counselor_account")
  private String counselorAccount;

  @Column(name = "class_name")
  private String className;

  public CounselorClassMapping() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCounselorAccount() {
    return counselorAccount;
  }

  public void setCounselorAccount(String counselorAccount) {
    this.counselorAccount = counselorAccount;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }
}