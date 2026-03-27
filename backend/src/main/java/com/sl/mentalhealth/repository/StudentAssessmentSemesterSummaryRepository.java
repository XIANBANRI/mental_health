package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.Student;
import com.sl.mentalhealth.entity.StudentAssessmentSemesterSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentAssessmentSemesterSummaryRepository
    extends JpaRepository<StudentAssessmentSemesterSummary, Long> {

  Optional<StudentAssessmentSemesterSummary> findByStudentIdAndSemester(String studentId, String semester);

  List<StudentAssessmentSemesterSummary> findByStudentIdOrderByLastTestedAtDescIdDesc(String studentId);

  /**
   * 判断某学生在某学期是否为危险
   */
  boolean existsByStudentIdAndSemesterAndSemesterLevel(String studentId,
      String semester,
      String semesterLevel);

  /**
   * 查询某学期、某些班级中的危险学生分页列表
   */
  @Query(value = """
      select s
      from StudentAssessmentSemesterSummary ss, Student s
      where ss.studentId = s.studentId
        and ss.semester = :semester
        and ss.semesterLevel = '危险'
        and s.className in :classNames
      order by ss.lastTestedAt desc, s.studentId asc
      """,
      countQuery = """
      select count(s.studentId)
      from StudentAssessmentSemesterSummary ss, Student s
      where ss.studentId = s.studentId
        and ss.semester = :semester
        and ss.semesterLevel = '危险'
        and s.className in :classNames
      """)
  Page<Student> findDangerousStudentsBySemesterAndClassNames(@Param("semester") String semester,
      @Param("classNames") List<String> classNames,
      Pageable pageable);
}