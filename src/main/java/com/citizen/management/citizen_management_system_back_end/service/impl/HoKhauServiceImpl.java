package com.citizen.management.citizen_management_system_back_end.service.impl;

import com.citizen.management.citizen_management_system_back_end.dto.DoiChuHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.NhapHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.TachHoRequest;
import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;
import com.citizen.management.citizen_management_system_back_end.repository.HoKhauRepository;
import com.citizen.management.citizen_management_system_back_end.repository.NhanKhauRepository;
import com.citizen.management.citizen_management_system_back_end.service.HoKhauService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HoKhauServiceImpl implements HoKhauService {

    private final HoKhauRepository hoKhauRepository;
    private final NhanKhauRepository nhanKhauRepository;

    @Override
    @Transactional
    public HoKhau taoMoi(HoKhau hoKhau) {
        return hoKhauRepository.save(hoKhau);
    }

    @Override
    @Transactional
    public HoKhau capNhat(String maHoKhau, HoKhau hoKhauSua) {
        Optional<HoKhau> optional = hoKhauRepository.findById(maHoKhau);
        if (optional.isPresent()) {
            HoKhau hk = optional.get();
            hk.setDiaChi(hoKhauSua.getDiaChi());
            hk.setNgayDangKy(hoKhauSua.getNgayDangKy());
            hk.setChuHo(hoKhauSua.getChuHo());
            hk.setDanhSachThanhVien(hoKhauSua.getDanhSachThanhVien());
            return hoKhauRepository.save(hk);
        }
        throw new RuntimeException("Không tìm thấy hộ khẩu với mã: " + maHoKhau);
    }

    @Override
    @Transactional
    public void xoa(String maHoKhau) {
        hoKhauRepository.deleteById(maHoKhau);
    }

    @Override
    public List<HoKhau> layTatCa() {
        return hoKhauRepository.findAll();
    }

    @Override
    public HoKhau layTheoId(String maHoKhau) {
        return hoKhauRepository.findById(maHoKhau).orElse(null);
    }

    @Override
    @Transactional
    public HoKhau tachHo(String maHoCu, TachHoRequest request) {
        HoKhau hoCu = hoKhauRepository.findById(maHoCu)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hộ khẩu với mã: " + maHoCu));

        // Lấy danh sách mã nhân khẩu cần tách (UUID)
        List<String> tachRaIds = request.getMaNhanKhauTachRa();

        // Kiểm tra các nhân khẩu này có thực sự thuộc về hoCu
        List<NhanKhau> thanhVienCu = hoCu.getDanhSachThanhVien();
        Set<String> idLienQuan = thanhVienCu.stream().map(NhanKhau::getMaNhanKhau).collect(Collectors.toSet());

        for (String id : tachRaIds) {
            if (!idLienQuan.contains(id)) {
                throw new RuntimeException("Nhân khẩu id=" + id + " không thuộc hộ khẩu mã=" + maHoCu);
            }
        }

        // Tạo hộ khẩu mới
        HoKhau hoMoi = new HoKhau();
        NhanKhau chuHoMoi = nhanKhauRepository.findById(request.getMaNhanKhauChuHoMoi())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với mã: " + request.getMaNhanKhauChuHoMoi()));
        hoMoi.setChuHo(chuHoMoi);
        hoMoi.setDiaChi(request.getDiaChiMoi());
        hoMoi.setNgayDangKy(null); // hoặc có thể lấy ngày hiện tại/new từ request
        hoMoi.setDanhSachThanhVien(new ArrayList<>());

        hoKhauRepository.save(hoMoi);

        List<NhanKhau> nhanKhausTachRa = nhanKhauRepository.findAllById(tachRaIds);
        for (NhanKhau nk : nhanKhausTachRa) {
            nk.setHoKhau(hoMoi);
            nhanKhauRepository.save(nk);
            hoMoi.addThanhVien(nk);
        }

        // Loại bỏ các nhân khẩu đã tách khỏi hộ cũ
        List<NhanKhau> thanhVienCuMoi = thanhVienCu.stream()
                .filter(nk -> !tachRaIds.contains(nk.getMaNhanKhau()))
                .collect(Collectors.toList());
        hoCu.setDanhSachThanhVien(thanhVienCuMoi);
        hoKhauRepository.save(hoCu);
        hoKhauRepository.save(hoMoi);

        return hoMoi;
    }

    @Override
    @Transactional
    public HoKhau nhapHo(String maHoNhapVao, NhapHoRequest request) {
        HoKhau hoNhapVao = hoKhauRepository.findById(maHoNhapVao)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hộ khẩu với mã: " + maHoNhapVao));

        List<String> maNhanKhauNhapVao = request.getMaNhanKhauNhapVao();
        List<NhanKhau> nhanKhauNhapVao = nhanKhauRepository.findAllById(maNhanKhauNhapVao);
        for (NhanKhau nk : nhanKhauNhapVao) {
            nk.setHoKhau(hoNhapVao);
            hoNhapVao.addThanhVien(nk);
        }

        return hoKhauRepository.save(hoNhapVao);
    }

    @Override
    @Transactional
    public HoKhau doiChuHo(String maHoKhau, DoiChuHoRequest request) {
        HoKhau hk = hoKhauRepository.findById(maHoKhau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hộ khẩu với mã: " + maHoKhau));
        NhanKhau chuHoMoi = nhanKhauRepository.findById(request.getMaNhanKhauMoi())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với mã: " + request.getMaNhanKhauMoi()));
        hk.setChuHo(chuHoMoi);
        return hoKhauRepository.save(hk);
    }
}