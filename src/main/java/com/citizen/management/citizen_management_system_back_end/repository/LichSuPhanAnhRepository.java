package com.citizen.management.citizen_management_system_back_end.repository;

import com.citizen.management.citizen_management_system_back_end.entity.LichSuPhanAnh;
import com.citizen.management.citizen_management_system_back_end.entity.PhanAnh;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LichSuPhanAnhRepository extends JpaRepository<LichSuPhanAnh,String> {
    List<LichSuPhanAnh> findByPhanAnhOrderByThoiGianDesc(PhanAnh phanAnh);
}
