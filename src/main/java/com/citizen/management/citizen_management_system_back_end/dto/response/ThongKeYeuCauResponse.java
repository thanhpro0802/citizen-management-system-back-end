package com.citizen.management.citizen_management_system_back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO cho thống kê yêu cầu cư trú
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThongKeYeuCauResponse {
    private long choXuLy;
    private long dangXuLy;
    private long daPheDuyet;
    private long tuChoi;
}
