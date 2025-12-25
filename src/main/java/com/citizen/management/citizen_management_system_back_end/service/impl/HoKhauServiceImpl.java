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

        hkHienTai.setDiaChi(hoKhauSua.getDiaChi());
        hkHienTai.setNgayDangKy(hoKhauSua.getNgayDangKy());

        // [LOGIC ĐỔI CHỦ HỘ BẰNG CCCD HOẶC ID]
        if (hoKhauSua.getChuHo() != null) {
            String cccdMoi = hoKhauSua.getChuHo().getSoCCCD();
            String idMoi = hoKhauSua.getChuHo().getMaNhanKhau();

            NhanKhau chuHoMoiDb = null;
            if (idMoi != null && !idMoi.isEmpty()) {
                chuHoMoiDb = nhanKhauRepository.findById(idMoi).orElse(null);
            } else if (cccdMoi != null && !cccdMoi.isEmpty()) {
                chuHoMoiDb = nhanKhauRepository.findBySoCCCD(cccdMoi).orElse(null);
            }

            if (chuHoMoiDb != null) {
                // Nếu người này khác chủ hộ hiện tại
                String currentOwnerId = hkHienTai.getChuHo() != null ? hkHienTai.getChuHo().getMaNhanKhau() : "";
                if (!chuHoMoiDb.getMaNhanKhau().equals(currentOwnerId)) {
                    // Set chủ hộ mới
                    chuHoMoiDb.setHoKhau(hkHienTai);
                    chuHoMoiDb.setQuanHeVoiChuHo("Chủ hộ");
                    nhanKhauRepository.save(chuHoMoiDb);
                    hkHienTai.setChuHo(chuHoMoiDb);
                }
            } else {
                // Nếu frontend gửi lên mà không tìm thấy thì có thể ném lỗi hoặc bỏ qua tùy nghiệp vụ
                // throw new RuntimeException("Không tìm thấy chủ hộ mới");
            }
        }

        // Cập nhật danh sách thành viên
        if (hoKhauSua.getDanhSachThanhVien() != null) {
            for (NhanKhau nkRequest : hoKhauSua.getDanhSachThanhVien()) {
                NhanKhau thanhVienDb = nhanKhauRepository.findById(nkRequest.getMaNhanKhau()).orElse(null);
                if (thanhVienDb != null) {
                    thanhVienDb.setHoKhau(hkHienTai);
                    thanhVienDb.setQuanHeVoiChuHo(nkRequest.getQuanHeVoiChuHo());
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