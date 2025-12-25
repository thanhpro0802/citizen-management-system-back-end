package com.citizen.management.citizen_management_system_back_end.service.impl;

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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Set;

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

        // 2. Tìm và set Chủ Hộ (nếu gửi CCCD hoặc ID)
        if (hoKhau.getChuHo() != null) {
            NhanKhau chuHo = null;
            // Ưu tiên tìm theo ID trước nếu có, nếu không tìm theo CCCD
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

            // Cập nhật lại tham chiếu chủ hộ chính xác trong hoKhauMoi
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

        // Trả về dữ liệu mới nhất từ DB
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

        // Danh sách các ID nhân khẩu sẽ ĐƯỢC GIỮ LẠI trong hộ này (bao gồm Chủ hộ + Thành viên)
        Set<String> idNhanKhauGiuLai = new HashSet<>();

        // 2. Xử lý Chủ Hộ
        if (hoKhauSua.getChuHo() != null) {
            String cccdMoi = hoKhauSua.getChuHo().getSoCCCD();
            // Tìm nhân khẩu chủ hộ mới
            NhanKhau chuHoMoiDb = nhanKhauRepository.findBySoCCCD(cccdMoi)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy chủ hộ mới với CCCD: " + cccdMoi));

            // Set chủ hộ mới
            if (!chuHoMoiDb.equals(hkHienTai.getChuHo())) {
                chuHoMoiDb.setHoKhau(hkHienTai);
                chuHoMoiDb.setQuanHeVoiChuHo("Chủ hộ");
                nhanKhauRepository.save(chuHoMoiDb);
                hkHienTai.setChuHo(chuHoMoiDb);
            }
            // Thêm ID chủ hộ vào danh sách giữ lại
            idNhanKhauGiuLai.add(chuHoMoiDb.getMaNhanKhau());
        }

        // 3. Xử lý Danh sách thành viên (Cập nhật quan hệ & Thu thập ID giữ lại)
        if (hoKhauSua.getDanhSachThanhVien() != null) {
            for (NhanKhau nkRequest : hoKhauSua.getDanhSachThanhVien()) {
                NhanKhau thanhVienDb = nhanKhauRepository.findById(nkRequest.getMaNhanKhau()).orElse(null);
                if (thanhVienDb != null) {
                    thanhVienDb.setHoKhau(hkHienTai);
                    thanhVienDb.setQuanHeVoiChuHo(nkRequest.getQuanHeVoiChuHo());
                    nhanKhauRepository.save(thanhVienDb);

                    // Thêm ID này vào danh sách giữ lại
                    idNhanKhauGiuLai.add(thanhVienDb.getMaNhanKhau());
                }
            }
        }

        // 4. [QUAN TRỌNG] XÓA NHỮNG NGƯỜI KHÔNG CÒN TRONG DANH SÁCH
        // Lấy danh sách thành viên hiện tại trong DB ra để kiểm tra
        // Lưu ý: hkHienTai.getDanhSachThanhVien() lấy từ DB lên
        List<NhanKhau> thanhVienCu = hkHienTai.getDanhSachThanhVien();
        for (NhanKhau nk : thanhVienCu) {
            // Nếu người này KHÔNG nằm trong danh sách giữ lại (tức là đã bị xóa ở Frontend)
            if (!idNhanKhauGiuLai.contains(nk.getMaNhanKhau())) {
                nk.setHoKhau(null); // Xóa khỏi hộ
                nk.setQuanHeVoiChuHo(null); // Reset quan hệ
                nhanKhauRepository.save(nk);
            }
        }

        return hoKhauRepository.save(hkHienTai);
    }

    @Override
    @Transactional
    public HoKhau tachHo(String maHoCu, TachHoRequest request) {
        HoKhau hoCu = hoKhauRepository.findById(maHoCu)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hộ khẩu cũ: " + maHoCu));

        // 1. Tìm chủ hộ mới bằng CCCD (Request mới dùng getCccdChuHoMoi)
        NhanKhau chuHoMoi = nhanKhauRepository.findBySoCCCD(request.getCccdChuHoMoi())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chủ hộ mới (CCCD: " + request.getCccdChuHoMoi() + ")"));

        // 2. Tìm danh sách tách bằng CCCD (Request mới dùng getCccdNhanKhauTachRa)
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

        // Tạo hộ mới
        HoKhau hoMoi = new HoKhau();
        hoMoi.setChuHo(chuHoMoi);
        hoMoi.setDiaChi(request.getDiaChiMoi());
        hoMoi.setNgayDangKy(null);
        hoKhauRepository.save(hoMoi);

        // Update Chủ hộ mới
        chuHoMoi.setHoKhau(hoMoi);
        chuHoMoi.setQuanHeVoiChuHo("Chủ hộ");
        nhanKhauRepository.save(chuHoMoi);

        // Update thành viên tách
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
        HoKhau hoNhapVao = hoKhauRepository.findById(maHoNhapVao)
                .orElseThrow(() -> new RuntimeException("Hộ khẩu không tồn tại"));

        // Request mới dùng getCccdNhanKhauNhapVao
        List<String> cccdList = request.getCccdNhanKhauNhapVao();
        for (String cccd : cccdList) {
            NhanKhau nk = nhanKhauRepository.findBySoCCCD(cccd)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu (CCCD: " + cccd + ")"));

            nk.setHoKhau(hoNhapVao);
            if(request.getQuanHeVoiChuHo() != null) {
                nk.setQuanHeVoiChuHo(request.getQuanHeVoiChuHo());
            } else {
                nk.setQuanHeVoiChuHo("Thành viên");
            }
            nhanKhauRepository.save(nk);
            hoNhapVao.addThanhVien(nk);
        }

        return hoKhauRepository.save(hoNhapVao);
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