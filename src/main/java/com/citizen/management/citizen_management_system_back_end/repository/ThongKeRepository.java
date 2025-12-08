package com.citizen.management.citizen_management_system_back_end.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.citizen.management.citizen_management_system_back_end.dto.projection.KeyValueProjection;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;

public interface ThongKeRepository extends JpaRepository<NhanKhau, Long> {
    
    @Query(
        "SELECT n.gioiTinh AS gioiTinh, COUNT(n) AS soLuong FROM NhanKhau n GROUP BY n.gioiTinh"
    )
    List<KeyValueProjection> theoGioiTinh();
}
