package com.sl.mentalhealth.service;

import com.sl.mentalhealth.kafka.TeacherProfileRequestProducer;
import com.sl.mentalhealth.kafka.message.TeacherProfileResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherProfileGatewayService {

  private final TeacherProfileRequestProducer teacherProfileRequestProducer;
  private final PendingTeacherProfileService pendingTeacherProfileService;

  public TeacherProfileResponseMessage getTeacherProfile(String teacherAccount) {
    String requestId = teacherProfileRequestProducer.send(teacherAccount);
    return pendingTeacherProfileService.waitResponse(requestId, 8);
  }
}