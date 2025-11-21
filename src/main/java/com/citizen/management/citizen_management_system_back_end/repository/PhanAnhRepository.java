package com.citizen.management.citizen_management_system_back_end.repository;

import com.citizen.management.citizen_management_system_back_end.entity.PhanAnh;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhanAnhRepository extends JpaRepository<PhanAnh,String> {
    // SELECT * FROM phan_anh WHERE ma_tai_khoan_gui = ?
    List<PhanAnh> findAllByNguoiGui(TaiKhoan nguoiGui);
}
