package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.StudentAssessmentSemesterSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentAssessmentSemesterSummaryRepository
    extends JpaRepository<StudentAssessmentSemesterSummary, Long> {

  Optional<StudentAssessmentSemesterSummary> findByStudentIdAndSemester(String studentId, String semester);

  List<StudentAssessmentSemesterSummary> findByStudentIdOrderByLastTestedAtDescIdDesc(String studentId);
}