package com.citizen.management.citizen_management_system_back_end.entity;

import com.citizen.management.citizen_management_system_back_end.enums.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Entity đại diện cho các yêu cầu cư trú (tạm trú, thường trú, tạm vắng, điều chỉnh, xóa đăng ký)
 */
@Entity
@Table(name = "yeu_cau_cu_tru")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class YeuCauCuTru {

    @Id
    @Column(name = "ma_yeu_cau")
    private String maYeuCau;

    // Loại yêu cầu
    @Enumerated(EnumType.STRING)
    @Column(name = "loai_yeu_cau", nullable = false, length = 50)
    private EnumLoaiYeuCauCuTru loaiYeuCau;

    // Người tạo yêu cầu (CONG_DAN)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_tao_id")
    @JsonIgnoreProperties({"hoKhau", "danhSachTamTru", "danhSachTamVang", "taiKhoan", "hibernateLazyInitializer", "handler"})
    private NhanKhau nguoiTao;

    // Đối tượng đề nghị
    @Enumerated(EnumType.STRING)
    @Column(name = "doi_tuong_de_nghi", length = 20)
    private EnumDoiTuongDeNghi doiTuongDeNghi;

    // === Thông tin khai hộ (nếu doiTuongDeNghi = KHAI_HO) ===
    @Column(name = "nguoi_de_nghi_ho_ten")
    private String nguoiDeNghiHoTen;

    @Column(name = "nguoi_de_nghi_ngay_sinh")
    private Date nguoiDeNghiNgaySinh;

    @Column(name = "nguoi_de_nghi_gioi_tinh")
    private String nguoiDeNghiGioiTinh;

    @Column(name = "nguoi_de_nghi_cccd")
    private String nguoiDeNghiCccd;

    // === Thông tin cho ĐĂNG KÝ TẠM TRÚ / THƯỜNG TRÚ ===
    @Enumerated(EnumType.STRING)
    @Column(name = "loai_hinh_dang_ky", length = 20)
    private EnumLoaiHinhDangKy loaiHinhDangKy;

    // Thông tin chủ hộ (nếu loaiHinhDangKy = VAO_HO_DA_CO)
    @Column(name = "chu_ho_ho_ten")
    private String chuHoHoTen;

    @Column(name = "chu_ho_cccd")
    private String chuHoCccd;

    // Địa chỉ cư trú
    @Column(name = "dia_chi_cu_tru", length = 500)
    private String diaChiCuTru;

    // === Thông tin cho KHAI BÁO TẠM VẮNG ===
    @Enumerated(EnumType.STRING)
    @Column(name = "loai_tam_vang", length = 20)
    private EnumLoaiTamVang loaiTamVang;

    @Column(name = "noi_den", length = 500)
    private String noiDen;

    @Column(name = "thoi_gian_bat_dau")
    private Date thoiGianBatDau;

    @Column(name = "thoi_gian_ket_thuc")
    private Date thoiGianKetThuc;

    @Column(name = "ly_do", length = 1000)
    private String lyDo;

    // === Thông tin cho ĐIỀU CHỈNH THÔNG TIN ===
    @Column(name = "phan_can_dieu_chinh", length = 1000)
    private String phanCanDieuChinh;

    // === Thông tin chung ===
    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false, length = 20)
    private EnumTrangThaiYeuCau trangThai;

    @Column(name = "ngay_tao")
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    private Date ngayCapNhat;

    // Cán bộ xử lý
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "can_bo_xu_ly_id")
    @JsonIgnoreProperties({"hoKhau", "danhSachTamTru", "danhSachTamVang", "taiKhoan", "hibernateLazyInitializer", "handler"})
    private NhanKhau canBoXuLy;

    // Ghi chú từ cán bộ
    @Column(name = "ghi_chu", length = 2000)
    private String ghiChu;

    // Lý do từ chối (nếu có)
    @Column(name = "ly_do_tu_choi", length = 1000)
    private String lyDoTuChoi;

    // File đính kèm (nếu có) - có thể lưu đường dẫn hoặc JSON array
    @Column(name = "file_dinh_kem", length = 2000)
    private String fileDinhKem;
}
