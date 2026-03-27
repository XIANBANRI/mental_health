package com.sl.mentalhealth.repository;

import com.sl.mentalhealth.entity.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, String> {

  Optional<Student> findByStudentId(String studentId);

  @Query("""
      select s from Student s
      where s.className in :classNames
        and (
            :keyword is null or :keyword = ''
            or s.studentId like concat('%', :keyword, '%')
            or s.name like concat('%', :keyword, '%')
        )
      """)
  Page<Student> searchByClassNamesAndKeyword(@Param("classNames") List<String> classNames,
      @Param("keyword") String keyword,
      Pageable pageable);

  @Query("""
      select s from Student s
      where s.studentId = :studentId
        and s.className in :classNames
      """)
  Optional<Student> findAccessibleStudent(@Param("studentId") String studentId,
      @Param("classNames") List<String> classNames);
}