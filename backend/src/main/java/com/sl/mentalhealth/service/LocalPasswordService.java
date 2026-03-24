package com.sl.mentalhealth.service;

import com.sl.mentalhealth.entity.Counselor;
import com.sl.mentalhealth.entity.Student;
import com.sl.mentalhealth.entity.Teacher;
import com.sl.mentalhealth.repository.CounselorRepository;
import com.sl.mentalhealth.repository.StudentRepository;
import com.sl.mentalhealth.repository.TeacherRepository;
import com.sl.mentalhealth.vo.ResetPasswordResponseVO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class LocalPasswordService {

  private final StudentRepository studentRepository;
  private final TeacherRepository teacherRepository;
  private final CounselorRepository counselorRepository;

  public LocalPasswordService(StudentRepository studentRepository,
      TeacherRepository teacherRepository,
      CounselorRepository counselorRepository) {
    this.studentRepository = studentRepository;
    this.teacherRepository = teacherRepository;
    this.counselorRepository = counselorRepository;
  }

  @Transactional
  public ResetPasswordResponseVO resetPassword(String role, String username,
      String phone, String newPassword) {

    if (role == null || role.trim().isEmpty()
        || username == null || username.trim().isEmpty()
        || phone == null || phone.trim().isEmpty()
        || newPassword == null || newPassword.trim().isEmpty()) {
      throw new RuntimeException("请填写完整信息");
    }

    switch (role) {
      case "student":
        return resetStudentPassword(username, phone, newPassword);

      case "teacher":
        return resetTeacherPassword(username, phone, newPassword);

      case "counselor":
        return resetCounselorPassword(username, phone, newPassword);

      default:
        throw new RuntimeException("身份类型错误");
    }
  }

  private ResetPasswordResponseVO resetStudentPassword(String username, String phone,
      String newPassword) {
    Optional<Student> optional = studentRepository.findById(username);

    if (optional.isEmpty()) {
      throw new RuntimeException("账号不存在");
    }

    Student student = optional.get();

    if (!Objects.equals(student.getPhone(), phone)) {
      throw new RuntimeException("手机号验证失败");
    }

    student.setPassword(newPassword);
    studentRepository.save(student);

    return new ResetPasswordResponseVO(true, "密码重置成功");
  }

  private ResetPasswordResponseVO resetTeacherPassword(String username, String phone,
      String newPassword) {
    Optional<Teacher> optional = teacherRepository.findById(username);

    if (optional.isEmpty()) {
      throw new RuntimeException("账号不存在");
    }

    Teacher teacher = optional.get();

    if (!Objects.equals(teacher.getPhone(), phone)) {
      throw new RuntimeException("手机号验证失败");
    }

    teacher.setPassword(newPassword);
    teacherRepository.save(teacher);

    return new ResetPasswordResponseVO(true, "密码重置成功");
  }

  private ResetPasswordResponseVO resetCounselorPassword(String username, String phone,
      String newPassword) {
    Optional<Counselor> optional = counselorRepository.findById(username);

    if (optional.isEmpty()) {
      throw new RuntimeException("账号不存在");
    }

    Counselor counselor = optional.get();

    if (!Objects.equals(counselor.getPhone(), phone)) {
      throw new RuntimeException("手机号验证失败");
    }

    counselor.setPassword(newPassword);
    counselorRepository.save(counselor);

    return new ResetPasswordResponseVO(true, "密码重置成功");
  }
}