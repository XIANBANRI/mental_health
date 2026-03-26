package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.StudentAssessmentRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentAssessmentRecordRepository extends JpaRepository<StudentAssessmentRecord, Long> {

  List<StudentAssessmentRecord> findByStudentIdOrderBySubmittedAtDescIdDesc(String studentId);

  List<StudentAssessmentRecord> findByStudentIdAndSemesterOrderBySubmittedAtDescIdDesc(
      String studentId, String semester);

  List<StudentAssessmentRecord> findByStudentIdAndSemesterOrderBySubmittedAtAscIdAsc(
      String studentId, String semester);

  Optional<StudentAssessmentRecord> findFirstByStudentIdAndSemesterAndScaleId(
      String studentId, String semester, Long scaleId);
}