package com.citizen.management.citizen_management_system_back_end.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class NhapHoRequest {
    private String maHoNhapVao; // ID hộ khẩu đích

    // Thay đổi cấu trúc: Nhận một danh sách đối tượng thay vì list string đơn thuần
    private List<ThanhVienNhapHo> danhSachNhanKhau;

    // Inner Class để hứng dữ liệu từng cặp (CCCD + Quan hệ)
    @Getter
    @Setter
    public static class ThanhVienNhapHo {
        private String cccd;
        private String quanHeVoiChuHo;
    }

    public String getMaHoNhapVao() {
        return maHoNhapVao;
    }

    public List<ThanhVienNhapHo> getDanhSachNhanKhau() {
        return danhSachNhanKhau;
    }

    public void setMaHoNhapVao(String maHoNhapVao) {
        this.maHoNhapVao = maHoNhapVao;
    }

    public void setDanhSachNhanKhau(List<ThanhVienNhapHo> danhSachNhanKhau) {
        this.danhSachNhanKhau = danhSachNhanKhau;
    }
}