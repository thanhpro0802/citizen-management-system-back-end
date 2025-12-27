package com.citizen.management.citizen_management_system_back_end.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.citizen.management.citizen_management_system_back_end.dto.projection.KeyValueProjection;
import com.citizen.management.citizen_management_system_back_end.dto.projection.PhanAnhTrangThaiProjection;
import com.citizen.management.citizen_management_system_back_end.dto.projection.PhanAnhTrangThaiTheoNamProjection;
import com.citizen.management.citizen_management_system_back_end.dto.projection.TamTruTamVangProjection;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;

public interface ThongKeRepository extends JpaRepository<NhanKhau, String> {

    long count();

    @Query("SELECT YEAR(CURRENT_DATE) - YEAR(n.ngaySinh) AS tuoi, COUNT(n) AS soLuong FROM NhanKhau n GROUP BY tuoi")
    List<KeyValueProjection> theoTuoi();

    @Query("SELECT n.gioiTinh AS key, COUNT(n) AS value FROM NhanKhau n GROUP BY n.gioiTinh")
    List<KeyValueProjection> theoGioiTinh();

    @Query("SELECT n.queQuan AS key, COUNT(n) AS value FROM NhanKhau n GROUP BY n.queQuan")
    List<KeyValueProjection> theoQueQuan();

    @Query("""
                SELECT n.danToc AS key,
                    COUNT(n) AS value
                FROM NhanKhau n
                GROUP BY n.danToc
            """)
    List<KeyValueProjection> theoDanToc();

    @Query("""
                SELECT hk.id AS key,
                    COUNT(nk.id) AS value
                FROM HoKhau hk
                LEFT JOIN NhanKhau nk ON nk.hoKhau = hk
                GROUP BY hk.id
            """)
    List<KeyValueProjection> theoSoThanhVien();

    @Query("""
                SELECT DATE(lspa.thoiGian) AS key,
                    COUNT(lspa.id) AS value
                FROM LichSuPhanAnh lspa
                WHERE lspa.thoiGian BETWEEN :start AND :end
                GROUP BY DATE(lspa.thoiGian)
            """)
    List<KeyValueProjection> theoNgay(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("""
                SELECT
                    FUNCTION('DATE', lspa.thoiGian) AS ngay,

                    COALESCE(SUM(
                        CASE WHEN pa.trangThaiHienTai = 'CHO' THEN 1 ELSE 0 END
                    ), 0) AS choXuLy,

                    COALESCE(SUM(
                        CASE WHEN pa.trangThaiHienTai = 'DANG_XU_LY' THEN 1 ELSE 0 END
                    ), 0) AS dangXuLy,

                    COALESCE(SUM(
                        CASE WHEN pa.trangThaiHienTai = 'DA_XU_LY' THEN 1 ELSE 0 END
                    ), 0) AS daXuLy

                FROM LichSuPhanAnh lspa
                JOIN lspa.phanAnh pa
                WHERE lspa.thoiGian BETWEEN :start AND :end
                GROUP BY FUNCTION('DATE', lspa.thoiGian)
                ORDER BY FUNCTION('DATE', lspa.thoiGian)
            """)
    List<PhanAnhTrangThaiProjection> thongKeTheoNgay(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("""
                SELECT
                    EXTRACT(MONTH FROM lspa.thoiGian) AS thang,

                    COALESCE(SUM(
                        CASE WHEN pa.trangThaiHienTai = 'CHO' THEN 1 ELSE 0 END
                    ), 0) AS choXuLy,

                    COALESCE(SUM(
                        CASE WHEN pa.trangThaiHienTai = 'DANG_XU_LY' THEN 1 ELSE 0 END
                    ), 0) AS dangXuLy,

                    COALESCE(SUM(
                        CASE WHEN pa.trangThaiHienTai = 'DA_XU_LY' THEN 1 ELSE 0 END
                    ), 0) AS daXuLy

                FROM LichSuPhanAnh lspa
                JOIN lspa.phanAnh pa
                WHERE EXTRACT(YEAR FROM lspa.thoiGian) = :year
                GROUP BY EXTRACT(MONTH FROM lspa.thoiGian)
                ORDER BY EXTRACT(MONTH FROM lspa.thoiGian)
            """)
    List<PhanAnhTrangThaiTheoNamProjection> thongKeTheoThang(
            @Param("year") int year);

    @Query("""
                SELECT COUNT(tt)
                FROM TamTru tt
                WHERE :time BETWEEN tt.ngayBatDau AND tt.ngayKetThuc
            """)
    Long countTamTru(@Param("time") LocalDateTime time);

    @Query("""
                SELECT COUNT(tv)
                FROM TamVang tv
                WHERE :time BETWEEN tv.ngayBatDau AND tv.ngayKetThuc
            """)
    Long countTamVang(@Param("time") LocalDateTime time);

    @Query(value = """
                SELECT ngay,
                       SUM(bat_dau) AS batDau,
                       SUM(ket_thuc) AS ketThuc
                FROM (
                    SELECT
                        DATE(ngay_bat_dau) AS ngay,
                        COUNT(*) AS bat_dau,
                        0 AS ket_thuc
                    FROM tam_tru
                    WHERE ngay_bat_dau BETWEEN :start AND :end
                    GROUP BY DATE(ngay_bat_dau)

                    UNION ALL

                    SELECT
                        DATE(ngay_ket_thuc) AS ngay,
                        0 AS bat_dau,
                        COUNT(*) AS ket_thuc
                    FROM tam_tru
                    WHERE ngay_ket_thuc BETWEEN :start AND :end
                    GROUP BY DATE(ngay_ket_thuc)
                ) t
                GROUP BY ngay
                ORDER BY ngay
            """, nativeQuery = true)
    List<TamTruTamVangProjection> thongKeTamTruTheoNgay(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query(value = """
                SELECT ngay,
                       SUM(bat_dau) AS batDau,
                       SUM(ket_thuc) AS ketThuc
                FROM (
                    SELECT
                        DATE(ngay_bat_dau) AS ngay,
                        COUNT(*) AS bat_dau,
                        0 AS ket_thuc
                    FROM tam_vang
                    WHERE ngay_bat_dau BETWEEN :start AND :end
                    GROUP BY DATE(ngay_bat_dau)

                    UNION ALL

                    SELECT
                        DATE(ngay_ket_thuc) AS ngay,
                        0 AS bat_dau,
                        COUNT(*) AS ket_thuc
                    FROM tam_vang
                    WHERE ngay_ket_thuc BETWEEN :start AND :end
                    GROUP BY DATE(ngay_ket_thuc)
                ) t
                GROUP BY ngay
                ORDER BY ngay
            """, nativeQuery = true)
    List<TamTruTamVangProjection> thongKeTamVangTheoNgay(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

}