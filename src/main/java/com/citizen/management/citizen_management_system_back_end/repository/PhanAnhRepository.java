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

    // Hàm này giữ nguyên để tìm list
    List<PhanAnh> findByTrangThaiHienTaiNotAndThoiHanXuLyBefore(EnumTrangThai trangThai, Date ngayHienTai);

    // Đếm theo trạng thái
    long countByTrangThaiHienTai(EnumTrangThai trangThai);

    // Đếm theo lĩnh vực
    long countByLinhVuc(String linhVuc);

    // Đếm theo mức độ khẩn cấp
    long countByMucDoKhanCap(EnumMucDoKhanCap mucDo);

    // === SỬA ĐỔI QUAN TRỌNG Ở ĐÂY ===
    // Thay thế hàm derived query dài dòng bằng @Query tường minh
    // Logic: Đếm các phản ánh có trạng thái KHÁC 'trangThai' (Đã xử lý) VÀ hạn xử lý < 'ngay' (Hôm nay)
    @Query("SELECT COUNT(p) FROM PhanAnh p WHERE p.trangThaiHienTai <> :trangThai AND p.thoiHanXuLy < :ngay")
    long countByTrangThaiHienTaiNotAndThoiHanXuLyBefore(@Param("trangThai") EnumTrangThai trangThai, @Param("ngay") Date ngay);

    // Đếm theo tháng (Code đã fix cho PostgreSQL)
    @Query("SELECT CAST(EXTRACT(MONTH FROM p.thoiGianTao) as int) as month, COUNT(p) " +
            "FROM PhanAnh p " +
            "WHERE EXTRACT(YEAR FROM p.thoiGianTao) = :year " +
            "GROUP BY CAST(EXTRACT(MONTH FROM p.thoiGianTao) as int)")
    List<Object[]> countByMonth(@Param("year") int year);
}