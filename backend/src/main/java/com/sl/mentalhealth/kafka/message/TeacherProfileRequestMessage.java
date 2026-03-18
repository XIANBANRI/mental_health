package com.sl.mentalhealth.kafka.message;

import lombok.Data;

@Data
public class TeacherProfileRequestMessage {

  private String requestId;
  private String teacherAccount;
}