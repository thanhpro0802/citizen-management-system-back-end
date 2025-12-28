package com.citizen.management.citizen_management_system_back_end.service.impl;

import com.citizen.management.citizen_management_system_back_end.dto.request.XuLyYeuCauCuTruRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.YeuCauCuTruRequest;
import com.citizen.management.citizen_management_system_back_end.dto.response.YeuCauCuTruResponse;
import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.entity.ThongBao;
import com.citizen.management.citizen_management_system_back_end.entity.YeuCauCuTru;
import com.citizen.management.citizen_management_system_back_end.enums.*;
import com.citizen.management.citizen_management_system_back_end.repository.*;
import com.citizen.management.citizen_management_system_back_end.service.YeuCauCuTruService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class YeuCauCuTruServiceImpl implements YeuCauCuTruService {

    private final YeuCauCuTruRepository yeuCauRepository;
    private final NhanKhauRepository nhanKhauRepository;
    private final ThongBaoRepository thongBaoRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final HoKhauRepository hoKhauRepository;

    @Override
    @Transactional
    public YeuCauCuTruResponse taoYeuCau(YeuCauCuTruRequest request, TaiKhoan nguoiTao) {
        // Kiểm tra người tạo có nhân khẩu không
        NhanKhau nhanKhau = nguoiTao.getNhanKhau();
        if (nhanKhau == null) {
            throw new RuntimeException("Tài khoản chưa liên kết với nhân khẩu");
        }

        // Validate dữ liệu theo loại yêu cầu
        validateYeuCauData(request);

        YeuCauCuTru yeuCau = new YeuCauCuTru();
        yeuCau.setMaYeuCau("YC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        yeuCau.setLoaiYeuCau(request.getLoaiYeuCau());
        yeuCau.setNguoiTao(nhanKhau);
        yeuCau.setDoiTuongDeNghi(request.getDoiTuongDeNghi());

        // Copy thông tin khai hộ
        if (request.getDoiTuongDeNghi() == EnumDoiTuongDeNghi.KHAI_HO) {
            yeuCau.setNguoiDeNghiHoTen(request.getNguoiDeNghiHoTen());
            yeuCau.setNguoiDeNghiNgaySinh(request.getNguoiDeNghiNgaySinh());
            yeuCau.setNguoiDeNghiGioiTinh(request.getNguoiDeNghiGioiTinh());
            yeuCau.setNguoiDeNghiCccd(request.getNguoiDeNghiCccd());
        }

        // Copy thông tin theo loại yêu cầu
        switch (request.getLoaiYeuCau()) {
            case DANG_KY_TAM_TRU:
            case DANG_KY_THUONG_TRU:
                yeuCau.setLoaiHinhDangKy(request.getLoaiHinhDangKy());
                yeuCau.setDiaChiCuTru(request.getDiaChiCuTru());
                if (request.getLoaiHinhDangKy() == EnumLoaiHinhDangKy.VAO_HO_DA_CO) {
                    yeuCau.setChuHoHoTen(request.getChuHoHoTen());
                    yeuCau.setChuHoCccd(request.getChuHoCccd());
                }
                break;

            case KHAI_BAO_TAM_VANG:
                yeuCau.setLoaiTamVang(request.getLoaiTamVang());
                yeuCau.setNoiDen(request.getNoiDen());
                yeuCau.setThoiGianBatDau(request.getThoiGianBatDau());
                yeuCau.setThoiGianKetThuc(request.getThoiGianKetThuc());
                yeuCau.setLyDo(request.getLyDo());
                break;

            case DIEU_CHINH_THONG_TIN:
                yeuCau.setPhanCanDieuChinh(request.getPhanCanDieuChinh());
                yeuCau.setLyDo(request.getLyDo());
                break;

            case XOA_DANG_KY:
                yeuCau.setLyDo(request.getLyDo());
                break;
        }

        yeuCau.setFileDinhKem(request.getFileDinhKem());
        yeuCau.setTrangThai(EnumTrangThaiYeuCau.CHO_XU_LY);
        yeuCau.setNgayTao(new Date());
        yeuCau.setNgayCapNhat(new Date());

        // Lưu yêu cầu trước
        YeuCauCuTru saved = yeuCauRepository.save(yeuCau);

        // --- CẬP NHẬT LOGIC GỬI THÔNG BÁO ---
        // Gửi cho Cán bộ hộ khẩu, Tổ trưởng, Tổ phó, Admin
        List<EnumVaiTro> rolesCanNotification = Arrays.asList(
                EnumVaiTro.CAN_BO_HO_KHAU,
                EnumVaiTro.TO_TRUONG,
                EnumVaiTro.TO_PHO,
                EnumVaiTro.ADMIN
        );

        // Tìm tất cả tài khoản có vai trò phù hợp
        // Lưu ý: Nếu Repo chưa có findByVaiTroIn thì có thể phải loop hoặc thêm method vào Repo
        List<TaiKhoan> danhSachNhanThongBao = new ArrayList<>();
        for (EnumVaiTro role : rolesCanNotification) {
            danhSachNhanThongBao.addAll(taiKhoanRepository.findByVaiTro(role));
        }

        for (TaiKhoan canBo : danhSachNhanThongBao) {
            ThongBao tb = new ThongBao();
            tb.setNguoiNhan(canBo);
            tb.setNoiDung("Yêu cầu cư trú mới từ " + nhanKhau.getHoTen() + ": " +
                    saved.getLoaiYeuCau().getTenHienThi() + " - Mã: " + saved.getMaYeuCau());
            tb.setThoiGian(new Date());
            tb.setDaXem(false);
            tb.setMaYeuCauCuTruLienQuan(saved.getMaYeuCau());
            thongBaoRepository.save(tb);
        }

        return convertToResponse(saved);
    }

    @Override
    public List<YeuCauCuTruResponse> layYeuCauCuaToi(TaiKhoan taiKhoan) {
        NhanKhau nhanKhau = taiKhoan.getNhanKhau();
        if (nhanKhau == null) {
            throw new RuntimeException("Tài khoản chưa liên kết với nhân khẩu");
        }

        List<YeuCauCuTru> yeuCauList = yeuCauRepository.findByNguoiTaoOrderByNgayTaoDesc(nhanKhau);
        return yeuCauList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public YeuCauCuTruResponse layChiTietYeuCau(String maYeuCau, TaiKhoan taiKhoan) {
        YeuCauCuTru yeuCau = yeuCauRepository.findById(maYeuCau)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy yêu cầu"));

        // Kiểm tra quyền truy cập
        if (!hasAccessToYeuCau(yeuCau, taiKhoan)) {
            throw new RuntimeException("Bạn không có quyền xem yêu cầu này");
        }

        return convertToResponse(yeuCau);
    }

    @Override
    @Transactional
    public YeuCauCuTruResponse huyYeuCau(String maYeuCau, TaiKhoan taiKhoan) {
        YeuCauCuTru yeuCau = yeuCauRepository.findById(maYeuCau)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy yêu cầu"));

        // Kiểm tra quyền sở hữu
        if (!yeuCau.getNguoiTao().getMaNhanKhau().equals(taiKhoan.getNhanKhau().getMaNhanKhau())) {
            throw new RuntimeException("Bạn không có quyền hủy yêu cầu này");
        }

        // Chỉ cho phép hủy khi đang chờ xử lý
        if (yeuCau.getTrangThai() != EnumTrangThaiYeuCau.CHO_XU_LY) {
            throw new RuntimeException("Chỉ có thể hủy yêu cầu đang chờ xử lý");
        }

        yeuCau.setTrangThai(EnumTrangThaiYeuCau.HUY);
        yeuCau.setNgayCapNhat(new Date());

        YeuCauCuTru updated = yeuCauRepository.save(yeuCau);
        return convertToResponse(updated);
    }

    @Override
    public Page<YeuCauCuTruResponse> layTatCaYeuCau(Pageable pageable) {
        Page<YeuCauCuTru> yeuCauPage = yeuCauRepository.findAllByOrderByNgayTaoDesc(pageable);
        return yeuCauPage.map(this::convertToResponse);
    }

    @Override
    public Page<YeuCauCuTruResponse> timKiemYeuCau(
            EnumTrangThaiYeuCau trangThai,
            EnumLoaiYeuCauCuTru loaiYeuCau,
            Pageable pageable) {

        Page<YeuCauCuTru> yeuCauPage = yeuCauRepository.searchYeuCau(trangThai, loaiYeuCau, null, pageable);
        return yeuCauPage.map(this::convertToResponse);
    }

    @Override
    @Transactional
    public YeuCauCuTruResponse xuLyYeuCau(String maYeuCau, XuLyYeuCauCuTruRequest request, TaiKhoan canBo) {
        YeuCauCuTru yeuCau = yeuCauRepository.findById(maYeuCau)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy yêu cầu"));

        // Kiểm tra quyền (phải là cán bộ)
        if (!isCanBo(canBo)) {
            throw new RuntimeException("Bạn không có quyền xử lý yêu cầu này");
        }

        // Cập nhật trạng thái và thông tin xử lý
        yeuCau.setTrangThai(request.getTrangThaiMoi());
        yeuCau.setCanBoXuLy(canBo.getNhanKhau());
        yeuCau.setGhiChu(request.getGhiChu());

        if (request.getTrangThaiMoi() == EnumTrangThaiYeuCau.TU_CHOI) {
            yeuCau.setLyDoTuChoi(request.getLyDoTuChoi());
        }

        yeuCau.setNgayCapNhat(new Date());

        // Nếu phê duyệt, thực hiện các nghiệp vụ tương ứng
        if (request.getTrangThaiMoi() == EnumTrangThaiYeuCau.DA_PHE_DUYET) {
            thucHienNghiepVu(yeuCau);
        }

        YeuCauCuTru updated = yeuCauRepository.save(yeuCau);
        return convertToResponse(updated);
    }

    @Override
    @Transactional
    public YeuCauCuTruResponse pheDuyetYeuCau(String maYeuCau, String ghiChu, TaiKhoan canBo) {
        XuLyYeuCauCuTruRequest request = new XuLyYeuCauCuTruRequest();
        request.setTrangThaiMoi(EnumTrangThaiYeuCau.DA_PHE_DUYET);
        request.setGhiChu(ghiChu);
        YeuCauCuTruResponse response = xuLyYeuCau(maYeuCau, request, canBo);

        // Tạo thông báo cho công dân
        YeuCauCuTru yeuCau = yeuCauRepository.findById(maYeuCau)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy yêu cầu với mã: " + maYeuCau));

        ThongBao tb = new ThongBao();
        tb.setNguoiNhan(yeuCau.getNguoiTao().getTaiKhoan());
        tb.setNoiDung("Đã phê duyệt: " + yeuCau.getLoaiYeuCau().getTenHienThi() +
                " - Mã: " + yeuCau.getMaYeuCau() +
                (ghiChu != null && !ghiChu.isEmpty() ? " - Ghi chú: " + ghiChu : ""));
        tb.setThoiGian(new Date());
        tb.setDaXem(false);
        tb.setMaYeuCauCuTruLienQuan(yeuCau.getMaYeuCau());
        thongBaoRepository.save(tb);

        return response;
    }

    @Override
    @Transactional
    public YeuCauCuTruResponse nhanXuLyYeuCau(String maYeuCau, TaiKhoan canBo) {
        YeuCauCuTru yeuCau = yeuCauRepository.findById(maYeuCau)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy yêu cầu với mã: " + maYeuCau));

        // Kiểm tra quyền xử lý
        if (!isCanBo(canBo)) {
            throw new RuntimeException("Bạn không có quyền nhận xử lý yêu cầu này");
        }

        // Kiểm tra trạng thái hiện tại
        if (yeuCau.getTrangThai() != EnumTrangThaiYeuCau.CHO_XU_LY) {
            throw new RuntimeException("Chỉ có thể nhận xử lý yêu cầu đang ở trạng thái CHỜ XỬ LÝ");
        }

        // Cập nhật trạng thái và cán bộ xử lý
        yeuCau.setTrangThai(EnumTrangThaiYeuCau.DANG_XU_LY);
        yeuCau.setCanBoXuLy(canBo.getNhanKhau());
        yeuCau.setNgayCapNhat(new Date());

        YeuCauCuTru updated = yeuCauRepository.save(yeuCau);

        // Tạo thông báo cho công dân
        ThongBao tb = new ThongBao();
        tb.setNguoiNhan(yeuCau.getNguoiTao().getTaiKhoan());
        tb.setNoiDung("Yêu cầu cư trú của bạn đang được xử lý bởi " + canBo.getNhanKhau().getHoTen());
        tb.setThoiGian(new Date());
        tb.setDaXem(false);
        tb.setMaYeuCauCuTruLienQuan(yeuCau.getMaYeuCau());
        thongBaoRepository.save(tb);

        return convertToResponse(updated);
    }

    @Override
    @Transactional
    public YeuCauCuTruResponse tuChoiYeuCau(String maYeuCau, String lyDoTuChoi, TaiKhoan canBo) {
        XuLyYeuCauCuTruRequest request = new XuLyYeuCauCuTruRequest();
        request.setTrangThaiMoi(EnumTrangThaiYeuCau.TU_CHOI);
        request.setLyDoTuChoi(lyDoTuChoi);
        YeuCauCuTruResponse response = xuLyYeuCau(maYeuCau, request, canBo);

        // Tạo thông báo cho công dân
        YeuCauCuTru yeuCau = yeuCauRepository.findById(maYeuCau)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy yêu cầu với mã: " + maYeuCau));

        ThongBao tb = new ThongBao();
        tb.setNguoiNhan(yeuCau.getNguoiTao().getTaiKhoan());
        tb.setNoiDung("Từ chối: " + yeuCau.getLoaiYeuCau().getTenHienThi() +
                " - Mã: " + yeuCau.getMaYeuCau() +
                " - Lý do: " + lyDoTuChoi);
        tb.setThoiGian(new Date());
        tb.setDaXem(false);
        tb.setMaYeuCauCuTruLienQuan(yeuCau.getMaYeuCau());
        thongBaoRepository.save(tb);

        return response;
    }

    @Override
    public long demYeuCauTheoTrangThai(EnumTrangThaiYeuCau trangThai) {
        return yeuCauRepository.countByTrangThai(trangThai);
    }

    // ========== HELPER METHODS ==========

    private void validateYeuCauData(YeuCauCuTruRequest request) {
        switch (request.getLoaiYeuCau()) {
            case DANG_KY_TAM_TRU:
            case DANG_KY_THUONG_TRU:
                if (request.getLoaiHinhDangKy() == null) {
                    throw new RuntimeException("Loại hình đăng ký không được để trống");
                }
                if (request.getDiaChiCuTru() == null || request.getDiaChiCuTru().trim().isEmpty()) {
                    throw new RuntimeException("Địa chỉ cư trú không được để trống");
                }
                if (request.getLoaiHinhDangKy() == EnumLoaiHinhDangKy.VAO_HO_DA_CO) {
                    if (request.getChuHoHoTen() == null || request.getChuHoCccd() == null) {
                        throw new RuntimeException("Thông tin chủ hộ không được để trống khi đăng ký vào hộ đã có");
                    }
                }
                break;

            case KHAI_BAO_TAM_VANG:
                if (request.getLoaiTamVang() == null) {
                    throw new RuntimeException("Loại tạm vắng không được để trống");
                }
                if (request.getNoiDen() == null || request.getNoiDen().trim().isEmpty()) {
                    throw new RuntimeException("Nơi đến không được để trống");
                }
                if (request.getThoiGianBatDau() == null || request.getThoiGianKetThuc() == null) {
                    throw new RuntimeException("Thời gian tạm vắng không được để trống");
                }
                break;

            case DIEU_CHINH_THONG_TIN:
            case XOA_DANG_KY:
                if (request.getLyDo() == null || request.getLyDo().trim().isEmpty()) {
                    throw new RuntimeException("Lý do không được để trống");
                }
                break;
        }

        // Validate khai hộ
        if (request.getDoiTuongDeNghi() == EnumDoiTuongDeNghi.KHAI_HO) {
            if (request.getNguoiDeNghiHoTen() == null || request.getNguoiDeNghiCccd() == null) {
                throw new RuntimeException("Thông tin người đề nghị không được để trống khi khai hộ");
            }
        }
    }

    private void thucHienNghiepVu(YeuCauCuTru yeuCau) {
        NhanKhau nguoiDeNghi = yeuCau.getNguoiTao();

        if (nguoiDeNghi == null) {
            throw new RuntimeException("Không tìm thấy thông tin nhân khẩu");
        }

        switch (yeuCau.getLoaiYeuCau()) {
            case DANG_KY_TAM_TRU:
                nguoiDeNghi.setTrangThai(EnumTrangThaiNhanKhau.TAM_TRU);
                nhanKhauRepository.save(nguoiDeNghi);
                break;

            case DANG_KY_THUONG_TRU:
                nguoiDeNghi.setTrangThai(EnumTrangThaiNhanKhau.THUONG_TRU);

                if (yeuCau.getLoaiHinhDangKy() == EnumLoaiHinhDangKy.VAO_HO_DA_CO) {
                    String cccdChuHo = yeuCau.getChuHoCccd();
                    if (cccdChuHo != null && !cccdChuHo.trim().isEmpty()) {
                        NhanKhau chuHo = nhanKhauRepository.findBySoCCCD(cccdChuHo)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy chủ hộ với CCCD: " + cccdChuHo));

                        if (chuHo.getMaHoKhau() != null) {
                            nguoiDeNghi.setMaHoKhau(chuHo.getMaHoKhau());
                            nguoiDeNghi.setQuanHeVoiChuHo("THANH_VIEN");
                        } else {
                            throw new RuntimeException("Chủ hộ chưa có hộ khẩu");
                        }
                    }
                } else if (yeuCau.getLoaiHinhDangKy() == EnumLoaiHinhDangKy.LAP_HO_MOI) {
                    HoKhau hoKhauMoi = new HoKhau();
                    hoKhauMoi.setMaHoKhau("HK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                    hoKhauMoi.setChuHo(nguoiDeNghi);
                    hoKhauMoi.setDiaChi(yeuCau.getDiaChiCuTru());
                    hoKhauMoi.setNgayDangKy(new Date());
                    hoKhauMoi = hoKhauRepository.save(hoKhauMoi);

                    nguoiDeNghi.setMaHoKhau(hoKhauMoi.getMaHoKhau());
                    nguoiDeNghi.setQuanHeVoiChuHo("CHU_HO");
                }

                nhanKhauRepository.save(nguoiDeNghi);
                break;

            case KHAI_BAO_TAM_VANG:
                nguoiDeNghi.setTrangThai(EnumTrangThaiNhanKhau.TAM_VANG);
                nhanKhauRepository.save(nguoiDeNghi);
                break;

            case XOA_DANG_KY:
                if (nguoiDeNghi.getTrangThai() == EnumTrangThaiNhanKhau.THUONG_TRU) {
                    nguoiDeNghi.setTrangThai(EnumTrangThaiNhanKhau.TAM_TRU);
                    nguoiDeNghi.setMaHoKhau(null);
                    nguoiDeNghi.setQuanHeVoiChuHo(null);
                    nhanKhauRepository.save(nguoiDeNghi);
                }
                break;

            case DIEU_CHINH_THONG_TIN:
                break;
        }
    }

    private boolean hasAccessToYeuCau(YeuCauCuTru yeuCau, TaiKhoan taiKhoan) {
        // Cán bộ có quyền xem tất cả
        if (isCanBo(taiKhoan)) {
            return true;
        }

        // Người dân chỉ xem được yêu cầu của mình
        if (taiKhoan.getNhanKhau() != null) {
            return yeuCau.getNguoiTao().getMaNhanKhau().equals(taiKhoan.getNhanKhau().getMaNhanKhau());
        }

        return false;
    }

    // --- CẬP NHẬT: CHECK QUYỀN CÁN BỘ DỰA TRÊN ENUM MỚI ---
    private boolean isCanBo(TaiKhoan taiKhoan) {
        EnumVaiTro vaiTro = taiKhoan.getVaiTro();
        return vaiTro == EnumVaiTro.ADMIN ||
                vaiTro == EnumVaiTro.TO_TRUONG ||
                vaiTro == EnumVaiTro.TO_PHO ||
                vaiTro == EnumVaiTro.CAN_BO_HO_KHAU ||
                vaiTro == EnumVaiTro.CAN_BO_NHAN_KHAU ||
                vaiTro == EnumVaiTro.CAN_BO_PHAN_ANH;
    }

    private YeuCauCuTruResponse convertToResponse(YeuCauCuTru entity) {
        YeuCauCuTruResponse response = new YeuCauCuTruResponse();

        response.setMaYeuCau(entity.getMaYeuCau());
        response.setLoaiYeuCau(entity.getLoaiYeuCau());
        response.setLoaiYeuCauText(getLoaiYeuCauText(entity.getLoaiYeuCau()));

        if (entity.getNguoiTao() != null) {
            response.setNguoiTaoMa(entity.getNguoiTao().getMaNhanKhau());
            response.setNguoiTaoHoTen(entity.getNguoiTao().getHoTen());
            response.setNguoiTaoCccd(entity.getNguoiTao().getSoCCCD());
            if (entity.getNguoiTao().getTaiKhoan() != null) {
                response.setNguoiTaoSoDienThoai(entity.getNguoiTao().getTaiKhoan().getSoDienThoai());
            }
        }

        response.setDoiTuongDeNghi(entity.getDoiTuongDeNghi());

        response.setNguoiDeNghiHoTen(entity.getNguoiDeNghiHoTen());
        response.setNguoiDeNghiNgaySinh(entity.getNguoiDeNghiNgaySinh());
        response.setNguoiDeNghiGioiTinh(entity.getNguoiDeNghiGioiTinh());
        response.setNguoiDeNghiCccd(entity.getNguoiDeNghiCccd());

        response.setLoaiHinhDangKy(entity.getLoaiHinhDangKy());
        response.setChuHoHoTen(entity.getChuHoHoTen());
        response.setChuHoCccd(entity.getChuHoCccd());
        response.setDiaChiCuTru(entity.getDiaChiCuTru());

        response.setLoaiTamVang(entity.getLoaiTamVang());
        response.setNoiDen(entity.getNoiDen());
        response.setThoiGianBatDau(entity.getThoiGianBatDau());
        response.setThoiGianKetThuc(entity.getThoiGianKetThuc());
        response.setLyDo(entity.getLyDo());

        response.setPhanCanDieuChinh(entity.getPhanCanDieuChinh());

        response.setTrangThai(entity.getTrangThai());
        response.setTrangThaiText(getTrangThaiText(entity.getTrangThai()));
        response.setNgayTao(entity.getNgayTao());
        response.setNgayCapNhat(entity.getNgayCapNhat());

        if (entity.getCanBoXuLy() != null) {
            response.setCanBoXuLyMa(entity.getCanBoXuLy().getMaNhanKhau());
            response.setCanBoXuLyHoTen(entity.getCanBoXuLy().getHoTen());
        }

        response.setGhiChu(entity.getGhiChu());
        response.setLyDoTuChoi(entity.getLyDoTuChoi());
        response.setFileDinhKem(entity.getFileDinhKem());

        return response;
    }

    private String getLoaiYeuCauText(EnumLoaiYeuCauCuTru loaiYeuCau) {
        switch (loaiYeuCau) {
            case DANG_KY_TAM_TRU: return "Đăng ký tạm trú";
            case DANG_KY_THUONG_TRU: return "Đăng ký thường trú";
            case KHAI_BAO_TAM_VANG: return "Khai báo tạm vắng";
            case DIEU_CHINH_THONG_TIN: return "Điều chỉnh thông tin cư trú";
            case XOA_DANG_KY: return "Xóa đăng ký";
            default: return "";
        }
    }

    private String getTrangThaiText(EnumTrangThaiYeuCau trangThai) {
        switch (trangThai) {
            case CHO_XU_LY: return "Chờ xử lý";
            case DANG_XU_LY: return "Đang xử lý";
            case DA_PHE_DUYET: return "Đã phê duyệt";
            case TU_CHOI: return "Từ chối";
            case HUY: return "Đã hủy";
            default: return "";
        }
    }
}