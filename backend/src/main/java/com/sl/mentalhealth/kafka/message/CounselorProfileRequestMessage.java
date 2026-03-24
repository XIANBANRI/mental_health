package com.sl.mentalhealth.kafka.message;

import java.io.Serializable;

public class CounselorProfileRequestMessage implements Serializable {

  private String correlationId;
  private String account;

  public CounselorProfileRequestMessage() {
  }

  public CounselorProfileRequestMessage(String correlationId, String account) {
    this.correlationId = correlationId;
    this.account = account;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }
}