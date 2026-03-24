package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.AssessmentRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRecordRepository extends JpaRepository<AssessmentRecord, Long> {

  /**
   * 按学生学号 + 学期查询测评记录
   */
  Optional<AssessmentRecord> findByStudentIdAndSemester(String studentId, String semester);

  /**
   * 按学生学号查询测评记录，按提交时间倒序、ID倒序
   * 兼容你原有代码
   */
  List<AssessmentRecord> findByStudentIdOrderBySubmittedAtDescIdDesc(String studentId);

  /**
   * 按学生学号查询测评记录，按提交时间倒序
   * 兼容老师端预约管理里“查看学生心理测试记录”的代码
   */
  List<AssessmentRecord> findByStudentIdOrderBySubmittedAtDesc(String studentId);
}