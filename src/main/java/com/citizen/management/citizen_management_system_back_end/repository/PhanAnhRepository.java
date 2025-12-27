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
    List<PhanAnh> findAllByNguoiGui(TaiKhoan nguoiGui);
    List<PhanAnh> findAllByOrderByMaPhanAnhDesc();

    List<PhanAnh> findByTrangThaiHienTaiNotAndThoiHanXuLyBefore(EnumTrangThai trangThai, Date ngayHienTai);

    long countByTrangThaiHienTai(EnumTrangThai trangThai);

    // --- BỔ SUNG HÀM NÀY ĐỂ LỌC THEO QUÝ ---
    // Đếm theo trạng thái trong khoảng thời gian (dựa trên thoiGianTao)
    long countByTrangThaiHienTaiAndThoiGianTaoBetween(EnumTrangThai trangThai, Date startDate, Date endDate);
    // ---------------------------------------

    long countByLinhVuc(String linhVuc);

    long countByMucDoKhanCap(EnumMucDoKhanCap mucDo);

    @Query("SELECT COUNT(p) FROM PhanAnh p WHERE p.trangThaiHienTai <> :trangThai AND p.thoiHanXuLy < :ngay")
    long countByTrangThaiHienTaiNotAndThoiHanXuLyBefore(@Param("trangThai") EnumTrangThai trangThai, @Param("ngay") Date ngay);

    @Query("SELECT CAST(EXTRACT(MONTH FROM p.thoiGianTao) as int) as month, COUNT(p) " +
            "FROM PhanAnh p " +
            "WHERE EXTRACT(YEAR FROM p.thoiGianTao) = :year " +
            "GROUP BY CAST(EXTRACT(MONTH FROM p.thoiGianTao) as int)")
    List<Object[]> countByMonth(@Param("year") int year);
}