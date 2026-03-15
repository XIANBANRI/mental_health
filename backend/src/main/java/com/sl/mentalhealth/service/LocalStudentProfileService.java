package com.sl.mentalhealth.service;

import com.sl.mentalhealth.entity.Student;
import com.sl.mentalhealth.kafka.message.StudentProfileRequestMessage;
import com.sl.mentalhealth.kafka.message.StudentProfileResponseMessage;
import com.sl.mentalhealth.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocalStudentProfileService {

  private final StudentRepository studentRepository;

  public LocalStudentProfileService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  public StudentProfileResponseMessage queryProfile(StudentProfileRequestMessage request) {
    String requestId = request.getRequestId();
    String studentId = request.getStudentId();

    if (studentId == null || studentId.trim().isEmpty()) {
      return new StudentProfileResponseMessage(
          requestId, false, "学号不能为空", null, null, null, null, null
      );
    }

    Optional<Student> optional = studentRepository.findById(studentId);

    if (optional.isEmpty()) {
      return new StudentProfileResponseMessage(
          requestId, false, "学生不存在", studentId, null, null, null, null
      );
    }

    Student student = optional.get();

    return new StudentProfileResponseMessage(
        requestId,
        true,
        "查询成功",
        student.getStudentId(),
        student.getName(),
        student.getClassName(),
        student.getCollege(),
        student.getPhone()
    );
  }
}