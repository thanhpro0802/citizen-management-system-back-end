package com.citizen.management.citizen_management_system_back_end.service.impl;

import com.citizen.management.citizen_management_system_back_end.dto.request.DoiChuHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.NhapHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.TachHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.HoKhauRequest;
import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.repository.HoKhauRepository;
import com.citizen.management.citizen_management_system_back_end.repository.NhanKhauRepository;
import com.citizen.management.citizen_management_system_back_end.service.HoKhauService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class HoKhauServiceImpl implements HoKhauService {

    private final HoKhauRepository hoKhauRepository;
    private final NhanKhauRepository nhanKhauRepository;

    @Override
    @Transactional
<<<<<<< Updated upstream
    public HoKhau taoMoi(HoKhau hoKhau) {
        return hoKhauRepository.save(hoKhau);
=======
    public HoKhau taoMoi(HoKhauRequest request) { // <--- ĐÃ SỬA: Nhận HoKhauRequest
        HoKhau hoKhau = new HoKhau();

        // 1. Set thông tin cơ bản
        if (request.getDiaChi() == null || request.getDiaChi().trim().isEmpty()) {
            throw new RuntimeException("Địa chỉ không được để trống");
        }
        hoKhau.setDiaChi(request.getDiaChi());
        hoKhau.setNgayDangKy(new Date());

        // 2. Xử lý Chủ Hộ (Tìm theo CCCD)
        if (request.getChuHo() == null || request.getChuHo().getSoCCCD() == null) {
            throw new RuntimeException("Vui lòng nhập số CCCD của chủ hộ!");
        }

        String cccd = request.getChuHo().getSoCCCD().trim();

        // Tìm nhân khẩu trong DB
        NhanKhau chuHo = nhanKhauRepository.findBySoCCCD(cccd)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với CCCD: " + cccd));

        // Kiểm tra: Người này đã có hộ khẩu chưa?
        if (chuHo.getHoKhau() != null) {
            throw new RuntimeException("Công dân " + chuHo.getHoTen() + " hiện đã thuộc một hộ khẩu khác!");
        }

        // 3. Gán quan hệ 2 chiều
        hoKhau.setChuHo(chuHo);

        // Lưu hộ khẩu trước để có ID
        HoKhau hoKhauMoi = hoKhauRepository.save(hoKhau);

        // Cập nhật thông tin nhân khẩu
        chuHo.setQuanHeVoiChuHo("Chủ hộ"); // Sửa thành tiếng Việt có dấu hoặc CHU_HO tùy quy ước enum của bạn
        chuHo.setHoKhau(hoKhauMoi);

        // Thêm chủ hộ vào danh sách thành viên của hộ
        hoKhauMoi.addThanhVien(chuHo);

        return hoKhauRepository.save(hoKhauMoi);
>>>>>>> Stashed changes
    }

    @Override
    @Transactional
    public HoKhau update(String maHoKhau, HoKhauRequest request) {
        // 1. Tìm hộ khẩu
        HoKhau hoKhau = hoKhauRepository.findById(maHoKhau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hộ khẩu với mã: " + maHoKhau));

        // 2. Cập nhật Địa chỉ
        if (request.getDiaChi() != null && !request.getDiaChi().trim().isEmpty()) {
            hoKhau.setDiaChi(request.getDiaChi().trim());
        }

        // 3. Cập nhật Chủ hộ (Xử lý đổi người đứng tên)
        if (request.getChuHo() != null && request.getChuHo().getSoCCCD() != null) {
            String newCCCD = request.getChuHo().getSoCCCD().trim();
            // Nếu CCCD khác với chủ hộ hiện tại (hoặc chưa có chủ hộ)
            if (hoKhau.getChuHo() == null || !hoKhau.getChuHo().getSoCCCD().equals(newCCCD)) {
                NhanKhau chuHoMoi = nhanKhauRepository.findBySoCCCD(newCCCD)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với CCCD: " + newCCCD));

                // Xử lý chủ hộ cũ (nếu có) -> Chuyển thành thành viên thường
                if (hoKhau.getChuHo() != null) {
                    hoKhau.getChuHo().setQuanHeVoiChuHo("Thành viên");
                }

                hoKhau.setChuHo(chuHoMoi);
                // Cập nhật chủ hộ mới
                chuHoMoi.setQuanHeVoiChuHo("Chủ hộ");
                chuHoMoi.setHoKhau(hoKhau);

                // Đảm bảo chủ hộ mới có trong danh sách thành viên
                hoKhau.addThanhVien(chuHoMoi);
            }
        }

        // 4. Cập nhật Quan hệ thành viên
        if (request.getDanhSachThanhVien() != null) {
            for (HoKhauRequest.ThanhVienRequest tvReq : request.getDanhSachThanhVien()) {
                if (tvReq.getMaNhanKhau() == null || tvReq.getQuanHeVoiChuHo() == null) continue;

                NhanKhau nk = nhanKhauRepository.findById(tvReq.getMaNhanKhau()).orElse(null);

                // Chỉ xử lý nếu nhân khẩu này đang thuộc hộ khẩu hiện tại
                if (nk != null && nk.getHoKhau() != null && nk.getHoKhau().getMaHoKhau().equals(maHoKhau)) {

                    // --- KIỂM TRA QUAN TRỌNG ---
                    boolean laChuHoMoi = hoKhau.getChuHo().getMaNhanKhau().equals(nk.getMaNhanKhau());

                    if (laChuHoMoi) {
                        nk.setQuanHeVoiChuHo("Chủ hộ");
                    } else {
                        nk.setQuanHeVoiChuHo(tvReq.getQuanHeVoiChuHo());
                    }
                    nhanKhauRepository.save(nk);
                }
            }
        }

        return hoKhauRepository.save(hoKhau);
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
        // 1. Tìm hộ cũ
        HoKhau hoCu = hoKhauRepository.findById(maHoCu)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hộ khẩu với mã: " + maHoCu));

        List<String> tachRaIds = request.getMaNhanKhauTachRa();

        // 2. Tạo hộ khẩu mới
        HoKhau hoMoi = new HoKhau();
        NhanKhau chuHoMoi = nhanKhauRepository.findById(request.getMaNhanKhauChuHoMoi())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu chủ hộ mới"));

        hoMoi.setChuHo(chuHoMoi);
        hoMoi.setDiaChi(request.getDiaChiMoi());
        hoMoi.setNgayDangKy(new java.util.Date());

        // Lưu hộ mới trước
        hoMoi = hoKhauRepository.save(hoMoi);

        // 3. Lấy danh sách nhân khẩu cần tách
        List<NhanKhau> nhanKhausTachRa = nhanKhauRepository.findAllById(tachRaIds);

        for (NhanKhau nk : nhanKhausTachRa) {
            // Chỉ tách nếu đang thuộc hộ cũ
            if (nk.getHoKhau() != null && !nk.getHoKhau().getMaHoKhau().equals(maHoCu)) {
                continue;
            }

            // Gỡ khỏi hộ cũ
            hoCu.removeThanhVien(nk);

            // Thêm vào hộ mới
            hoMoi.addThanhVien(nk);

            // Cập nhật quan hệ (nếu là chủ hộ mới thì set Chủ hộ, còn lại là Thành viên)
            if(nk.getMaNhanKhau().equals(chuHoMoi.getMaNhanKhau())) {
                nk.setQuanHeVoiChuHo("Chủ hộ");
            } else {
                nk.setQuanHeVoiChuHo("Thành viên");
            }

            nhanKhauRepository.save(nk);
        }

        hoKhauRepository.save(hoCu);
        return hoKhauRepository.save(hoMoi);
    }


    @Override
    @Transactional
    public HoKhau doiChuHo(String maHoKhau, DoiChuHoRequest request) {
        HoKhau hk = hoKhauRepository.findById(maHoKhau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hộ khẩu với mã: " + maHoKhau));
        NhanKhau chuHoMoi = nhanKhauRepository.findById(request.getMaNhanKhauMoi())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với mã: " + request.getMaNhanKhauMoi()));

        // Chủ cũ thành thành viên
        if(hk.getChuHo() != null) {
            hk.getChuHo().setQuanHeVoiChuHo("Thành viên");
        }

        // Chủ mới
        hk.setChuHo(chuHoMoi);
        chuHoMoi.setQuanHeVoiChuHo("Chủ hộ");

        return hoKhauRepository.save(hk);
    }

    @Override
    @Transactional
    public HoKhau layHoKhauCuaToi(TaiKhoan taiKhoan) {
        if (taiKhoan == null || taiKhoan.getNhanKhau() == null) {
            throw new RuntimeException("Tài khoản không hợp lệ hoặc chưa liên kết với nhân khẩu");
        }

        String maNhanKhau = taiKhoan.getNhanKhau().getMaNhanKhau();
        NhanKhau nhanKhau = nhanKhauRepository.findById(maNhanKhau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với mã: " + maNhanKhau));

        HoKhau hoKhau = nhanKhau.getHoKhau();

        if (hoKhau == null) {
            return null;
        }

        // Trigger lazy load
        if (hoKhau.getDanhSachThanhVien() != null) {
            hoKhau.getDanhSachThanhVien().size();
        }

        return hoKhau;
    }

    @Override
    public List<HoKhau> timKiemTheoDiaChi(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return hoKhauRepository.findByDiaChiContainingIgnoreCase(keyword.trim());
    }

    @Override
    public List<HoKhau> timKiemTheoChuHo(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return hoKhauRepository.findByChuHoContaining(keyword.trim());
    }

    @Override
    public List<HoKhau> timKiemTongHop(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return hoKhauRepository.timKiemTongHop(keyword.trim());
    }

    @Override
    @Transactional
    public HoKhau nhapHo(String maHoNhapVao, NhapHoRequest request) {
        HoKhau hoNhapVao = hoKhauRepository.findById(maHoNhapVao)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hộ khẩu đích"));

        List<NhanKhau> nhanKhauList = new ArrayList<>();

        if (request.getCccdNhapVao() != null && !request.getCccdNhapVao().isEmpty()) {
            for (String cccd : request.getCccdNhapVao()) {
                nhanKhauRepository.findBySoCCCD(cccd.trim()).ifPresent(nhanKhauList::add);
            }
        }

        Set<HoKhau> cacHoKhauBiAnhHuong = new HashSet<>();

        for (NhanKhau nk : nhanKhauList) {
            if (nk.getHoKhau() != null) {
                cacHoKhauBiAnhHuong.add(nk.getHoKhau());
                nk.getHoKhau().removeThanhVien(nk);
            }

            nk.setHoKhau(hoNhapVao);
            // Quan hệ khi nhập hộ mặc định là thành viên
            nk.setQuanHeVoiChuHo("Thành viên");
            hoNhapVao.addThanhVien(nk);
            nhanKhauRepository.save(nk);
        }

        for (HoKhau hoCu : cacHoKhauBiAnhHuong) {
            if (!hoCu.getMaHoKhau().equals(maHoNhapVao)) {
                if (hoCu.getDanhSachThanhVien().isEmpty()) {
                    hoKhauRepository.delete(hoCu);
                } else {
                    hoKhauRepository.save(hoCu);
                }
            }
        }

        return hoKhauRepository.save(hoNhapVao);
    }
}