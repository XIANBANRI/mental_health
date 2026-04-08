package com.sl.mentalhealth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sl.mentalhealth.dto.TeacherScheduleDeleteRequest;
import com.sl.mentalhealth.dto.TeacherScheduleQueryRequest;
import com.sl.mentalhealth.dto.TeacherScheduleSaveRequest;
import com.sl.mentalhealth.entity.Teacher;
import com.sl.mentalhealth.entity.TeacherSchedule;
import com.sl.mentalhealth.repository.AppointmentRepository;
import com.sl.mentalhealth.repository.TeacherRepository;
import com.sl.mentalhealth.repository.TeacherScheduleRepository;
import com.sl.mentalhealth.vo.TeacherScheduleVO;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocalTeacherScheduleServiceTest {

    @Mock
    private TeacherScheduleRepository teacherScheduleRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private LocalTeacherScheduleService service;

    private void mockTeacherExists(String account) {
        when(teacherRepository.findByAccount(account)).thenReturn(Optional.of(org.mockito.Mockito.mock(Teacher.class)));
    }

    @Test
    void query_allEnabled_success() {
        TeacherScheduleQueryRequest request = org.mockito.Mockito.mock(TeacherScheduleQueryRequest.class);
        when(request.getTeacherAccount()).thenReturn("t001");
        when(request.getWeekDay()).thenReturn(null);
        mockTeacherExists("t001");

        TeacherSchedule schedule = new TeacherSchedule();
        schedule.setId(1L);
        schedule.setTeacherAccount("t001");
        schedule.setWeekDay(1);
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(10, 0));
        schedule.setMaxAppointments(3);
        schedule.setRemark("上午");

        when(teacherScheduleRepository.findByTeacherAccountAndStatusOrderByWeekDayAscStartTimeAsc("t001", 1))
                .thenReturn(List.of(schedule));

        List<TeacherScheduleVO> result = service.query(request);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("t001", result.get(0).getTeacherAccount());
        assertTrue(result.get(0).getStartTime().startsWith("09:00"));
    }

    @Test
    void add_newRecord_success() {
        TeacherScheduleSaveRequest request = org.mockito.Mockito.mock(TeacherScheduleSaveRequest.class);
        when(request.getTeacherAccount()).thenReturn("t001");
        when(request.getWeekDay()).thenReturn(1);
        when(request.getStartTime()).thenReturn("09:00");
        when(request.getEndTime()).thenReturn("10:00");
        when(request.getMaxAppointments()).thenReturn(3);
        when(request.getRemark()).thenReturn("上午");
        mockTeacherExists("t001");

        when(teacherScheduleRepository.existsByTeacherAccountAndWeekDayAndStartTimeAndEndTimeAndStatus(
                "t001", 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 1)).thenReturn(false);
        when(teacherScheduleRepository.findByTeacherAccountAndWeekDayAndStartTimeAndEndTimeAndStatus(
                "t001", 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)).thenReturn(Optional.empty());
        when(teacherScheduleRepository.save(any(TeacherSchedule.class))).thenAnswer(invocation -> {
            TeacherSchedule entity = invocation.getArgument(0);
            entity.setId(1L);
            return entity;
        });

        TeacherScheduleVO result = service.add(request);

        assertEquals(1L, result.getId());
        assertEquals(1, result.getWeekDay());
        assertEquals(3, result.getMaxAppointments());
        assertEquals("上午", result.getRemark());
    }

    @Test
    void add_duplicateEnabledRecord_throwsException() {
        TeacherScheduleSaveRequest request = org.mockito.Mockito.mock(TeacherScheduleSaveRequest.class);
        when(request.getTeacherAccount()).thenReturn("t001");
        when(request.getWeekDay()).thenReturn(1);
        when(request.getStartTime()).thenReturn("09:00");
        when(request.getEndTime()).thenReturn("10:00");
        when(request.getMaxAppointments()).thenReturn(3);
        mockTeacherExists("t001");

        when(teacherScheduleRepository.existsByTeacherAccountAndWeekDayAndStartTimeAndEndTimeAndStatus(
                "t001", 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 1)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.add(request));
        assertEquals("该工作时间已存在，请勿重复添加", ex.getMessage());
    }

    @Test
    void update_missingId_throwsException() {
        TeacherScheduleSaveRequest request = org.mockito.Mockito.mock(TeacherScheduleSaveRequest.class);
        when(request.getTeacherAccount()).thenReturn("t001");
        when(request.getWeekDay()).thenReturn(1);
        when(request.getStartTime()).thenReturn("09:00");
        when(request.getEndTime()).thenReturn("10:00");
        when(request.getMaxAppointments()).thenReturn(3);
        when(request.getId()).thenReturn(null);
        mockTeacherExists("t001");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.update(request));
        assertEquals("工作时间ID不能为空", ex.getMessage());
    }

    @Test
    void delete_hasActiveAppointments_throwsException() {
        TeacherScheduleDeleteRequest request = org.mockito.Mockito.mock(TeacherScheduleDeleteRequest.class);
        when(request.getId()).thenReturn(1L);
        when(request.getTeacherAccount()).thenReturn("t001");
        mockTeacherExists("t001");

        TeacherSchedule entity = new TeacherSchedule();
        entity.setId(1L);
        entity.setTeacherAccount("t001");
        when(teacherScheduleRepository.findByIdAndTeacherAccount(1L, "t001")).thenReturn(Optional.of(entity));
        when(appointmentRepository.countByScheduleIdAndStatusNotIn(anyLong(), anySet())).thenReturn(2L);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.delete(request));
        assertEquals("该工作时间存在未完成的预约记录，无法删除。只有已完成、已拒绝或已取消的预约全部处理完后才可删除。", ex.getMessage());
    }

    @Test
    void delete_success_setsDisabledStatus() {
        TeacherScheduleDeleteRequest request = org.mockito.Mockito.mock(TeacherScheduleDeleteRequest.class);
        when(request.getId()).thenReturn(1L);
        when(request.getTeacherAccount()).thenReturn("t001");
        mockTeacherExists("t001");

        TeacherSchedule entity = new TeacherSchedule();
        entity.setId(1L);
        entity.setTeacherAccount("t001");
        entity.setStatus(1);
        when(teacherScheduleRepository.findByIdAndTeacherAccount(1L, "t001")).thenReturn(Optional.of(entity));
        when(appointmentRepository.countByScheduleIdAndStatusNotIn(anyLong(), anySet())).thenReturn(0L);
        when(teacherScheduleRepository.save(any(TeacherSchedule.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.delete(request);

        ArgumentCaptor<TeacherSchedule> captor = ArgumentCaptor.forClass(TeacherSchedule.class);
        verify(teacherScheduleRepository).save(captor.capture());
        assertEquals(0, captor.getValue().getStatus());
    }
}
