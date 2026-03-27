package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.Student;
import com.sl.mentalhealth.entity.StudentAssessmentSemesterSummary;
import com.sl.mentalhealth.repository.projection.ClassDangerCountProjection;
import com.sl.mentalhealth.repository.projection.SemesterDangerCountProjection;
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

  /**
   * 柱状图：某辅导员在某学期下，各班危险人数
   * 即使某个班危险人数为 0，也会返回该班
   */
  @Query(value = """
      SELECT
          ccm.class_name AS className,
          COUNT(DISTINCT sass.student_id) AS dangerCount
      FROM counselor_class_mapping ccm
      LEFT JOIN student s
             ON s.class_name = ccm.class_name
      LEFT JOIN student_assessment_semester_summary sass
             ON sass.student_id = s.student_id
            AND sass.semester = :semester
            AND sass.semester_level = '危险'
      WHERE ccm.counselor_account = :counselorAccount
      GROUP BY ccm.class_name
      ORDER BY ccm.class_name
      """, nativeQuery = true)
  List<ClassDangerCountProjection> findDangerCountByClass(
      @Param("counselorAccount") String counselorAccount,
      @Param("semester") String semester
  );

  /**
   * 折线图：8 个学期每学期危险总人数
   * 固定返回第1学期~第8学期，没有数据的学期返回 0
   */
  @Query(value = """
      SELECT
          sem.semester AS semester,
          COUNT(DISTINCT CASE
              WHEN ccm.counselor_account IS NOT NULL THEN sass.student_id
          END) AS dangerCount
      FROM (
          SELECT '第1学期' AS semester
          UNION ALL SELECT '第2学期'
          UNION ALL SELECT '第3学期'
          UNION ALL SELECT '第4学期'
          UNION ALL SELECT '第5学期'
          UNION ALL SELECT '第6学期'
          UNION ALL SELECT '第7学期'
          UNION ALL SELECT '第8学期'
      ) sem
      LEFT JOIN student_assessment_semester_summary sass
             ON sass.semester = sem.semester
            AND sass.semester_level = '危险'
      LEFT JOIN student s
             ON s.student_id = sass.student_id
      LEFT JOIN counselor_class_mapping ccm
             ON ccm.class_name = s.class_name
            AND ccm.counselor_account = :counselorAccount
      GROUP BY sem.semester
      ORDER BY CAST(REPLACE(REPLACE(sem.semester, '第', ''), '学期', '') AS UNSIGNED)
      """, nativeQuery = true)
  List<SemesterDangerCountProjection> findDangerCountBySemester(
      @Param("counselorAccount") String counselorAccount
  );
}