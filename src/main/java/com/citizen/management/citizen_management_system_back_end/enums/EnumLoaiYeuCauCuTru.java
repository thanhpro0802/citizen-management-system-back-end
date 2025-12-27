package com.citizen.management.citizen_management_system_back_end.enums;

/**
 * Enum cho các loại yêu cầu cư trú
 */
public enum EnumLoaiYeuCauCuTru {
    DANG_KY_TAM_TRU("Đăng ký tạm trú", "Đăng ký tạm trú cho những người ở tạm thời tại địa phương (dưới 12 tháng)"),
    DANG_KY_THUONG_TRU("Đăng ký thường trú", "Đăng ký thường trú để được cấp hộ khẩu thường trú tại địa phương"),
    KHAI_BAO_TAM_VANG("Khai báo tạm vắng", "Khai báo khi rời khỏi nơi cư trú thường xuyên trong thời gian dài"),
    DIEU_CHINH_THONG_TIN("Điều chỉnh thông tin", "Điều chỉnh, cập nhật thông tin cư trú đã đăng ký trước đó"),
    XOA_DANG_KY("Xóa đăng ký", "Xóa đăng ký thường trú hoặc tạm trú khi chuyển đi nơi khác");
    
    private final String tenHienThi;
    private final String moTa;
    
    EnumLoaiYeuCauCuTru(String tenHienThi, String moTa) {
        this.tenHienThi = tenHienThi;
        this.moTa = moTa;
    }
    
    public String getTenHienThi() {
        return tenHienThi;
    }
    
    public String getMoTa() {
        return moTa;
    }
}
