package com.citizen.management.citizen_management_system_back_end.repository;

import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, String> {
    // SỬA: Tìm theo cccd (khớp với field 'cccd' trong Entity TaiKhoan)
    Optional<TaiKhoan> findByCccd(String cccd);

    // SỬA: Kiểm tra tồn tại theo cccd
    Boolean existsByCccd(String cccd);
}