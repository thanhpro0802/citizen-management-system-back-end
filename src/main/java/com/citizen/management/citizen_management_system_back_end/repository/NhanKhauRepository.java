package com.citizen.management.citizen_management_system_back_end.repository;

import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThaiNhanKhau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface NhanKhauRepository extends JpaRepository<NhanKhau, String>, JpaSpecificationExecutor<NhanKhau> {
    // Giữ nguyên vì khớp với field 'soCCCD' trong Entity NhanKhau
    boolean existsBySoCCCD(String soCCCD);

    Optional<NhanKhau> findBySoCCCD(String soCCCD);
    
    // Đếm theo giới tính
    long countByGioiTinh(String gioiTinh);

    long countByTrangThai(EnumTrangThaiNhanKhau trangThai);
}