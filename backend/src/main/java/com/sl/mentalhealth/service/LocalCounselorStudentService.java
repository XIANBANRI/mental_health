package com.sl.mentalhealth.service;

import com.sl.mentalhealth.entity.CounselorClassMapping;
import com.sl.mentalhealth.entity.Student;
import com.sl.mentalhealth.entity.StudentAssessmentSemesterSummary;
import com.sl.mentalhealth.repository.CounselorClassMappingRepository;
import com.sl.mentalhealth.repository.StudentAssessmentSemesterSummaryRepository;
import com.sl.mentalhealth.repository.StudentRepository;
import com.sl.mentalhealth.vo.CounselorStudentAssessmentSummaryVO;
import com.sl.mentalhealth.vo.CounselorStudentDetailVO;
import com.sl.mentalhealth.vo.CounselorStudentPageVO;
import com.sl.mentalhealth.vo.CounselorStudentVO;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocalCounselorStudentService {

  private final CounselorClassMappingRepository counselorClassMappingRepository;
  private final StudentRepository studentRepository;
  private final StudentAssessmentSemesterSummaryRepository studentAssessmentSemesterSummaryRepository;

  public List<String> listManagedClasses(String counselorAccount) {
    validateCounselorAccount(counselorAccount);
    return getManagedClasses(counselorAccount);
  }

  public CounselorStudentPageVO listStudents(String counselorAccount, String className, String keyword,
      Integer pageNum, Integer pageSize) {
    validateCounselorAccount(counselorAccount);

    List<String> managedClasses = getManagedClasses(counselorAccount);
    if (managedClasses.isEmpty()) {
      return CounselorStudentPageVO.builder()
          .list(Collections.emptyList())
          .total(0L)
          .build();
    }

    List<String> targetClasses = managedClasses;
    if (StringUtils.hasText(className) && !"全部".equals(className.trim())) {
      String selectedClassName = className.trim();
      if (!managedClasses.contains(selectedClassName)) {
        throw new IllegalArgumentException("无权查看该班级学生");
      }
      targetClasses = Collections.singletonList(selectedClassName);
    }

    int safePageNum = (pageNum == null || pageNum < 1) ? 1 : pageNum;
    int safePageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;

    PageRequest pageRequest = PageRequest.of(
        safePageNum - 1,
        safePageSize,
        Sort.by(Sort.Direction.ASC, "studentId")
    );

    Page<Student> studentPage = studentRepository.searchByClassNamesAndKeyword(
        targetClasses,
        StringUtils.hasText(keyword) ? keyword.trim() : null,
        pageRequest
    );

    List<CounselorStudentVO> list = studentPage.getContent()
        .stream()
        .map(this::toStudentVO)
        .collect(Collectors.toList());

    return CounselorStudentPageVO.builder()
        .list(list)
        .total(studentPage.getTotalElements())
        .build();
  }

  public CounselorStudentDetailVO getStudentDetail(String counselorAccount, String studentId) {
    validateCounselorAccount(counselorAccount);

    if (!StringUtils.hasText(studentId)) {
      throw new IllegalArgumentException("学生学号不能为空");
    }

    List<String> managedClasses = getManagedClasses(counselorAccount);
    if (managedClasses.isEmpty()) {
      throw new IllegalArgumentException("当前辅导员未绑定任何班级");
    }

    Student student = studentRepository.findAccessibleStudent(studentId.trim(), managedClasses)
        .orElseThrow(() -> new IllegalArgumentException("无权查看该学生信息，或学生不存在"));

    List<CounselorStudentAssessmentSummaryVO> summaries =
        studentAssessmentSemesterSummaryRepository
            .findByStudentIdOrderByLastTestedAtDescIdDesc(student.getStudentId())
            .stream()
            .map(this::toAssessmentSummaryVO)
            .collect(Collectors.toList());

    return CounselorStudentDetailVO.builder()
        .studentId(student.getStudentId())
        .name(student.getName())
        .college(student.getCollege())
        .className(student.getClassName())
        .grade(student.getGrade())
        .phone(student.getPhone())
        .assessmentSummaries(summaries)
        .build();
  }

  private List<String> getManagedClasses(String counselorAccount) {
    return counselorClassMappingRepository.findByCounselorAccountOrderByClassNameAsc(counselorAccount)
        .stream()
        .map(CounselorClassMapping::getClassName)
        .filter(StringUtils::hasText)
        .distinct()
        .collect(Collectors.toList());
  }

  private CounselorStudentVO toStudentVO(Student student) {
    return CounselorStudentVO.builder()
        .studentId(student.getStudentId())
        .name(student.getName())
        .college(student.getCollege())
        .className(student.getClassName())
        .grade(student.getGrade())
        .phone(student.getPhone())
        .build();
  }

  private CounselorStudentAssessmentSummaryVO toAssessmentSummaryVO(
      StudentAssessmentSemesterSummary summary) {
    return CounselorStudentAssessmentSummaryVO.builder()
        .id(summary.getId())
        .studentId(summary.getStudentId())
        .semester(summary.getSemester())
        .testedCount(summary.getTestedCount())
        .scoreSummary(summary.getScoreSummary())
        .semesterLevel(summary.getSemesterLevel())
        .lastTestedAt(summary.getLastTestedAt())
        .createdAt(summary.getCreatedAt())
        .updatedAt(summary.getUpdatedAt())
        .build();
  }

  private void validateCounselorAccount(String counselorAccount) {
    if (!StringUtils.hasText(counselorAccount)) {
      throw new IllegalArgumentException("辅导员账号不能为空");
    }
  }
}