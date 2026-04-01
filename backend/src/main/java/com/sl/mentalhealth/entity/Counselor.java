package com.sl.mentalhealth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
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

  @Column(name = "avatar_url")
  private String avatarUrl;

  public Counselor() {
  }

}