package com.citizen.management.citizen_management_system_back_end.repository;

import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.entity.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThongBaoRepository extends JpaRepository<ThongBao, String> {
    //Lấy danh sách thông báo của user, mới nhất lên đầu
    List<ThongBao> findAllByNguoiNhanOrderByThoiGianDesc(TaiKhoan nguoiNhan);
}
