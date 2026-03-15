package com.sl.mentalhealth.service;

import com.sl.mentalhealth.dto.StudentProfileRequest;
import com.sl.mentalhealth.kafka.StudentProfileRequestProducer;
import com.sl.mentalhealth.kafka.message.StudentProfileRequestMessage;
import com.sl.mentalhealth.kafka.message.StudentProfileResponseMessage;
import com.sl.mentalhealth.vo.StudentProfileResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class StudentProfileGatewayService {

  private static final Logger log = LoggerFactory.getLogger(StudentProfileGatewayService.class);

  private final StudentProfileRequestProducer studentProfileRequestProducer;
  private final PendingStudentProfileService pendingStudentProfileService;

  public StudentProfileGatewayService(StudentProfileRequestProducer studentProfileRequestProducer,
      PendingStudentProfileService pendingStudentProfileService) {
    this.studentProfileRequestProducer = studentProfileRequestProducer;
    this.pendingStudentProfileService = pendingStudentProfileService;
  }

  public StudentProfileResponseVO queryProfile(StudentProfileRequest request) {
    String requestId = UUID.randomUUID().toString();

    log.info("开始处理学生信息查询请求, requestId={}, studentId={}",
        requestId, request.getStudentId());

    CompletableFuture<StudentProfileResponseMessage> future =
        pendingStudentProfileService.create(requestId);

    try {
      studentProfileRequestProducer.send(
          new StudentProfileRequestMessage(requestId, request.getStudentId())
      );

      StudentProfileResponseMessage response = future.get(15, TimeUnit.SECONDS);

      if (!Boolean.TRUE.equals(response.getSuccess())) {
        throw new RuntimeException(response.getMessage());
      }

      return new StudentProfileResponseVO(
          response.getStudentId(),
          response.getName(),
          response.getClassName(),
          response.getCollege(),
          response.getPhone()
      );
    } catch (Exception e) {
      log.error("学生信息查询异常, requestId={}, message={}", requestId, e.getMessage(), e);
      throw new RuntimeException(e.getMessage() == null ? "学生信息查询失败" : e.getMessage());
    } finally {
      pendingStudentProfileService.remove(requestId);
    }
  }
}