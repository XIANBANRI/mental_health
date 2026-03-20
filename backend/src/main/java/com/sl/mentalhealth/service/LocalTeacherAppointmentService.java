package com.sl.mentalhealth.service;

import com.sl.mentalhealth.dto.TeacherAppointmentQueryRequest;
import com.sl.mentalhealth.dto.TeacherAppointmentUpdateStatusRequest;
import com.sl.mentalhealth.dto.TeacherAssessmentRecordQueryRequest;
import com.sl.mentalhealth.entity.Appointment;
import com.sl.mentalhealth.entity.AssessmentRecord;
import com.sl.mentalhealth.entity.Student;
import com.sl.mentalhealth.repository.AppointmentRepository;
import com.sl.mentalhealth.repository.AssessmentRecordRepository;
import com.sl.mentalhealth.repository.StudentRepository;
import com.sl.mentalhealth.repository.TeacherRepository;
import com.sl.mentalhealth.vo.TeacherAppointmentVO;
import com.sl.mentalhealth.vo.TeacherAssessmentRecordVO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class LocalTeacherAppointmentService {

  private final AppointmentRepository appointmentRepository;
  private final AssessmentRecordRepository assessmentRecordRepository;
  private final TeacherRepository teacherRepository;
  private final StudentRepository studentRepository;

  public List<TeacherAppointmentVO> query(TeacherAppointmentQueryRequest request) {
    validateTeacher(request.getTeacherAccount());

    String status = safe(request.getStatus());
    if ("COMPLETED".equals(status) || "CANCELLED".equals(status)) {
      throw new RuntimeException("预约查询不显示已完成或已取消记录，请到预约记录中查看");
    }

    LocalDate appointmentDate = parseDate(request.getAppointmentDate());

    List<Appointment> list = appointmentRepository.findTeacherAppointments(
        request.getTeacherAccount(),
        safe(request.getStudentId()),
        status,
        appointmentDate
    );

    return list.stream().map(this::toAppointmentVO).toList();
  }

  public List<TeacherAppointmentVO> record(TeacherAppointmentQueryRequest request) {
    validateTeacher(request.getTeacherAccount());

    LocalDate appointmentDate = parseDate(request.getAppointmentDate());

    List<Appointment> list = appointmentRepository.findTeacherAppointmentRecords(
        request.getTeacherAccount(),
        safe(request.getStudentId()),
        safe(request.getStatus()),
        appointmentDate
    );

    return list.stream().map(this::toAppointmentVO).toList();
  }

  public TeacherAppointmentVO updateStatus(TeacherAppointmentUpdateStatusRequest request) {
    if (request.getId() == null) {
      throw new RuntimeException("预约ID不能为空");
    }

    validateTeacher(request.getTeacherAccount());

    String targetStatus = safe(request.getStatus());
    if (!StringUtils.hasText(targetStatus)) {
      throw new RuntimeException("状态不能为空");
    }

    Appointment appointment = appointmentRepository.findByIdAndTeacherAccount(
            request.getId(),
            request.getTeacherAccount()
        )
        .orElseThrow(() -> new RuntimeException("未找到对应预约记录"));

    String currentStatus = appointment.getStatus();

    switch (targetStatus) {
      case "APPROVED":
        if (!"PENDING".equals(currentStatus)) {
          throw new RuntimeException("只有待处理预约才能通过");
        }
        appointment.setStatus("APPROVED");
        appointment.setApprovedAt(LocalDateTime.now());
        break;

      case "REJECTED":
        if (!"PENDING".equals(currentStatus)) {
          throw new RuntimeException("只有待处理预约才能拒绝");
        }
        appointment.setStatus("REJECTED");
        break;

      case "COMPLETED":
        if (!"APPROVED".equals(currentStatus)) {
          throw new RuntimeException("只有已通过预约才能标记完成");
        }
        appointment.setStatus("COMPLETED");
        appointment.setCompletedAt(LocalDateTime.now());
        break;

      default:
        throw new RuntimeException("不支持的状态操作");
    }

    appointment.setTeacherReply(request.getTeacherReply());
    Appointment saved = appointmentRepository.save(appointment);
    return toAppointmentVO(saved);
  }

  public List<TeacherAssessmentRecordVO> assessmentRecord(TeacherAssessmentRecordQueryRequest request) {
    validateTeacher(request.getTeacherAccount());

    String studentId = safe(request.getStudentId());
    if (!StringUtils.hasText(studentId)) {
      throw new RuntimeException("学生学号不能为空");
    }

    Student student = studentRepository.findByStudentId(studentId)
        .orElseThrow(() -> new RuntimeException("未找到对应学生信息"));

    List<AssessmentRecord> records =
        assessmentRecordRepository.findByStudentIdOrderBySubmittedAtDesc(studentId);

    return records.stream().map(item -> new TeacherAssessmentRecordVO(
        item.getId(),
        student.getStudentId(),
        student.getName(),
        student.getCollege(),
        student.getClassName(),
        item.getSemester(),

        item.getK10Score(),
        item.getK10Status(),
        item.getK10Level(),
        item.getK10Summary(),

        item.getWho5Score(),
        item.getWho5Status(),
        item.getWho5Level(),
        item.getWho5Summary(),

        item.getPhq9Score(),
        item.getPhq9Status(),
        item.getPhq9Level(),
        item.getPhq9Summary(),

        item.getGad7Score(),
        item.getGad7Status(),
        item.getGad7Level(),
        item.getGad7Summary(),

        item.getHealthTotalScore(),
        item.getHealthStatus(),
        item.getHealthSummary(),

        item.getSubmittedAt() == null ? null : item.getSubmittedAt().toString()
    )).toList();
  }

  private void validateTeacher(String teacherAccount) {
    if (!StringUtils.hasText(teacherAccount)) {
      throw new RuntimeException("老师账号不能为空");
    }

    teacherRepository.findByAccount(teacherAccount)
        .orElseThrow(() -> new RuntimeException("老师账号不存在"));
  }

  private LocalDate parseDate(String text) {
    if (!StringUtils.hasText(text)) {
      return null;
    }
    return LocalDate.parse(text.trim());
  }

  private String safe(String text) {
    return text == null ? null : text.trim();
  }

  private TeacherAppointmentVO toAppointmentVO(Appointment appointment) {
    String studentName = "";
    Student student = studentRepository.findByStudentId(appointment.getStudentAccount()).orElse(null);
    if (student != null) {
      studentName = student.getName();
    }

    return new TeacherAppointmentVO(
        appointment.getId(),
        appointment.getAppointmentNo(),
        appointment.getStudentAccount(),
        studentName,
        appointment.getTeacherAccount(),
        appointment.getScheduleId(),
        appointment.getAppointmentDate() == null ? null : appointment.getAppointmentDate().toString(),
        appointment.getStartTime() == null ? null : appointment.getStartTime().toString(),
        appointment.getEndTime() == null ? null : appointment.getEndTime().toString(),
        appointment.getPurpose(),
        appointment.getRemark(),
        appointment.getTeacherReply(),
        appointment.getStatus(),
        appointment.getCreatedAt() == null ? null : appointment.getCreatedAt().toString()
    );
  }
}