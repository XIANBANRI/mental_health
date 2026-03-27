package com.sl.mentalhealth.controller;

import com.sl.mentalhealth.common.Result;
import com.sl.mentalhealth.dto.CounselorWarningQueryRequest;
import com.sl.mentalhealth.service.CounselorWarningGatewayService;
import com.sl.mentalhealth.vo.CounselorWarningDetailVO;
import com.sl.mentalhealth.vo.CounselorWarningPageVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/counselor/warning")
@RequiredArgsConstructor
public class CounselorWarningController {

  private final CounselorWarningGatewayService counselorWarningGatewayService;

  @GetMapping("/classes")
  public Result<List<String>> classes(@RequestParam String counselorAccount) {
    try {
      List<String> result = counselorWarningGatewayService.listManagedClasses(counselorAccount);
      return Result.success("查询班级列表成功", result);
    } catch (Exception e) {
      return Result.error("查询班级列表失败：" + e.getMessage());
    }
  }

  @PostMapping("/list")
  public Result<CounselorWarningPageVO> list(@RequestBody CounselorWarningQueryRequest request) {
    try {
      CounselorWarningPageVO result = counselorWarningGatewayService.listDangerousStudents(request);
      return Result.success("查询预警名单成功", result);
    } catch (Exception e) {
      return Result.error("查询预警名单失败：" + e.getMessage());
    }
  }

  @GetMapping("/detail")
  public Result<CounselorWarningDetailVO> detail(@RequestParam String counselorAccount,
      @RequestParam String studentId,
      @RequestParam String semester) {
    try {
      CounselorWarningDetailVO result = counselorWarningGatewayService
          .getDangerousStudentDetail(counselorAccount, studentId, semester);
      return Result.success("查询预警详情成功", result);
    } catch (Exception e) {
      return Result.error("查询预警详情失败：" + e.getMessage());
    }
  }
}