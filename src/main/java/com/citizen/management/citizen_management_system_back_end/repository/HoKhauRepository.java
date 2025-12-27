package com.citizen.management.citizen_management_system_back_end.repository;

import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HoKhauRepository extends JpaRepository<HoKhau, String> {
    // Tìm kiếm hộ khẩu theo địa chỉ (không phân biệt hoa thường)
    List<HoKhau> findByDiaChiContainingIgnoreCase(String diaChi);

    // Tìm kiếm tổng hợp: Địa chỉ HOẶC Tên chủ hộ HOẶC CCCD chủ hộ
    @Query("SELECT h FROM HoKhau h WHERE " +
            "LOWER(h.diaChi) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(h.chuHo.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "h.chuHo.soCCCD LIKE CONCAT('%', :keyword, '%')")
    List<HoKhau> timKiemTongHop(String keyword);

    // Tìm hộ khẩu theo mã nhân khẩu của chủ hộ
    @Query("SELECT h FROM HoKhau h WHERE " +
            "LOWER(h.chuHo.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "h.chuHo.soCCCD LIKE CONCAT('%', :keyword, '%')")
    List<HoKhau> findByChuHoContaining(String keyword);

    // Đếm theo tháng đăng ký (FIXED FOR POSTGRESQL)
    @Query("SELECT CAST(EXTRACT(MONTH FROM h.ngayDangKy) as int) as month, COUNT(h) " +
            "FROM HoKhau h " +
            "WHERE EXTRACT(YEAR FROM h.ngayDangKy) = :year " +
            "GROUP BY CAST(EXTRACT(MONTH FROM h.ngayDangKy) as int)")
    List<Object[]> countByMonth(@Param("year") int year);
}