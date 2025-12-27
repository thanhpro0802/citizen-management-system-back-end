package com.citizen.management.citizen_management_system_back_end.dto.request;

import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThaiYeuCau;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO cho xử lý yêu cầu cư trú (dành cho cán bộ)
 */
@Getter
@Setter
public class XuLyYeuCauCuTruRequest {

    private EnumTrangThaiYeuCau trangThaiMoi;
    private String ghiChu;
    private String lyDoTuChoi;
}
