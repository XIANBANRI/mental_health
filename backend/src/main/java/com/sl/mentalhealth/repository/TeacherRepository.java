package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.Teacher;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, String> {

  Optional<Teacher> findByAccount(String account);
}