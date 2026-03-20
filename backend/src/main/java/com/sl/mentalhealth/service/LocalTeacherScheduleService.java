package com.sl.mentalhealth.service;

import com.sl.mentalhealth.dto.TeacherScheduleDeleteRequest;
import com.sl.mentalhealth.dto.TeacherScheduleQueryRequest;
import com.sl.mentalhealth.dto.TeacherScheduleSaveRequest;
import com.sl.mentalhealth.entity.Teacher;
import com.sl.mentalhealth.entity.TeacherSchedule;
import com.sl.mentalhealth.repository.TeacherRepository;
import com.sl.mentalhealth.repository.TeacherScheduleRepository;
import com.sl.mentalhealth.vo.TeacherScheduleVO;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class LocalTeacherScheduleService {

  private final TeacherScheduleRepository teacherScheduleRepository;
  private final TeacherRepository teacherRepository;

  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
  private static final DateTimeFormatter SHORT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

  public List<TeacherScheduleVO> query(TeacherScheduleQueryRequest request) {
    validateTeacherAccount(request.getTeacherAccount());

    List<TeacherSchedule> list;
    if (request.getWeekDay() == null) {
      list = teacherScheduleRepository.findByTeacherAccountOrderByWeekDayAscStartTimeAsc(
          request.getTeacherAccount()
      );
    } else {
      validateWeekDay(request.getWeekDay());
      list = teacherScheduleRepository.findByTeacherAccountAndWeekDayOrderByStartTimeAsc(
          request.getTeacherAccount(),
          request.getWeekDay()
      );
    }

    return list.stream().map(this::toVO).toList();
  }

  public TeacherScheduleVO add(TeacherScheduleSaveRequest request) {
    validateTeacherAccount(request.getTeacherAccount());
    validateSaveRequest(request);

    LocalTime startTime = parseTime(request.getStartTime());
    LocalTime endTime = parseTime(request.getEndTime());

    if (!startTime.isBefore(endTime)) {
      throw new RuntimeException("开始时间必须早于结束时间");
    }

    boolean exists = teacherScheduleRepository.existsByTeacherAccountAndWeekDayAndStartTimeAndEndTime(
        request.getTeacherAccount(),
        request.getWeekDay(),
        startTime,
        endTime
    );
    if (exists) {
      throw new RuntimeException("该工作时间已存在，请勿重复添加");
    }

    TeacherSchedule entity = new TeacherSchedule();
    entity.setTeacherAccount(request.getTeacherAccount());
    entity.setWeekDay(request.getWeekDay());
    entity.setStartTime(startTime);
    entity.setEndTime(endTime);
    entity.setMaxAppointments(request.getMaxAppointments());
    entity.setRemark(request.getRemark());

    TeacherSchedule saved = teacherScheduleRepository.save(entity);
    return toVO(saved);
  }

  public TeacherScheduleVO update(TeacherScheduleSaveRequest request) {
    validateTeacherAccount(request.getTeacherAccount());
    validateSaveRequest(request);

    if (request.getId() == null) {
      throw new RuntimeException("工作时间ID不能为空");
    }

    LocalTime startTime = parseTime(request.getStartTime());
    LocalTime endTime = parseTime(request.getEndTime());

    if (!startTime.isBefore(endTime)) {
      throw new RuntimeException("开始时间必须早于结束时间");
    }

    TeacherSchedule entity = teacherScheduleRepository.findByIdAndTeacherAccount(
            request.getId(),
            request.getTeacherAccount()
        )
        .orElseThrow(() -> new RuntimeException("未找到对应的工作时间记录"));

    boolean exists = teacherScheduleRepository
        .existsByTeacherAccountAndWeekDayAndStartTimeAndEndTimeAndIdNot(
            request.getTeacherAccount(),
            request.getWeekDay(),
            startTime,
            endTime,
            request.getId()
        );
    if (exists) {
      throw new RuntimeException("修改后的工作时间与已有记录重复");
    }

    entity.setWeekDay(request.getWeekDay());
    entity.setStartTime(startTime);
    entity.setEndTime(endTime);
    entity.setMaxAppointments(request.getMaxAppointments());
    entity.setRemark(request.getRemark());

    TeacherSchedule saved = teacherScheduleRepository.save(entity);
    return toVO(saved);
  }

  public void delete(TeacherScheduleDeleteRequest request) {
    if (request.getId() == null) {
      throw new RuntimeException("工作时间ID不能为空");
    }
    validateTeacherAccount(request.getTeacherAccount());

    TeacherSchedule entity = teacherScheduleRepository.findByIdAndTeacherAccount(
            request.getId(),
            request.getTeacherAccount()
        )
        .orElseThrow(() -> new RuntimeException("未找到对应的工作时间记录"));

    teacherScheduleRepository.delete(entity);
  }

  private void validateTeacherAccount(String teacherAccount) {
    if (!StringUtils.hasText(teacherAccount)) {
      throw new RuntimeException("老师账号不能为空");
    }

    Teacher teacher = teacherRepository.findByAccount(teacherAccount)
        .orElseThrow(() -> new RuntimeException("老师账号不存在"));
  }

  private void validateSaveRequest(TeacherScheduleSaveRequest request) {
    if (request.getWeekDay() == null) {
      throw new RuntimeException("星期不能为空");
    }
    validateWeekDay(request.getWeekDay());

    if (!StringUtils.hasText(request.getStartTime())) {
      throw new RuntimeException("开始时间不能为空");
    }
    if (!StringUtils.hasText(request.getEndTime())) {
      throw new RuntimeException("结束时间不能为空");
    }
    if (request.getMaxAppointments() == null || request.getMaxAppointments() < 1) {
      throw new RuntimeException("最大预约人数必须大于0");
    }
  }

  private void validateWeekDay(Integer weekDay) {
    if (weekDay < 1 || weekDay > 7) {
      throw new RuntimeException("星期参数不合法");
    }
  }

  private LocalTime parseTime(String timeText) {
    String text = timeText.trim();
    if (text.length() == 5) {
      return LocalTime.parse(text, SHORT_TIME_FORMATTER);
    }
    return LocalTime.parse(text, TIME_FORMATTER);
  }

  private TeacherScheduleVO toVO(TeacherSchedule entity) {
    return new TeacherScheduleVO(
        entity.getId(),
        entity.getTeacherAccount(),
        entity.getWeekDay(),
        entity.getStartTime() == null ? null : entity.getStartTime().toString(),
        entity.getEndTime() == null ? null : entity.getEndTime().toString(),
        entity.getMaxAppointments(),
        entity.getRemark()
    );
  }
}