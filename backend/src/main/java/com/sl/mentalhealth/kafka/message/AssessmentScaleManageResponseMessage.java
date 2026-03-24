package com.sl.mentalhealth.kafka.message;

public class AssessmentScaleManageResponseMessage {

  private String requestId;
  private Boolean success;
  private String message;
  private Object data;

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public static AssessmentScaleManageResponseMessage ok(String requestId, String message, Object data) {
    AssessmentScaleManageResponseMessage response = new AssessmentScaleManageResponseMessage();
    response.setRequestId(requestId);
    response.setSuccess(true);
    response.setMessage(message);
    response.setData(data);
    return response;
  }

  public static AssessmentScaleManageResponseMessage fail(String requestId, String message) {
    AssessmentScaleManageResponseMessage response = new AssessmentScaleManageResponseMessage();
    response.setRequestId(requestId);
    response.setSuccess(false);
    response.setMessage(message);
    response.setData(null);
    return response;
  }
}