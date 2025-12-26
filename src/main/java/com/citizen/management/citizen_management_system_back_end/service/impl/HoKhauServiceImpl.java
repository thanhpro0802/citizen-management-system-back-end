package com.citizen.management.citizen_management_system_back_end.service.impl;

import com.citizen.management.citizen_management_system_back_end.dto.request.NhapHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.TachHoRequest;
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
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HoKhauServiceImpl implements HoKhauService {

    private final HoKhauRepository hoKhauRepository;
    private final NhanKhauRepository nhanKhauRepository;

    @Override
    @Transactional
    public HoKhau taoMoi(HoKhau hoKhau) {
        // 1. Lưu Hộ khẩu
        HoKhau hoKhauMoi = hoKhauRepository.save(hoKhau);

        // 2. Tìm và set Chủ Hộ
        if (hoKhau.getChuHo() != null) {
            NhanKhau chuHo = null;
            if (hoKhau.getChuHo().getMaNhanKhau() != null && !hoKhau.getChuHo().getMaNhanKhau().isEmpty()) {
                chuHo = nhanKhauRepository.findById(hoKhau.getChuHo().getMaNhanKhau()).orElse(null);
            } else if (hoKhau.getChuHo().getSoCCCD() != null && !hoKhau.getChuHo().getSoCCCD().isEmpty()) {
                chuHo = nhanKhauRepository.findBySoCCCD(hoKhau.getChuHo().getSoCCCD()).orElse(null);
            }

            if (chuHo == null) {
                throw new RuntimeException("Không tìm thấy chủ hộ (kiểm tra lại UUID hoặc CCCD)");
            }

            chuHo.setHoKhau(hoKhauMoi);
            chuHo.setQuanHeVoiChuHo("Chủ hộ");
            nhanKhauRepository.save(chuHo);
            hoKhauMoi.setChuHo(chuHo);
        }

        // 3. Cập nhật thành viên
        if (hoKhau.getDanhSachThanhVien() != null) {
            for (NhanKhau nkRequest : hoKhau.getDanhSachThanhVien()) {
                NhanKhau thanhVienDb = nhanKhauRepository.findById(nkRequest.getMaNhanKhau()).orElse(null);
                if (thanhVienDb != null) {
                    thanhVienDb.setHoKhau(hoKhauMoi);
                    thanhVienDb.setQuanHeVoiChuHo(nkRequest.getQuanHeVoiChuHo());
                    nhanKhauRepository.save(thanhVienDb);
                }
            }
        }
        return layTheoId(hoKhauMoi.getMaHoKhau());
    }

    @Override
    @Transactional
    public HoKhau capNhat(String maHoKhau, HoKhau hoKhauSua) {
        HoKhau hkHienTai = hoKhauRepository.findById(maHoKhau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hộ khẩu với mã: " + maHoKhau));

        // 1. Cập nhật thông tin cơ bản
        hkHienTai.setDiaChi(hoKhauSua.getDiaChi());
        hkHienTai.setNgayDangKy(hoKhauSua.getNgayDangKy());

        // Snapshot danh sách ID mới để biết ai được giữ lại
        Set<String> idThanhVienMoi = hoKhauSua.getDanhSachThanhVien().stream()
                .map(NhanKhau::getMaNhanKhau)
                .collect(Collectors.toSet());

        // Nếu có chủ hộ mới trong request, giữ lại ID đó
        if (hoKhauSua.getChuHo() != null && hoKhauSua.getChuHo().getSoCCCD() != null) {
            NhanKhau chuHoMoiDb = nhanKhauRepository.findBySoCCCD(hoKhauSua.getChuHo().getSoCCCD()).orElse(null);
            if (chuHoMoiDb != null) {
                idThanhVienMoi.add(chuHoMoiDb.getMaNhanKhau());
            }
        }

        // 2. DỌN DẸP: Xóa những người không còn trong danh sách
        List<NhanKhau> thanhVienHienTai = hkHienTai.getDanhSachThanhVien();
        if (hkHienTai.getChuHo() != null) {
            thanhVienHienTai.add(hkHienTai.getChuHo());
        }

        for (NhanKhau nkCu : thanhVienHienTai) {
            if (!idThanhVienMoi.contains(nkCu.getMaNhanKhau())) {
                nkCu.setHoKhau(null);
                nkCu.setQuanHeVoiChuHo(null);
                nhanKhauRepository.save(nkCu);
            }
        }

        // 3. Xử lý đổi Chủ Hộ
        if (hoKhauSua.getChuHo() != null) {
            String cccdMoi = hoKhauSua.getChuHo().getSoCCCD();
            NhanKhau chuHoMoiDb = nhanKhauRepository.findBySoCCCD(cccdMoi)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy chủ hộ mới với CCCD: " + cccdMoi));

            if (hkHienTai.getChuHo() == null || !chuHoMoiDb.getMaNhanKhau().equals(hkHienTai.getChuHo().getMaNhanKhau())) {
                // Xử lý chủ hộ CŨ -> xuống làm thành viên
                if (hkHienTai.getChuHo() != null) {
                    NhanKhau chuHoCu = hkHienTai.getChuHo();
                    if (idThanhVienMoi.contains(chuHoCu.getMaNhanKhau())) {
                        chuHoCu.setQuanHeVoiChuHo("Thành viên");
                        nhanKhauRepository.save(chuHoCu);
                    }
                }
                // Xử lý chủ hộ MỚI
                chuHoMoiDb.setHoKhau(hkHienTai);
                chuHoMoiDb.setQuanHeVoiChuHo("Chủ hộ");
                nhanKhauRepository.save(chuHoMoiDb);
                hkHienTai.setChuHo(chuHoMoiDb);
            }
        }

        // 4. Cập nhật thành viên còn lại
        if (hoKhauSua.getDanhSachThanhVien() != null) {
            for (NhanKhau nkRequest : hoKhauSua.getDanhSachThanhVien()) {
                NhanKhau thanhVienDb = nhanKhauRepository.findById(nkRequest.getMaNhanKhau()).orElse(null);
                if (thanhVienDb != null) {
                    thanhVienDb.setHoKhau(hkHienTai);
                    // Nếu là chủ hộ thì không đổi quan hệ
                    if (hkHienTai.getChuHo() != null && thanhVienDb.getMaNhanKhau().equals(hkHienTai.getChuHo().getMaNhanKhau())) {
                        thanhVienDb.setQuanHeVoiChuHo("Chủ hộ");
                    } else {
                        thanhVienDb.setQuanHeVoiChuHo(nkRequest.getQuanHeVoiChuHo());
                    }
                    nhanKhauRepository.save(thanhVienDb);
                }
            }
        }
        return hoKhauRepository.save(hkHienTai);
    }

    @Override
    @Transactional
    public HoKhau tachHo(String maHoCu, TachHoRequest request) {
        HoKhau hoCu = hoKhauRepository.findById(maHoCu)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hộ khẩu cũ: " + maHoCu));

        NhanKhau chuHoMoi = nhanKhauRepository.findBySoCCCD(request.getCccdChuHoMoi())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chủ hộ mới (CCCD: " + request.getCccdChuHoMoi() + ")"));

        List<String> cccdTachRa = request.getCccdNhanKhauTachRa();
        List<NhanKhau> nhanKhauTachRa = new ArrayList<>();

        for(String cccd : cccdTachRa) {
            NhanKhau nk = nhanKhauRepository.findBySoCCCD(cccd)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu tách (CCCD: " + cccd + ")"));
            if (nk.getHoKhau() == null || !nk.getHoKhau().getMaHoKhau().equals(maHoCu)) {
                throw new RuntimeException("Nhân khẩu " + cccd + " không thuộc hộ khẩu cũ.");
            }
            nhanKhauTachRa.add(nk);
        }

        HoKhau hoMoi = new HoKhau();
        hoMoi.setChuHo(chuHoMoi);
        hoMoi.setDiaChi(request.getDiaChiMoi());
        hoMoi.setNgayDangKy(null);
        hoKhauRepository.save(hoMoi);

        chuHoMoi.setHoKhau(hoMoi);
        chuHoMoi.setQuanHeVoiChuHo("Chủ hộ");
        nhanKhauRepository.save(chuHoMoi);

        for (NhanKhau nk : nhanKhauTachRa) {
            nk.setHoKhau(hoMoi);
            nk.setQuanHeVoiChuHo("Thành viên");
            nhanKhauRepository.save(nk);
            hoMoi.addThanhVien(nk);
        }

        hoKhauRepository.save(hoCu);
        return hoMoi;
    }

    @Override
    @Transactional
    public HoKhau nhapHo(String maHoNhapVao, NhapHoRequest request) {
        // 1. Lấy hộ khẩu đích (Hộ B)
        HoKhau hoNhapVao = hoKhauRepository.findById(maHoNhapVao)
                .orElseThrow(() -> new RuntimeException("Hộ khẩu nhập vào không tồn tại"));

        // Set lưu ID các hộ cũ bị ảnh hưởng
        Set<String> danhSachMaHoCu = new HashSet<>();

        if (request.getDanhSachNhanKhau() != null) {
            for (NhapHoRequest.ThanhVienNhapHo item : request.getDanhSachNhanKhau()) {
                String cccd = item.getCccd();
                String quanHe = item.getQuanHeVoiChuHo();

                // Tìm nhân khẩu
                NhanKhau nk = nhanKhauRepository.findBySoCCCD(cccd)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu (CCCD: " + cccd + ")"));

                HoKhau hoKhauCu = nk.getHoKhau();

                // --- XỬ LÝ HỘ CŨ (HỘ A) TRƯỚC KHI CHUYỂN ---
                if (hoKhauCu != null && !hoKhauCu.getMaHoKhau().equals(maHoNhapVao)) {
                    danhSachMaHoCu.add(hoKhauCu.getMaHoKhau());

                    // Nếu là chủ hộ cũ -> Gỡ chức chủ hộ ngay
                    if (hoKhauCu.getChuHo() != null && hoKhauCu.getChuHo().getMaNhanKhau().equals(nk.getMaNhanKhau())) {
                        hoKhauCu.setChuHo(null);
                        hoKhauRepository.save(hoKhauCu);
                    }
                }

                // --- CHUYỂN SANG HỘ MỚI ---
                nk.setHoKhau(hoNhapVao);
                nk.setQuanHeVoiChuHo( (quanHe != null && !quanHe.isBlank()) ? quanHe : "Thành viên" );

                nhanKhauRepository.save(nk);

                // Quan trọng: Thêm vào list Java của hộ mới để đồng bộ
                hoNhapVao.addThanhVien(nk);
            }
        }

        // Lưu hộ mới
        HoKhau hoSauKhiNhap = hoKhauRepository.save(hoNhapVao);

        // --- BƯỚC QUAN TRỌNG: FLUSH VÀ XÓA HỘ CŨ ---
        // Ép Hibernate đẩy mọi thay đổi xuống DB để lệnh count bên dưới chính xác
        nhanKhauRepository.flush();

        for (String maHoCu : danhSachMaHoCu) {
            HoKhau hkCu = hoKhauRepository.findById(maHoCu).orElse(null);
            if (hkCu != null) {
                // Hỏi trực tiếp Database: Hộ này còn bao nhiêu người?
                long soThanhVienConLai = nhanKhauRepository.countByHoKhau(hkCu);

                // Nếu không còn chủ hộ VÀ Database bảo là 0 người -> XÓA
                if (hkCu.getChuHo() == null && soThanhVienConLai == 0) {
                    hoKhauRepository.delete(hkCu);
                }
            }
        }

        return hoSauKhiNhap;
    }
    @Override
    public void xoa(String maHoKhau) {
        hoKhauRepository.deleteById(maHoKhau);
    }
    @Override
    public List<HoKhau> layTatCa() { return hoKhauRepository.findAll(); }
    @Override
    public HoKhau layTheoId(String maHoKhau) { return hoKhauRepository.findById(maHoKhau).orElse(null); }
    @Override
    public Long getCountHoKhau(String diaChi) { return diaChi != null ? hoKhauRepository.countByDiaChi(diaChi) : hoKhauRepository.count(); }
    @Override
    public HoKhau xemHoKhauCuaToi(String username) {
        NhanKhau nhanKhau = nhanKhauRepository.findByTaiKhoan_Cccd(username).orElseThrow(()->new RuntimeException("Not found"));
        return nhanKhau.getHoKhau();
    }
}