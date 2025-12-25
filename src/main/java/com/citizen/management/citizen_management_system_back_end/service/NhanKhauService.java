package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface NhanKhauService {
    NhanKhauDto create(NhanKhauDto dto);
    NhanKhauDto update(String maNhanKhau, NhanKhauDto dto);
    void delete(String maNhanKhau);
    NhanKhauDto getById(String maNhanKhau);
    Page<NhanKhauDto> search(SearchNhanKhauCriteria criteria, Pageable pageable);

    TamTruDto registerTamTru(TamTruDto dto);
    TamVangDto registerTamVang(TamVangDto dto);
    NhanKhauDto declareDeath(String maNhanKhau); // cập nhật status = KHAI_TU
    
    // Lấy thông tin nhân khẩu và danh sách thành viên cùng hộ khẩu
    Map<String, Object> layThongTinNhanKhauVaHoKhau(String maNhanKhau);
}
