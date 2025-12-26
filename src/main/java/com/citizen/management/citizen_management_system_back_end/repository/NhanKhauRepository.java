package com.citizen.management.citizen_management_system_back_end.repository;

import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface NhanKhauRepository extends JpaRepository<NhanKhau, String>, JpaSpecificationExecutor<NhanKhau> {
    // Giữ nguyên vì khớp với field 'soCCCD' trong Entity NhanKhau
    boolean existsBySoCCCD(String soCCCD);

    Optional<NhanKhau> findBySoCCCD(String soCCCD);

    Optional<NhanKhau> findByTaiKhoan_Cccd(String cccd);

    long countByHoKhau(com.citizen.management.citizen_management_system_back_end.entity.HoKhau hoKhau);
}