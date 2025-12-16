package com.citizen.management.citizen_management_system_back_end.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class PhanCongRequest {
    private String maCanBoPhuTrach;
    private Date thoiHanXuLy;
}
