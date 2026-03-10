package com.sl.mentalhealth.dto;

public class ResetPasswordRequest {

  private String role;
  private String username;
  private String phone;
  private String password;

  public ResetPasswordRequest() {
  }

  public ResetPasswordRequest(String role, String username, String phone, String password) {
    this.role = role;
    this.username = username;
    this.phone = phone;
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}