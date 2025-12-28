package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.enums.EnumVaiTro;
import com.citizen.management.citizen_management_system_back_end.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TaiKhoanService {
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    public List<TaiKhoan> getDanhSachCanBo() {
        // Định nghĩa danh sách các vai trò được coi là "Cán bộ"
        List<EnumVaiTro> rolesCanBo = Arrays.asList(
                EnumVaiTro.CAN_BO_HO_KHAU,
                EnumVaiTro.CAN_BO_NHAN_KHAU,
                EnumVaiTro.CAN_BO_PHAN_ANH,
                EnumVaiTro.TO_TRUONG,
                EnumVaiTro.TO_PHO,
                EnumVaiTro.ADMIN // Thường Admin cũng nằm trong danh sách quản lý
        );

        // Gọi hàm tìm kiếm theo danh sách (Cần thêm vào Repository như hướng dẫn trên)
        return taiKhoanRepository.findByVaiTroIn(rolesCanBo);
    }

    public List<TaiKhoan> getAll() {
        return taiKhoanRepository.findAll();
    }
}
