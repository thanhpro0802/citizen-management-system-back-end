package com.citizen.management.citizen_management_system_back_end.enums;

/**
 * EnumTrangThai đại diện cho các trạng thái xử lý của một yêu cầu hoặc tiến trình trong hệ thống.
 * <p>
 * Các trạng thái bao gồm:
 * <ul>
 *   <li>{@link #CHO} - Đang chờ xử lý</li>
 *   <li>{@link #DANG_XU_LY} - Đang được xử lý</li>
 *   <li>{@link #DA_XU_LY} - Đã được xử lý xong</li>
 * </ul>
 */
public enum EnumTrangThai {
    /**
     * Trạng thái chờ xử lý. Yêu cầu hoặc tiến trình đang ở hàng đợi và chưa được xử lý.
     */
    CHO,
    /**
     * Trạng thái đang xử lý. Yêu cầu hoặc tiến trình đang được xử lý bởi hệ thống hoặc người dùng.
     */
    DANG_XU_LY,
    /**
     * Trạng thái đã xử lý. Yêu cầu hoặc tiến trình đã hoàn thành việc xử lý.
     */
    DA_XU_LY
}
