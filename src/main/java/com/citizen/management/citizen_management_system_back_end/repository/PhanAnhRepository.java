package com.citizen.management.citizen_management_system_back_end.repository;

import com.citizen.management.citizen_management_system_back_end.entity.PhanAnh;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.enums.EnumMucDoKhanCap;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PhanAnhRepository extends JpaRepository<PhanAnh,String> {
    // SELECT * FROM phan_anh WHERE ma_tai_khoan_gui = ?
    List<PhanAnh> findAllByNguoiGui(TaiKhoan nguoiGui);
    List<PhanAnh> findAllByOrderByMaPhanAnhDesc();
    List<PhanAnh> findByTrangThaiHienTaiNotAndThoiHanXuLyBefore(EnumTrangThai trangThai, Date ngayHienTai);
    
    // Đếm theo trạng thái
    long countByTrangThaiHienTai(EnumTrangThai trangThai);
    
    // Đếm theo lĩnh vực
    long countByLinhVuc(String linhVuc);
    
    // Đếm theo mức độ khẩn cấp
    long countByMucDoKhanCap(EnumMucDoKhanCap mucDo);
    
    // Đếm quá hạn
    long countByTrangThaiHienTaiNotAndThoiHanXuLyBefore(EnumTrangThai trangThai, Date ngay);
    
    // Đếm theo tháng
    @Query("SELECT FUNCTION('MONTH', p.thoiGianTao) as month, COUNT(p) FROM PhanAnh p WHERE FUNCTION('YEAR', p.thoiGianTao) = :year GROUP BY FUNCTION('MONTH', p.thoiGianTao)")
    List<Object[]> countByMonth(@Param("year") int year);
}
