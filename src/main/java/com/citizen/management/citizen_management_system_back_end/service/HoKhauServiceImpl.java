package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.dto.DoiChuHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.NhapHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.TachHoRequest;
import com.citizen.management.citizen_management_system_back_end.model.HoKhau;
import com.citizen.management.citizen_management_system_back_end.model.NhanKhau;
import com.citizen.management.citizen_management_system_back_end.repository.HoKhauRepository;
import com.citizen.management.citizen_management_system_back_end.repository.NhanKhauRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HoKhauServiceImpl implements HoKhauService {

    @Autowired
    private HoKhauRepository hoKhauRepository;

    @Autowired
    private NhanKhauRepository nhanKhauRepository;

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

    @Override
    public HoKhau tachHo(Long idHoCu, TachHoRequest request) {
        HoKhau hoCu = hoKhauRepository.findById(idHoCu)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hộ khẩu với ID: " + idHoCu));

        // Tạo hộ khẩu mới
        HoKhau hoMoi = new HoKhau();
        NhanKhau chuHoMoi = nhanKhauRepository.findById(request.getIdChuHoMoi())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với ID: " + request.getIdChuHoMoi()));
        hoMoi.setTenChuHo(chuHoMoi.getHoTen());
        hoMoi.setDiaChi(request.getDiaChiMoi());

        // Lấy danh sách đối tượng NhanKhau dựa vào id
        List<NhanKhau> nhanKhausTachRa = nhanKhauRepository.findAllById(request.getIdNhanKhauTachRa());
        hoMoi.setDanhSachThanhVien(nhanKhausTachRa);

        // Loại bỏ các nhân khẩu đã tách khỏi hộ cũ
        List<NhanKhau> thanhVienCu = hoCu.getDanhSachThanhVien();
        List<Long> tachRaIds = request.getIdNhanKhauTachRa();
        thanhVienCu.removeIf(nk -> tachRaIds.contains(nk.getId()));
        hoCu.setDanhSachThanhVien(thanhVienCu);

        hoKhauRepository.save(hoCu);
        HoKhau hoKhauMoi = hoKhauRepository.save(hoMoi);
        return hoKhauMoi;
    }

    @Override
    public HoKhau nhapHo(Long idHoNhapVao, NhapHoRequest request) {
        HoKhau hoNhapVao = hoKhauRepository.findById(idHoNhapVao)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hộ khẩu với ID: " + idHoNhapVao));

        // Lấy danh sách đối tượng NhanKhau cần nhập
        List<NhanKhau> nhanKhauNhapVao = nhanKhauRepository.findAllById(request.getIdNhanKhauNhapVao());
        List<NhanKhau> dsThanhVien = hoNhapVao.getDanhSachThanhVien();
        dsThanhVien.addAll(nhanKhauNhapVao);
        hoNhapVao.setDanhSachThanhVien(dsThanhVien);

        // Bạn nên cập nhật hộ gốc của các nhân khẩu nhập vào (ngoài scope ở đây)
        return hoKhauRepository.save(hoNhapVao);
    }

    @Override
    public HoKhau doiChuHo(Long idHoKhau, DoiChuHoRequest request) {
        HoKhau hk = hoKhauRepository.findById(idHoKhau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hộ khẩu với ID: " + idHoKhau));
        NhanKhau chuHoMoi = nhanKhauRepository.findById(request.getIdNhanKhauMoi())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với ID: " + request.getIdNhanKhauMoi()));
        hk.setTenChuHo(chuHoMoi.getHoTen()); // giả sử có trường hoTen bên NhanKhau
        return hoKhauRepository.save(hk);
    }
}