package com.sl.mentalhealth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "admin")
public class Admin {

  @Setter
  @Getter
  @Id
  @Column(name = "account")
  private String account;

  @Setter
  @Getter
  @Column(name = "password")
  private String password;

  @Column(name = "name")
  private String name;

  public Admin() {
  }

}