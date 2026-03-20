package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.Student;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, String> {

  Optional<Student> findByStudentId(String studentId);
}