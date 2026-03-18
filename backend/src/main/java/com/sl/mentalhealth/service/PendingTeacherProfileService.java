package com.sl.mentalhealth.service;

import com.sl.mentalhealth.kafka.message.TeacherProfileResponseMessage;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.springframework.stereotype.Service;

@Service
public class PendingTeacherProfileService {

  private final Map<String, CompletableFuture<TeacherProfileResponseMessage>> pendingMap =
      new ConcurrentHashMap<>();

  public void put(String requestId, CompletableFuture<TeacherProfileResponseMessage> future) {
    pendingMap.put(requestId, future);
  }

  public void complete(String requestId, TeacherProfileResponseMessage response) {
    CompletableFuture<TeacherProfileResponseMessage> future = pendingMap.remove(requestId);
    if (future != null) {
      future.complete(response);
    }
  }

  public TeacherProfileResponseMessage waitResponse(String requestId, long timeoutSeconds) {
    CompletableFuture<TeacherProfileResponseMessage> future = new CompletableFuture<>();
    put(requestId, future);

    try {
      return future.get(timeoutSeconds, TimeUnit.SECONDS);
    } catch (TimeoutException e) {
      pendingMap.remove(requestId);
      TeacherProfileResponseMessage response = new TeacherProfileResponseMessage();
      response.setRequestId(requestId);
      response.setSuccess(false);
      response.setMessage("老师信息查询超时");
      return response;
    } catch (Exception e) {
      pendingMap.remove(requestId);
      TeacherProfileResponseMessage response = new TeacherProfileResponseMessage();
      response.setRequestId(requestId);
      response.setSuccess(false);
      response.setMessage("老师信息查询失败");
      return response;
    }
  }
}