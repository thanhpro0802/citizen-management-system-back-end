package com.citizen.management.citizen_management_system_back_end.dto.projection;

import java.time.LocalDate;

public interface TamTruTamVangProjection {
    LocalDate getNgay();

    Long getBatDau();

    Long getKetThuc();
}