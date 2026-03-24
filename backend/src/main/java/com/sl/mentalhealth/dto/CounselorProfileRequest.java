package com.sl.mentalhealth.dto;

public class CounselorProfileRequest {

  private String account;

  public CounselorProfileRequest() {
  }

  public CounselorProfileRequest(String account) {
    this.account = account;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }
}