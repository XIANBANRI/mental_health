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
import java.time.format.DateTimeFormatter;
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

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    String currentStatus = safe(appointment.getStatus());
    String newOfflineRecord = safe(request.getOfflineRecord());
    LocalDateTime now = LocalDateTime.now();

    switch (targetStatus) {
      case "APPROVED" -> handleApproved(appointment, currentStatus, newOfflineRecord, now);
      case "REJECTED" -> handleRejected(appointment, currentStatus);
      case "COMPLETED" -> handleCompleted(appointment, currentStatus, newOfflineRecord, now);
      default -> throw new RuntimeException("不支持的状态操作");
    }

    Appointment saved = appointmentRepository.save(appointment);
    return toAppointmentVO(saved);
  }

  private void handleApproved(
      Appointment appointment,
      String currentStatus,
      String newOfflineRecord,
      LocalDateTime now
  ) {
    if ("PENDING".equals(currentStatus)) {
      appointment.setStatus("APPROVED");
      appointment.setApprovedAt(now);
      return;
    }

    if (!"APPROVED".equals(currentStatus)) {
      throw new RuntimeException("只有待处理或已通过预约才能执行该操作");
    }

    if (!StringUtils.hasText(newOfflineRecord)) {
      throw new RuntimeException("线下问诊记录不能为空");
    }

    appointment.setTeacherReply(newOfflineRecord);
  }

  private void handleRejected(Appointment appointment, String currentStatus) {
    if (!"PENDING".equals(currentStatus)) {
      throw new RuntimeException("只有待处理预约才能拒绝");
    }
    appointment.setStatus("REJECTED");
  }

  private void handleCompleted(
      Appointment appointment,
      String currentStatus,
      String newOfflineRecord,
      LocalDateTime now
  ) {
    if (!"APPROVED".equals(currentStatus)) {
      throw new RuntimeException("只有已通过预约才能完成记录");
    }

    String finalRecord = StringUtils.hasText(newOfflineRecord)
        ? newOfflineRecord
        : safe(appointment.getTeacherReply());

    if (!StringUtils.hasText(finalRecord)) {
      throw new RuntimeException("请先填写线下问诊记录，再点击完成");
    }

    appointment.setTeacherReply(finalRecord);
    appointment.setStatus("COMPLETED");
    appointment.setCompletedAt(now);
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
        item.getSubmittedAt() == null ? "" : item.getSubmittedAt().format(DATE_TIME_FORMATTER)
    )).toList();
  }

  private TeacherAppointmentVO toAppointmentVO(Appointment appointment) {
    String studentId = appointment.getStudentAccount();
    String studentName = "";

    if (StringUtils.hasText(studentId)) {
      studentName = studentRepository.findByStudentId(studentId)
          .map(Student::getName)
          .orElse("");
    }

    boolean recordCompleted = "COMPLETED".equals(safe(appointment.getStatus()));

    return new TeacherAppointmentVO(
        appointment.getId(),
        appointment.getAppointmentNo(),
        studentId,
        studentName,
        appointment.getTeacherAccount(),
        appointment.getScheduleId(),
        appointment.getAppointmentDate() == null ? "" : appointment.getAppointmentDate().format(DATE_FORMATTER),
        appointment.getStartTime() == null ? "" : appointment.getStartTime().format(TIME_FORMATTER),
        appointment.getEndTime() == null ? "" : appointment.getEndTime().format(TIME_FORMATTER),
        safe(appointment.getPurpose()),
        safe(appointment.getRemark()),
        safe(appointment.getTeacherReply()),
        recordCompleted,
        safe(appointment.getStatus()),
        formatDateTime(appointment.getCreatedAt()),
        formatDateTime(appointment.getApprovedAt()),
        formatDateTime(appointment.getCompletedAt())
    );
  }

  private void validateTeacher(String teacherAccount) {
    if (!StringUtils.hasText(teacherAccount)) {
      throw new RuntimeException("老师账号不能为空");
    }

    teacherRepository.findByAccount(teacherAccount)
        .orElseThrow(() -> new RuntimeException("老师账号不存在"));
  }

  private LocalDate parseDate(String value) {
    String text = safe(value);
    if (!StringUtils.hasText(text)) {
      return null;
    }
    return LocalDate.parse(text);
  }

  private String formatDateTime(LocalDateTime value) {
    return value == null ? "" : value.format(DATE_TIME_FORMATTER);
  }

  private String safe(String value) {
    return value == null ? "" : value.trim();
  }
}