package com.citizen.management.citizen_management_system_back_end.dto;

import lombok.Data;

import java.util.List;

@Data
public class GuiPhanAnhRequest {
    private String tieuDe;
    private String noiDung;
    private String linhVuc;
    private List<String> danhSachFileUrl; // Nhan tu Cloudinary
}
