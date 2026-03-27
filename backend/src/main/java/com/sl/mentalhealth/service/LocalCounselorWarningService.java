package com.sl.mentalhealth.service;

import com.sl.mentalhealth.entity.CounselorClassMapping;
import com.sl.mentalhealth.entity.Student;
import com.sl.mentalhealth.entity.StudentAssessmentRecord;
import com.sl.mentalhealth.repository.CounselorClassMappingRepository;
import com.sl.mentalhealth.repository.StudentAssessmentRecordRepository;
import com.sl.mentalhealth.repository.StudentAssessmentSemesterSummaryRepository;
import com.sl.mentalhealth.repository.StudentRepository;
import com.sl.mentalhealth.vo.CounselorWarningDetailVO;
import com.sl.mentalhealth.vo.CounselorWarningPageVO;
import com.sl.mentalhealth.vo.CounselorWarningRecordVO;
import com.sl.mentalhealth.vo.CounselorWarningStudentVO;
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
public class LocalCounselorWarningService {

  private final CounselorClassMappingRepository counselorClassMappingRepository;
  private final StudentAssessmentSemesterSummaryRepository studentAssessmentSemesterSummaryRepository;
  private final StudentAssessmentRecordRepository studentAssessmentRecordRepository;
  private final StudentRepository studentRepository;

  public List<String> listManagedClasses(String counselorAccount) {
    validateCounselorAccount(counselorAccount);
    return getManagedClasses(counselorAccount);
  }

  public CounselorWarningPageVO listDangerousStudents(String counselorAccount,
      String semester,
      String className,
      Integer pageNum,
      Integer pageSize) {
    validateCounselorAccount(counselorAccount);

    String safeSemester = StringUtils.hasText(semester) ? semester.trim() : "第1学期";
    List<String> managedClasses = getManagedClasses(counselorAccount);
    if (managedClasses.isEmpty()) {
      return CounselorWarningPageVO.builder()
          .list(Collections.emptyList())
          .total(0L)
          .build();
    }

    List<String> targetClasses = managedClasses;
    if (StringUtils.hasText(className)) {
      String selectedClass = className.trim();
      if (!managedClasses.contains(selectedClass)) {
        throw new IllegalArgumentException("无权查看该班级预警信息");
      }
      targetClasses = Collections.singletonList(selectedClass);
    }

    int safePageNum = (pageNum == null || pageNum < 1) ? 1 : pageNum;
    int safePageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;

    PageRequest pageRequest = PageRequest.of(
        safePageNum - 1,
        safePageSize,
        Sort.by(Sort.Direction.ASC, "studentId")
    );

    Page<Student> page = studentAssessmentSemesterSummaryRepository
        .findDangerousStudentsBySemesterAndClassNames(safeSemester, targetClasses, pageRequest);

    List<CounselorWarningStudentVO> list = page.getContent()
        .stream()
        .map(this::toWarningStudentVO)
        .collect(Collectors.toList());

    return CounselorWarningPageVO.builder()
        .list(list)
        .total(page.getTotalElements())
        .build();
  }

  public CounselorWarningDetailVO getDangerousStudentDetail(String counselorAccount,
      String studentId,
      String semester) {
    validateCounselorAccount(counselorAccount);

    if (!StringUtils.hasText(studentId)) {
      throw new IllegalArgumentException("学生学号不能为空");
    }

    String safeSemester = StringUtils.hasText(semester) ? semester.trim() : "第1学期";
    List<String> managedClasses = getManagedClasses(counselorAccount);
    if (managedClasses.isEmpty()) {
      throw new IllegalArgumentException("当前辅导员未绑定任何班级");
    }

    Student student = studentRepository.findById(studentId.trim())
        .orElseThrow(() -> new IllegalArgumentException("学生不存在"));

    if (!managedClasses.contains(student.getClassName())) {
      throw new IllegalArgumentException("无权查看该学生预警详情");
    }

    boolean dangerous = studentAssessmentSemesterSummaryRepository
        .existsByStudentIdAndSemesterAndSemesterLevel(student.getStudentId(), safeSemester, "危险");

    if (!dangerous) {
      throw new IllegalArgumentException("该学生在当前学期不属于危险预警名单");
    }

    List<CounselorWarningRecordVO> records = studentAssessmentRecordRepository
        .findByStudentIdAndSemesterOrderBySubmittedAtDescIdDesc(student.getStudentId(), safeSemester)
        .stream()
        .map(this::toRecordVO)
        .collect(Collectors.toList());

    return CounselorWarningDetailVO.builder()
        .studentId(student.getStudentId())
        .name(student.getName())
        .className(student.getClassName())
        .phone(student.getPhone())
        .semester(safeSemester)
        .records(records)
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

  private CounselorWarningStudentVO toWarningStudentVO(Student student) {
    return CounselorWarningStudentVO.builder()
        .studentId(student.getStudentId())
        .name(student.getName())
        .className(student.getClassName())
        .phone(student.getPhone())
        .build();
  }

  private CounselorWarningRecordVO toRecordVO(StudentAssessmentRecord record) {
    return CounselorWarningRecordVO.builder()
        .id(record.getId())
        .scaleCode(record.getScaleCode())
        .scaleName(record.getScaleName())
        .rawScore(record.getRawScore())
        .resultLevel(record.getResultLevel())
        .resultSummary(record.getResultSummary())
        .suggestion(record.getSuggestion())
        .submittedAt(record.getSubmittedAt())
        .build();
  }

  private void validateCounselorAccount(String counselorAccount) {
    if (!StringUtils.hasText(counselorAccount)) {
      throw new IllegalArgumentException("辅导员账号不能为空");
    }
  }
}