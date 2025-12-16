package com.citizen.management.citizen_management_system_back_end.repository;

import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.entity.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThongBaoReposity extends JpaRepository<ThongBao, String> {
    //Lay danh sach thong bao cua user, moi nhat len dau
    List<ThongBao> findAllByNguoiNhanOrderByThoiGianDesc(TaiKhoan nguoiNhan);
    long countByNguoiNhanAndDaXemFalse(TaiKhoan nguoiNhan);
}
