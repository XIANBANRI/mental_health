package com.sl.mentalhealth.service;

import com.sl.mentalhealth.kafka.CounselorProfileRequestProducer;
import com.sl.mentalhealth.kafka.message.CounselorProfileRequestMessage;
import com.sl.mentalhealth.vo.CounselorProfileResponseVO;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class CounselorProfileGatewayService {

  private final CounselorProfileRequestProducer counselorProfileRequestProducer;
  private final PendingCounselorProfileService pendingCounselorProfileService;

  public CounselorProfileGatewayService(
      CounselorProfileRequestProducer counselorProfileRequestProducer,
      PendingCounselorProfileService pendingCounselorProfileService) {
    this.counselorProfileRequestProducer = counselorProfileRequestProducer;
    this.pendingCounselorProfileService = pendingCounselorProfileService;
  }

  public CounselorProfileResponseVO getProfile(String account) {
    String correlationId = UUID.randomUUID().toString();

    CompletableFuture<CounselorProfileResponseVO> future =
        pendingCounselorProfileService.create(correlationId);

    CounselorProfileRequestMessage message =
        new CounselorProfileRequestMessage(correlationId, account);

    counselorProfileRequestProducer.send(message);

    try {
      return future.get(10, TimeUnit.SECONDS);
    } catch (Exception e) {
      pendingCounselorProfileService.remove(correlationId);
      throw new RuntimeException("获取辅导员个人信息超时或失败：" + e.getMessage(), e);
    }
  }
}