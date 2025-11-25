package com.citizen.management.citizen_management_system_back_end.enums;

/**
 * EnumHanhDong định nghĩa các hành động có thể thực hiện trong hệ thống quản lý công dân.
 * <p>
 * Mỗi giá trị đại diện cho một hành động cụ thể trong quy trình xử lý.
 */
public enum EnumHanhDong {
    /**
     * Tạo mới: Hành động khởi tạo hoặc tạo mới một đối tượng hoặc yêu cầu trong hệ thống.
     */
    TAO_MOI,
    /**
     * Phân công: Hành động giao nhiệm vụ hoặc chuyển giao công việc cho cá nhân hoặc bộ phận khác.
     */
    PHAN_CONG,
    /**
     * Xử lý: Hành động thực hiện xử lý, giải quyết hoặc thao tác trên đối tượng hoặc yêu cầu.
     */
    XU_LY,
    /**
     * Phản hồi: Hành động trả lời, cung cấp thông tin phản hồi hoặc xác nhận kết quả xử lý.
     */
    PHAN_HOI
}
