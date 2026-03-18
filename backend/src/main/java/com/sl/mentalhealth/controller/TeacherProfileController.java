package com.sl.mentalhealth.controller;

import com.sl.mentalhealth.common.Result;
import com.sl.mentalhealth.dto.TeacherProfileRequest;
import com.sl.mentalhealth.kafka.message.TeacherProfileResponseMessage;
import com.sl.mentalhealth.service.TeacherProfileGatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
@CrossOrigin
public class TeacherProfileController {

  private final TeacherProfileGatewayService teacherProfileGatewayService;

  @PostMapping("/profile")
  public Result<?> getTeacherProfile(@RequestBody TeacherProfileRequest request) {
    if (request == null || request.getTeacherAccount() == null || request.getTeacherAccount().trim().isEmpty()) {
      return Result.badRequest("老师账号不能为空");
    }

    TeacherProfileResponseMessage response =
        teacherProfileGatewayService.getTeacherProfile(request.getTeacherAccount());

    if (response.isSuccess()) {
      return Result.success(response.getMessage(), response.getData());
    }
    return Result.error(response.getMessage());
  }
}