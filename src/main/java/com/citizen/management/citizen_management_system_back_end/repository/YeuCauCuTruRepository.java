package com.citizen.management.citizen_management_system_back_end.repository;

import com.citizen.management.citizen_management_system_back_end.entity.YeuCauCuTru;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;
import com.citizen.management.citizen_management_system_back_end.enums.EnumLoaiYeuCauCuTru;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThaiYeuCau;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YeuCauCuTruRepository extends JpaRepository<YeuCauCuTru, String> {

    // Tìm tất cả yêu cầu của một người dân
    List<YeuCauCuTru> findByNguoiTaoOrderByNgayTaoDesc(NhanKhau nguoiTao);

    // Tìm theo trạng thái
    Page<YeuCauCuTru> findByTrangThai(EnumTrangThaiYeuCau trangThai, Pageable pageable);

    // Tìm theo loại yêu cầu
    Page<YeuCauCuTru> findByLoaiYeuCau(EnumLoaiYeuCauCuTru loaiYeuCau, Pageable pageable);

    // Tìm tất cả yêu cầu (cho cán bộ)
    Page<YeuCauCuTru> findAllByOrderByNgayTaoDesc(Pageable pageable);

    // Tìm yêu cầu của một người dân với phân trang
    Page<YeuCauCuTru> findByNguoiTaoOrderByNgayTaoDesc(NhanKhau nguoiTao, Pageable pageable);

    // Đếm số yêu cầu theo trạng thái
    long countByTrangThai(EnumTrangThaiYeuCau trangThai);

    // Tìm yêu cầu theo trạng thái và loại
    Page<YeuCauCuTru> findByTrangThaiAndLoaiYeuCau(
        EnumTrangThaiYeuCau trangThai, 
        EnumLoaiYeuCauCuTru loaiYeuCau, 
        Pageable pageable
    );

    // Tìm kiếm yêu cầu theo nhiều tiêu chí
    @Query("SELECT y FROM YeuCauCuTru y WHERE " +
           "(:trangThai IS NULL OR y.trangThai = :trangThai) AND " +
           "(:loaiYeuCau IS NULL OR y.loaiYeuCau = :loaiYeuCau) AND " +
           "(:nguoiTao IS NULL OR y.nguoiTao = :nguoiTao) " +
           "ORDER BY y.ngayTao DESC")
    Page<YeuCauCuTru> searchYeuCau(
        @Param("trangThai") EnumTrangThaiYeuCau trangThai,
        @Param("loaiYeuCau") EnumLoaiYeuCauCuTru loaiYeuCau,
        @Param("nguoiTao") NhanKhau nguoiTao,
        Pageable pageable
    );
}
