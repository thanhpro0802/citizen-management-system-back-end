package com.citizen.management.citizen_management_system_back_end.repository;

import com.citizen.management.citizen_management_system_back_end.model.HoKhau;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoKhauRepository extends JpaRepository<HoKhau, Long> {
    // Custom query nếu cần
}