package com.sl.mentalhealth.controller;

import com.sl.mentalhealth.common.Result;
import com.sl.mentalhealth.dto.CounselorProfileRequest;
import com.sl.mentalhealth.service.CounselorProfileGatewayService;
import com.sl.mentalhealth.vo.CounselorProfileResponseVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/counselor/profile")
@CrossOrigin
public class CounselorProfileController {

  private final CounselorProfileGatewayService counselorProfileGatewayService;

  public CounselorProfileController(CounselorProfileGatewayService counselorProfileGatewayService) {
    this.counselorProfileGatewayService = counselorProfileGatewayService;
  }

  @PostMapping("/get")
  public Result getProfile(@RequestBody CounselorProfileRequest request) {
    if (request == null || request.getAccount() == null || request.getAccount().trim().isEmpty()) {
      return Result.error("辅导员账号不能为空");
    }

    CounselorProfileResponseVO data =
        counselorProfileGatewayService.getProfile(request.getAccount().trim());

    return Result.success("查询成功", data);
  }
}