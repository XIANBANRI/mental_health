package com.sl.mentalhealth.kafka;

import com.sl.mentalhealth.kafka.message.TeacherProfileRequestMessage;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeacherProfileRequestProducer {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public String send(String teacherAccount) {
    String requestId = UUID.randomUUID().toString();

    TeacherProfileRequestMessage message = new TeacherProfileRequestMessage();
    message.setRequestId(requestId);
    message.setTeacherAccount(teacherAccount);

    kafkaTemplate.send(KafkaTopics.TEACHER_PROFILE_REQUEST, requestId, message);
    return requestId;
  }
}