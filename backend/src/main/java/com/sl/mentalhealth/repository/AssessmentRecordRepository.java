package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.AssessmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssessmentRecordRepository extends JpaRepository<AssessmentRecord, Long> {

  Optional<AssessmentRecord> findByStudentIdAndSemester(String studentId, String semester);

  List<AssessmentRecord> findByStudentIdOrderBySubmittedAtDescIdDesc(String studentId);
}
