package com.sl.mentalhealth.service;

import com.sl.mentalhealth.entity.Counselor;
import com.sl.mentalhealth.entity.Student;
import com.sl.mentalhealth.kafka.message.StudentProfileRequestMessage;
import com.sl.mentalhealth.kafka.message.StudentProfileResponseMessage;
import com.sl.mentalhealth.repository.CounselorRepository;
import com.sl.mentalhealth.repository.StudentRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class LocalStudentProfileService {

  private final StudentRepository studentRepository;
  private final CounselorRepository counselorRepository;

  public LocalStudentProfileService(StudentRepository studentRepository,
      CounselorRepository counselorRepository) {
    this.studentRepository = studentRepository;
    this.counselorRepository = counselorRepository;
  }

  public StudentProfileResponseMessage queryProfile(StudentProfileRequestMessage request) {
    String requestId = request.getRequestId();
    String studentId = request.getStudentId();

    if (studentId == null || studentId.trim().isEmpty()) {
      return new StudentProfileResponseMessage(
          requestId, false, "学号不能为空",
          null, null, null, null, null, null, null, null
      );
    }

    Optional<Student> optional = studentRepository.findById(studentId);

    if (optional.isEmpty()) {
      return new StudentProfileResponseMessage(
          requestId, false, "学生不存在",
          studentId, null, null, null, null, null, null, null
      );
    }

    Student student = optional.get();

    String counselorName = null;
    String counselorPhone = null;

    if (student.getCollege() != null && student.getGrade() != null) {
      Optional<Counselor> counselorOptional =
          counselorRepository.findFirstByCollegeAndGrade(student.getCollege(), student.getGrade());

      if (counselorOptional.isPresent()) {
        Counselor counselor = counselorOptional.get();
        counselorName = counselor.getName();
        counselorPhone = counselor.getPhone();
      }
    }

    return new StudentProfileResponseMessage(
        requestId,
        true,
        "查询成功",
        student.getStudentId(),
        student.getName(),
        student.getClassName(),
        student.getCollege(),
        student.getGrade(),
        student.getPhone(),
        counselorName,
        counselorPhone
    );
  }
}