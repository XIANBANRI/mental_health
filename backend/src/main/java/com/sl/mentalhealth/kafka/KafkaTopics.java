package com.sl.mentalhealth.kafka;

public final class KafkaTopics {

  private KafkaTopics() {
  }

  public static final String LOGIN_REQUEST = "mh.login.request";
  public static final String LOGIN_RESPONSE = "mh.login.response";

  public static final String RESET_PASSWORD_REQUEST = "mh.reset-password.request";
  public static final String RESET_PASSWORD_RESPONSE = "mh.reset-password.response";
}