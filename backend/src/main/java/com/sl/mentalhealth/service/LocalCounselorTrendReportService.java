package com.sl.mentalhealth.service;

import com.sl.mentalhealth.dto.CounselorTrendReportQueryRequest;
import com.sl.mentalhealth.repository.StudentAssessmentSemesterSummaryRepository;
import com.sl.mentalhealth.repository.projection.ClassDangerCountProjection;
import com.sl.mentalhealth.repository.projection.SemesterDangerCountProjection;
import com.sl.mentalhealth.vo.CounselorTrendBarVO;
import com.sl.mentalhealth.vo.CounselorTrendLineVO;
import com.sl.mentalhealth.vo.CounselorTrendReportVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalCounselorTrendReportService {

  private final StudentAssessmentSemesterSummaryRepository studentAssessmentSemesterSummaryRepository;

  public CounselorTrendReportVO queryTrendReport(CounselorTrendReportQueryRequest request) {
    String counselorAccount = request.getCounselorAccount();
    String selectedSemester = StringUtils.hasText(request.getSemester()) ? request.getSemester() : "第1学期";

    List<ClassDangerCountProjection> classRaw =
        studentAssessmentSemesterSummaryRepository.findDangerCountByClass(counselorAccount, selectedSemester);

    List<SemesterDangerCountProjection> semesterRaw =
        studentAssessmentSemesterSummaryRepository.findDangerCountBySemester(counselorAccount);

    List<CounselorTrendBarVO> barChart = new ArrayList<>();
    for (ClassDangerCountProjection item : classRaw) {
      barChart.add(new CounselorTrendBarVO(
          item.getClassName(),
          item.getDangerCount() == null ? 0L : item.getDangerCount()
      ));
    }

    List<CounselorTrendLineVO> lineChart = new ArrayList<>();
    for (SemesterDangerCountProjection item : semesterRaw) {
      lineChart.add(new CounselorTrendLineVO(
          item.getSemester(),
          item.getDangerCount() == null ? 0L : item.getDangerCount()
      ));
    }

    CounselorTrendReportVO vo = new CounselorTrendReportVO();
    vo.setSemesterOptions(buildSemesterOptions());
    vo.setSelectedSemester(selectedSemester);
    vo.setBarChart(barChart);
    vo.setLineChart(lineChart);
    return vo;
  }

  private List<String> buildSemesterOptions() {
    List<String> list = new ArrayList<>();
    for (int i = 1; i <= 8; i++) {
      list.add("第" + i + "学期");
    }
    return list;
  }
}