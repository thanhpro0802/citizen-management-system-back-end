package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.model.HoKhau;
import com.citizen.management.citizen_management_system_back_end.repository.HoKhauRepository;
import com.citizen.management.citizen_management_system_back_end.service.HoKhauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HoKhauServiceImpl implements HoKhauService {

    @Autowired
    private HoKhauRepository hoKhauRepository;

    @Override
    public HoKhau taoMoi(HoKhau hoKhau) {
        return hoKhauRepository.save(hoKhau);
    }

    @Override
    public HoKhau capNhat(Long id, HoKhau hoKhauSua) {
        Optional<HoKhau> optional = hoKhauRepository.findById(id);
        if (optional.isPresent()) {
            HoKhau hk = optional.get();
            hk.setSoHoKhau(hoKhauSua.getSoHoKhau());
            hk.setDiaChi(hoKhauSua.getDiaChi());
            hk.setTenChuHo(hoKhauSua.getTenChuHo());
            hk.setSoDienThoaiChuHo(hoKhauSua.getSoDienThoaiChuHo());
            hk.setDanhSachThanhVien(hoKhauSua.getDanhSachThanhVien());
            return hoKhauRepository.save(hk);
        }
        throw new RuntimeException("Không tìm thấy hộ khẩu với ID: " + id);
    }

    @Override
    public void xoa(Long id) {
        hoKhauRepository.deleteById(id);
    }

    @Override
    public List<HoKhau> layTatCa() {
        return hoKhauRepository.findAll();
    }

    @Override
    public HoKhau layTheoId(Long id) {
        return hoKhauRepository.findById(id).orElse(null);
    }
}