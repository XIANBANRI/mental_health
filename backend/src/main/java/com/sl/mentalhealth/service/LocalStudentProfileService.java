package com.sl.mentalhealth.service;

import com.sl.mentalhealth.entity.Counselor;
import com.sl.mentalhealth.entity.CounselorClassMapping;
import com.sl.mentalhealth.entity.Student;
import com.sl.mentalhealth.kafka.message.StudentProfileRequestMessage;
import com.sl.mentalhealth.kafka.message.StudentProfileResponseMessage;
import com.sl.mentalhealth.repository.CounselorClassMappingRepository;
import com.sl.mentalhealth.repository.CounselorRepository;
import com.sl.mentalhealth.repository.StudentRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class LocalStudentProfileService {

  private final StudentRepository studentRepository;
  private final CounselorRepository counselorRepository;
  private final CounselorClassMappingRepository counselorClassMappingRepository;

  public LocalStudentProfileService(StudentRepository studentRepository,
      CounselorRepository counselorRepository,
      CounselorClassMappingRepository counselorClassMappingRepository) {
    this.studentRepository = studentRepository;
    this.counselorRepository = counselorRepository;
    this.counselorClassMappingRepository = counselorClassMappingRepository;
  }

  public StudentProfileResponseMessage handle(StudentProfileRequestMessage request) {
    if (StudentProfileRequestMessage.ACTION_UPDATE_AVATAR.equals(request.getAction())) {
      return updateAvatar(request);
    }
    return queryProfile(request);
  }

  public StudentProfileResponseMessage queryProfile(StudentProfileRequestMessage request) {
    String requestId = request.getRequestId();
    String studentId = request.getStudentId();

    if (studentId == null || studentId.trim().isEmpty()) {
      return new StudentProfileResponseMessage(
          requestId, false, "学号不能为空",
          null, null, null, null, null, null, null, null, null
      );
    }

    Optional<Student> optional = studentRepository.findById(studentId.trim());

    if (optional.isEmpty()) {
      return new StudentProfileResponseMessage(
          requestId, false, "学生不存在",
          studentId, null, null, null, null, null, null, null, null
      );
    }

    Student student = optional.get();
    CounselorContact counselorContact = resolveCounselorContact(student);

    return buildSuccess(requestId, "查询成功", student, counselorContact);
  }

  public StudentProfileResponseMessage updateAvatar(StudentProfileRequestMessage request) {
    String requestId = request.getRequestId();
    String studentId = request.getStudentId();
    String avatarUrl = request.getAvatarUrl();

    if (studentId == null || studentId.trim().isEmpty()) {
      return new StudentProfileResponseMessage(
          requestId, false, "学号不能为空",
          null, null, null, null, null, null, null, null, null
      );
    }

    if (avatarUrl == null || avatarUrl.trim().isEmpty()) {
      return new StudentProfileResponseMessage(
          requestId, false, "头像地址不能为空",
          studentId, null, null, null, null, null, null, null, null
      );
    }

    Optional<Student> optional = studentRepository.findById(studentId.trim());

    if (optional.isEmpty()) {
      return new StudentProfileResponseMessage(
          requestId, false, "学生不存在",
          studentId, null, null, null, null, null, null, null, null
      );
    }

    Student student = optional.get();
    student.setAvatarUrl(avatarUrl.trim());
    studentRepository.save(student);

    CounselorContact counselorContact = resolveCounselorContact(student);
    return buildSuccess(requestId, "头像上传成功", student, counselorContact);
  }

  private StudentProfileResponseMessage buildSuccess(String requestId, String message,
      Student student, CounselorContact counselorContact) {
    return new StudentProfileResponseMessage(
        requestId,
        true,
        message,
        student.getStudentId(),
        student.getName(),
        student.getClassName(),
        student.getCollege(),
        student.getGrade(),
        student.getPhone(),
        student.getAvatarUrl(),
        counselorContact.name(),
        counselorContact.phone()
    );
  }

  private CounselorContact resolveCounselorContact(Student student) {
    String counselorName = null;
    String counselorPhone = null;

    if (student.getClassName() != null && !student.getClassName().trim().isEmpty()) {
      Optional<CounselorClassMapping> mappingOptional =
          counselorClassMappingRepository.findFirstByClassName(student.getClassName());

      if (mappingOptional.isPresent()) {
        CounselorClassMapping mapping = mappingOptional.get();

        Optional<Counselor> counselorOptional =
            counselorRepository.findById(mapping.getCounselorAccount());

        if (counselorOptional.isPresent()) {
          Counselor counselor = counselorOptional.get();

          if (student.getGrade() != null && student.getGrade().equals(counselor.getGrade())) {
            counselorName = counselor.getName();
            counselorPhone = counselor.getPhone();
          }
        }
      }
    }

    return new CounselorContact(counselorName, counselorPhone);
  }

  private record CounselorContact(String name, String phone) {
  }
}