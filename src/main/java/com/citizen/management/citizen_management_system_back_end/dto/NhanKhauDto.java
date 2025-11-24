package com.citizen.management.citizen_management_system_back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class NhanKhauDto {
    private Long id;
    private String hoTen;
    private String gioiTinh;
}
