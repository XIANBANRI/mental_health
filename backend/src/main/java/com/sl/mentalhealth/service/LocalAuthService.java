package com.sl.mentalhealth.service;

import com.sl.mentalhealth.entity.Admin;
import com.sl.mentalhealth.entity.Counselor;
import com.sl.mentalhealth.entity.Student;
import com.sl.mentalhealth.entity.Teacher;
import com.sl.mentalhealth.repository.AdminRepository;
import com.sl.mentalhealth.repository.CounselorRepository;
import com.sl.mentalhealth.repository.StudentRepository;
import com.sl.mentalhealth.repository.TeacherRepository;
import com.sl.mentalhealth.vo.LoginResponseVO;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class LocalAuthService {

  private final StudentRepository studentRepository;
  private final TeacherRepository teacherRepository;
  private final CounselorRepository counselorRepository;
  private final AdminRepository adminRepository;

  public LocalAuthService(StudentRepository studentRepository,
      TeacherRepository teacherRepository,
      CounselorRepository counselorRepository,
      AdminRepository adminRepository) {
    this.studentRepository = studentRepository;
    this.teacherRepository = teacherRepository;
    this.counselorRepository = counselorRepository;
    this.adminRepository = adminRepository;
  }

  public LoginResponseVO login(String role, String username, String password) {
    if (role == null || role.trim().isEmpty()
        || username == null || username.trim().isEmpty()
        || password == null || password.trim().isEmpty()) {
      throw new RuntimeException("请填写完整信息");
    }

    switch (role) {
      case "student":
        return loginStudent(username, password);

      case "teacher":
        return loginTeacher(username, password);

      case "counselor":
        return loginCounselor(username, password);

      case "admin":
        return loginAdmin(username, password);

      default:
        throw new RuntimeException("身份类型错误");
    }
  }

  private LoginResponseVO loginStudent(String username, String password) {
    Optional<Student> studentOptional = studentRepository.findById(username);

    if (studentOptional.isPresent()
        && Objects.equals(studentOptional.get().getPassword(), password)) {
      return new LoginResponseVO("student", username, "/student");
    }

    throw new RuntimeException("账号或密码错误");
  }

  private LoginResponseVO loginTeacher(String username, String password) {
    Optional<Teacher> teacherOptional = teacherRepository.findById(username);

    if (teacherOptional.isPresent()
        && Objects.equals(teacherOptional.get().getPassword(), password)) {
      return new LoginResponseVO("teacher", username, "/teacher");
    }

    throw new RuntimeException("账号或密码错误");
  }

  private LoginResponseVO loginCounselor(String username, String password) {
    Optional<Counselor> counselorOptional = counselorRepository.findById(username);

    if (counselorOptional.isPresent()
        && Objects.equals(counselorOptional.get().getPassword(), password)) {
      return new LoginResponseVO("counselor", username, "/counselor");
    }

    throw new RuntimeException("账号或密码错误");
  }

  private LoginResponseVO loginAdmin(String username, String password) {
    Optional<Admin> adminOptional = adminRepository.findById(username);

    if (adminOptional.isPresent()
        && Objects.equals(adminOptional.get().getPassword(), password)) {
      return new LoginResponseVO("admin", username, "/admin");
    }

    throw new RuntimeException("账号或密码错误");
  }
}