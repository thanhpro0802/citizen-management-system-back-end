package com.citizen.management.citizen_management_system_back_end.dto.request;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HoKhauRequest {
    private String diaChi;

    // Object chứa thông tin chủ hộ (để tìm hoặc cập nhật chủ hộ)
    private ChuHoRequest chuHo;

    // --- THÊM MỚI: Danh sách thành viên cần cập nhật quan hệ ---
    private List<ThanhVienRequest> danhSachThanhVien;

    @Getter
    @Setter
    public static class ChuHoRequest {
        private String soCCCD;
    }

    // Class con để hứng dữ liệu từng thành viên
    @Getter
    @Setter
    public static class ThanhVienRequest {
        private String maNhanKhau;     // ID nhân khẩu
        private String quanHeVoiChuHo; // Quan hệ mới (Vợ, Con,...)
    }
}