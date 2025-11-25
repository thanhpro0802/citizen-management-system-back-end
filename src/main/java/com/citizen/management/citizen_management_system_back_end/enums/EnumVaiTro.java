package com.citizen.management.citizen_management_system_back_end.enums;

/**
 * Enum này định nghĩa các vai trò (quyền)
 * trong hệ thống.
 */
public enum EnumVaiTro {
    /**
     * Người dân: Chỉ có quyền cơ bản như gửi phản ánh,
     * xem thông tin của bản thân.
     */
    CONG_DAN,

    /**
     * Cán bộ: Có các quyền nghiệp vụ như xử lý
     * phản ánh, cập nhật nhân khẩu, hộ khẩu...
     */
    CAN_BO,

    /**
     * Quản trị viên: Quyền cao nhất, quản lý tài
     * khoản cán bộ, cấu hình hệ thống.
     */
    ADMIN
}