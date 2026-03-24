package com.sl.mentalhealth.controller;

import com.sl.mentalhealth.dto.StudentProfileRequest;
import com.sl.mentalhealth.service.StudentProfileGatewayService;
import com.sl.mentalhealth.vo.StudentProfileResponseVO;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentProfileController {

  private final StudentProfileGatewayService studentProfileGatewayService;

  public StudentProfileController(StudentProfileGatewayService studentProfileGatewayService) {
    this.studentProfileGatewayService = studentProfileGatewayService;
  }

  @PostMapping("/profile")
  public ResponseEntity<Map<String, Object>> queryProfile(
      @RequestBody StudentProfileRequest request) {

    Map<String, Object> result = new HashMap<>();

    try {
      StudentProfileResponseVO data = studentProfileGatewayService.queryProfile(request);
      result.put("success", true);
      result.put("message", "查询成功");
      result.put("data", data);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      result.put("success", false);
      result.put("message", e.getMessage() == null ? "查询失败" : e.getMessage());
      return ResponseEntity.badRequest().body(result);
    }
  }
}