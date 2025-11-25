package com.citizen.management.citizen_management_system_back_end.dto;

import lombok.Data;

import java.util.List;

@Data
public class XuLyNoiBoRequest {
    //Ghi chu noi bo
    private String noiDungCapNhat;

    //Danh sach cac URL file da tai len
    private List<String> danhSachFileUrl;
}
