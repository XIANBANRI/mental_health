package com.sl.mentalhealth.kafka;

public final class KafkaTopics {

  private KafkaTopics() {
  }

  public static final String LOGIN_REQUEST = "mh.login.request";
  public static final String LOGIN_RESPONSE = "mh.login.response";

  public static final String RESET_PASSWORD_REQUEST = "mh.reset-password.request";
  public static final String RESET_PASSWORD_RESPONSE = "mh.reset-password.response";

  public static final String STUDENT_PROFILE_REQUEST = "mh.student.profile.request";
  public static final String STUDENT_PROFILE_RESPONSE = "mh.student.profile.response";

  public static final String ASSESSMENT_REQUEST = "mh.assessment.request";
  public static final String ASSESSMENT_RESPONSE = "mh.assessment.response";
}