package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, String> {
}