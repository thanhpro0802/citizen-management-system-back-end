package com.citizen.management.citizen_management_system_back_end.repository;

import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoKhauRepository extends JpaRepository<HoKhau, String> {
    // Tìm kiếm hộ khẩu theo địa chỉ (không phân biệt hoa thường)
    List<HoKhau> findByDiaChiContainingIgnoreCase(String diaChi);

    // Tìm hộ khẩu theo mã nhân khẩu của chủ hộ
    Optional<HoKhau> findByChuHo_MaNhanKhau(String maNhanKhau);
}