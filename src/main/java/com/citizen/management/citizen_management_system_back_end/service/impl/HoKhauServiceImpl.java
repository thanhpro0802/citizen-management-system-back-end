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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
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
    @Transactional
    public HoKhau tachHo(Long idHoCu, TachHoRequest request) {
        HoKhau hoCu = hoKhauRepository.findById(idHoCu)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hộ khẩu với ID: " + idHoCu));

        // Lấy danh sách ID cần tách
        List<Long> tachRaIds = request.getIdNhanKhauTachRa();

        // Kiểm tra các nhân khẩu này có thực sự thuộc về hoCu
        List<NhanKhau> thanhVienCu = hoCu.getDanhSachThanhVien();
        Set<Long> idLienQuan = thanhVienCu.stream().map(NhanKhau::getId).collect(Collectors.toSet());

        for (Long id : tachRaIds) {
            if (!idLienQuan.contains(id)) {
                throw new RuntimeException("Nhân khẩu id=" + id + " không thuộc hộ khẩu id=" + idHoCu);
            }
        }

        // Tạo hộ khẩu mới
        HoKhau hoMoi = new HoKhau();
        NhanKhau chuHoMoi = nhanKhauRepository.findById(request.getIdChuHoMoi())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với ID: " + request.getIdChuHoMoi()));
        hoMoi.setTenChuHo(chuHoMoi.getHoTen());
        hoMoi.setDiaChi(request.getDiaChiMoi());
        hoMoi.setDanhSachThanhVien(new ArrayList<>()); // bắt đầu với danh sách rỗng

        hoKhauRepository.save(hoMoi); // lưu để có ID nếu cần dùng

        // Tách các nhân khẩu ra, đồng thời cập nhật hoKhau mới cho họ
        List<NhanKhau> nhanKhausTachRa = nhanKhauRepository.findAllById(tachRaIds);
        for (NhanKhau nk : nhanKhausTachRa) {
            nk.setHoKhau(hoMoi);  // cập nhật trường tham chiếu ngược
            nhanKhauRepository.save(nk);
            hoMoi.getDanhSachThanhVien().add(nk); // thêm vào danh sách thành viên hộ mới
        }

        // Loại bỏ các nhân khẩu đã tách khỏi hộ cũ (cập nhật danh sách thành viên)
        List<NhanKhau> thanhVienCuMoi = thanhVienCu.stream()
                .filter(nk -> !tachRaIds.contains(nk.getId()))
                .collect(Collectors.toList());
        hoCu.setDanhSachThanhVien(thanhVienCuMoi);
        hoKhauRepository.save(hoCu);
        hoKhauRepository.save(hoMoi); // Lưu lại hộ mới với danh sách thành viên mới

        return hoMoi;
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