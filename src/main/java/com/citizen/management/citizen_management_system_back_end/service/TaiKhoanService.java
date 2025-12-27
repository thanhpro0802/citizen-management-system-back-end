package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.enums.EnumVaiTro;
import com.citizen.management.citizen_management_system_back_end.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaiKhoanService {
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    public List<TaiKhoan> getDanhSachCanBo() {
        return taiKhoanRepository.findByVaiTro(EnumVaiTro.CAN_BO);
    }

    public List<TaiKhoan> getAll() {
        return taiKhoanRepository.findAll();
    }
}
