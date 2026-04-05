package com.sl.mentalhealth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sl.mentalhealth.entity.Admin;
import com.sl.mentalhealth.repository.AdminRepository;
import com.sl.mentalhealth.vo.AdminProfileResponseVO;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocalAdminProfileServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private LocalAdminProfileService service;

    @Test
    void getAdminProfile_success() {
        Admin admin = new Admin();
        admin.setAccount("admin001");
        admin.setName("系统管理员");
        admin.setAvatarUrl("/files/avatar/admin/a.png");

        when(adminRepository.findById("admin001")).thenReturn(Optional.of(admin));

        AdminProfileResponseVO result = service.getAdminProfile("admin001");

        assertEquals("admin001", result.getAccount());
        assertEquals("系统管理员", result.getName());
        assertEquals("/files/avatar/admin/a.png", result.getAvatarUrl());
    }

    @Test
    void getAdminProfile_blankAccount_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.getAdminProfile("  "));

        assertEquals("管理员账号不能为空", ex.getMessage());
    }

    @Test
    void updateAvatar_success() {
        Admin admin = new Admin();
        admin.setAccount("admin001");
        admin.setName("系统管理员");

        when(adminRepository.findById("admin001")).thenReturn(Optional.of(admin));
        when(adminRepository.save(any(Admin.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AdminProfileResponseVO result = service.updateAvatar("admin001", " /files/avatar/admin/new.png ");

        assertEquals("/files/avatar/admin/new.png", result.getAvatarUrl());
        verify(adminRepository).save(admin);
    }

    @Test
    void updateAvatar_adminNotFound_throwsRuntimeException() {
        when(adminRepository.findById("admin001")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateAvatar("admin001", "/files/avatar/admin/new.png"));

        assertEquals("管理员不存在", ex.getMessage());
    }
}
