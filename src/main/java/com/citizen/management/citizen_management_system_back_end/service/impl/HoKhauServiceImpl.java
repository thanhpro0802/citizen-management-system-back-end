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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HoKhauServiceImpl implements HoKhauService {

    private final HoKhauRepository hoKhauRepository;
    private final NhanKhauRepository nhanKhauRepository;

    @Override
    @Transactional
    public HoKhau taoMoi(HoKhau hoKhau) {

        if (hoKhau.getChuHo() == null || hoKhau.getChuHo().getSoCCCD() == null) {
            throw new RuntimeException("Phải chọn chủ hộ khi tạo hộ khẩu");
        }

        // 1. Tìm chủ hộ bằng CCCD
        NhanKhau chuHo = nhanKhauRepository
                .findBySoCCCD(hoKhau.getChuHo().getSoCCCD().trim())
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy nhân khẩu với CCCD đã nhập")
                );

        // 2. Kiểm tra chủ hộ đã có hộ khẩu chưa
        if (chuHo.getHoKhau() != null) {
            throw new RuntimeException("Nhân khẩu này đã thuộc một hộ khẩu khác");
        }

        // 3. Set thông tin hộ khẩu
        hoKhau.setChuHo(chuHo);
        hoKhau.setNgayDangKy(new Date());

        // 4. Lưu hộ khẩu trước
        HoKhau hoKhauDaLuu = hoKhauRepository.save(hoKhau);

        // 5. Gán chủ hộ vào danh sách thành viên
        if (hoKhauDaLuu.getDanhSachThanhVien() == null) {
            hoKhauDaLuu.setDanhSachThanhVien(new ArrayList<>());
        }

        hoKhauDaLuu.addThanhVien(chuHo);

        // 6. Set quan hệ cho chủ hộ
        chuHo.setHoKhau(hoKhauDaLuu);
        chuHo.setQuanHeVoiChuHo("CHU_HO");
        nhanKhauRepository.save(chuHo);

        return hoKhauDaLuu;
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
            if (hoKhau.getChuHo() == null || !hoKhau.getChuHo().getSoCCCD().equals(newCCCD)) {
                NhanKhau chuHoMoi = nhanKhauRepository.findBySoCCCD(newCCCD)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với CCCD: " + newCCCD));

                hoKhau.setChuHo(chuHoMoi); // Gán object chủ hộ mới vào hộ khẩu
                // (Chưa save vội, để bước 4 xử lý luôn thể)
            }
        }

        // 4. Cập nhật Quan hệ thành viên (LOGIC ĐÃ SỬA)
        if (request.getDanhSachThanhVien() != null) {
            for (HoKhauRequest.ThanhVienRequest tvReq : request.getDanhSachThanhVien()) {
                if (tvReq.getMaNhanKhau() == null || tvReq.getQuanHeVoiChuHo() == null) continue;

                NhanKhau nk = nhanKhauRepository.findById(tvReq.getMaNhanKhau()).orElse(null);

                // Chỉ xử lý nếu nhân khẩu này đang thuộc hộ khẩu hiện tại
                if (nk != null && nk.getHoKhau() != null && nk.getHoKhau().getMaHoKhau().equals(maHoKhau)) {

                    // --- KIỂM TRA QUAN TRỌNG ---
                    // Kiểm tra xem người này CÓ PHẢI LÀ CHỦ HỘ MỚI (được set ở bước 3) hay không?
                    boolean laChuHoMoi = hoKhau.getChuHo().getMaNhanKhau().equals(nk.getMaNhanKhau());

                    if (laChuHoMoi) {
                        // Nếu là chủ hộ mới -> Bắt buộc quan hệ là CHU_HO
                        nk.setQuanHeVoiChuHo("CHU_HO");
                    } else {
                        // Nếu KHÔNG phải chủ hộ mới -> Cho phép cập nhật quan hệ theo ý người dùng
                        // (Kể cả người này trước đây là Chủ hộ cũ, giờ sẽ bị cập nhật thành quan hệ mới)
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

        HoKhau hoKhau = hoKhauRepository.findById(maHoKhau)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy hộ khẩu: " + maHoKhau)
                );

        // Gỡ chủ hộ
        hoKhau.setChuHo(null);

        // Gỡ liên kết nhân khẩu
        for (NhanKhau nk : hoKhau.getDanhSachThanhVien()) {
            nk.setHoKhau(null);
            nk.setQuanHeVoiChuHo(null);
            nhanKhauRepository.save(nk);
        }

        hoKhau.getDanhSachThanhVien().clear();

        hoKhauRepository.delete(hoKhau);
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
        hoMoi.setNgayDangKy(new java.util.Date()); // Lấy ngày hiện tại

        // Lưu hộ mới trước để có ID (nếu cần thiết với JPA)
        hoMoi = hoKhauRepository.save(hoMoi);

        // 3. Lấy danh sách nhân khẩu cần tách
        List<NhanKhau> nhanKhausTachRa = nhanKhauRepository.findAllById(tachRaIds);

        // Kiểm tra xem các nhân khẩu này có thuộc hộ cũ không (Optional)
        for (NhanKhau nk : nhanKhausTachRa) {
            if (nk.getHoKhau() != null && !nk.getHoKhau().getMaHoKhau().equals(maHoCu)) {
                // Có thể bỏ qua hoặc throw exception tùy nghiệp vụ
                continue;
            }

            // QUAN TRỌNG: Thực hiện chuyển đổi dứt khoát
            // Bước A: Gỡ khỏi hộ cũ (Hàm này sẽ set hoKhau = null và xóa khỏi list của hoCu)
            hoCu.removeThanhVien(nk);

            // Bước B: Thêm vào hộ mới (Hàm này set hoKhau = hoMoi và thêm vào list của hoMoi)
            hoMoi.addThanhVien(nk);

            // Bước C: Lưu nhân khẩu để cập nhật khóa ngoại (Foreign Key) ngay lập tức
            nhanKhauRepository.save(nk);
        }

        // 4. Lưu lại trạng thái 2 hộ khẩu
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
        hk.setChuHo(chuHoMoi);
        return hoKhauRepository.save(hk);
    }

    @Override
    @Transactional // <--- QUAN TRỌNG: Giữ session để load danh sách thành viên (tránh lỗi Lazy)
    public HoKhau layHoKhauCuaToi(TaiKhoan taiKhoan) {
        if (taiKhoan == null || taiKhoan.getNhanKhau() == null) {
            throw new RuntimeException("Tài khoản không hợp lệ hoặc chưa liên kết với nhân khẩu");
        }

        String maNhanKhau = taiKhoan.getNhanKhau().getMaNhanKhau();
        NhanKhau nhanKhau = nhanKhauRepository.findById(maNhanKhau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với mã: " + maNhanKhau));

        HoKhau hoKhau = nhanKhau.getHoKhau();

        // SỬA LỖI Ở ĐÂY: Trả về null thay vì ném Exception
        if (hoKhau == null) {
            return null;
        }

        // Mẹo: Gọi .size() để Hibernate tải dữ liệu danh sách thành viên ngay lập tức
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

        // (Logic tìm nhân khẩu giữ nguyên như cũ...)
        if (request.getCccdNhapVao() != null && !request.getCccdNhapVao().isEmpty()) {
            for (String cccd : request.getCccdNhapVao()) {
                nhanKhauRepository.findBySoCCCD(cccd.trim()).ifPresent(nhanKhauList::add);
            }
        }

        // Dùng Set để lưu danh sách các Hộ khẩu cũ bị ảnh hưởng (để kiểm tra xóa sau này)
        Set<HoKhau> cacHoKhauBiAnhHuong = new HashSet<>();

        for (NhanKhau nk : nhanKhauList) {
            if (nk.getHoKhau() != null) {
                // Lưu lại hộ cũ trước khi remove
                cacHoKhauBiAnhHuong.add(nk.getHoKhau());

                // Gỡ khỏi hộ cũ
                nk.getHoKhau().removeThanhVien(nk);
            }

            // Gán vào hộ mới
            nk.setHoKhau(hoNhapVao);
            hoNhapVao.addThanhVien(nk);
            nhanKhauRepository.save(nk);
        }

        // --- LOGIC MỚI: QUÉT CÁC HỘ CŨ ---
        for (HoKhau hoCu : cacHoKhauBiAnhHuong) {
            // Cẩn thận: Đừng xóa chính cái hộ đang nhập vào (nếu có trường hợp nhập nội bộ)
            if (!hoCu.getMaHoKhau().equals(maHoNhapVao)) {
                if (hoCu.getDanhSachThanhVien().isEmpty()) {
                    hoKhauRepository.delete(hoCu); // Xóa nếu rỗng
                } else {
                    hoKhauRepository.save(hoCu);   // Lưu nếu còn người
                }
            }
        }

        return hoKhauRepository.save(hoNhapVao);
    }
}