package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, String> {
}