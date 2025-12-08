package com.citizen.management.citizen_management_system_back_end.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.citizen.management.citizen_management_system_back_end.dto.projection.KeyValueProjection;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;

public interface ThongKeRepository extends JpaRepository<NhanKhau, Long> {
    
    @Query(
        "SELECT YEAR(CURRENT_DATE) - YEAR(n.ngaySinh) AS tuoi, COUNT(n) AS soLuong FROM NhanKhau n GROUP BY tuoi"
    )
    List<KeyValueProjection> theoTuoi();

    @Query(
        "SELECT n.gioiTinh AS key, COUNT(n) AS value FROM NhanKhau n GROUP BY n.gioiTinh"
    )
    List<KeyValueProjection> theoGioiTinh();

    @Query(
        "SELECT n.queQuan AS key, COUNT(n) AS value FROM NhanKhau n GROUP BY n.queQuan"
    )
    List<KeyValueProjection> theoQueQuan();  
    
    @Query(
        "SELECT n.danToc AS key, COUNT(n) AS value FROM NhanKhau n GROUP BY n.danToc"
    )
    List<KeyValueProjection> theoDanToc(); 
}
