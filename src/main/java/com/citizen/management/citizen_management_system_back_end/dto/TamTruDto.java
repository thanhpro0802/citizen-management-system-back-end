package com.citizen.management.citizen_management_system_back_end.dto;

import lombok.Data;
import java.util.Date;

@Data
public class TamTruDto {
    private String maTamTru;
    private String maNhanKhau;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String lyDo;
}
