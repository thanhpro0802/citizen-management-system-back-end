package com.citizen.management.citizen_management_system_back_end.service.impl;

import com.citizen.management.citizen_management_system_back_end.dto.request.*;
import com.citizen.management.citizen_management_system_back_end.entity.*;
import com.citizen.management.citizen_management_system_back_end.enums.EnumHanhDong;
import com.citizen.management.citizen_management_system_back_end.enums.EnumMucDoKhanCap;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThai;
import com.citizen.management.citizen_management_system_back_end.repository.*;
import com.citizen.management.citizen_management_system_back_end.service.IPhanAnhService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhanAnhServiceImpl implements IPhanAnhService {
    private final PhanAnhRepository phanAnhRepository;
    private final LichSuPhanAnhRepository lichSuRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final TepDinhKemRepository tepDinhKemRepository;
    private final ThongBaoRepository thongBaoRepository;

    @Override
    @Transactional
    public PhanAnh guiPhanAnh(GuiPhanAnhRequest request, TaiKhoan nguoiGui) {
        // He thong tiep nhan
        // 1.Tao phan anh moi
        PhanAnh pa = new PhanAnh();
        pa.setTieuDe(request.getTieuDe());
        pa.setLinhVuc(request.getLinhVuc());
        pa.setNguoiGui(nguoiGui);

        pa.setNoiDung(request.getNoiDung());

        // Set trang thai dau
        pa.setTrangThaiHienTai(EnumTrangThai.CHO);
        pa.setMucDoKhanCap(EnumMucDoKhanCap.THAP);

        pa.setThoiGianTao(new Date());

        // Luu de lay ID
        PhanAnh paDaLuu = phanAnhRepository.save(pa);

        // 2.Tao lich su dau tien
        LichSuPhanAnh ls = new LichSuPhanAnh();
        ls.setPhanAnh(paDaLuu);
        ls.setTaiKhoanThucHien(nguoiGui);
        ls.setThoiGian(new Date());
        ls.setHanhDong(EnumHanhDong.TAO_MOI);
        ls.setNoiDung(request.getNoiDung());

        lichSuRepository.save(ls);

        // 3.Xu ly file dinh kem
        if (request.getDanhSachFileUrl() != null && !request.getDanhSachFileUrl().isEmpty()) {
            List<TepDinhKem> listTep = new ArrayList<>();

            for (String url : request.getDanhSachFileUrl()) {
                TepDinhKem tep = new TepDinhKem();
                tep.setPhanAnh(paDaLuu);
                tep.setUrl(url);
                tep.setTenFileGoc("Anh_dinh_kem_cong_dan"); // Temp co dinh
                listTep.add(tep);
            }

            // Luu file vao tep_dinh_kem
            tepDinhKemRepository.saveAll(listTep);
        }

        return paDaLuu;
    }

    @Override
    @Transactional
    public PhanAnh phanCongXuLy(String maPhanAnh, PhanCongRequest request, TaiKhoan nguoiPhanCong) {
        // 1. Tìm cán bộ nhận việc (Người cấp dưới)
        TaiKhoan canBoDuocGiao = taiKhoanRepository.findById(request.getMaCanBoPhuTrach())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Không tìm thấy cán bộ với mã: " + request.getMaCanBoPhuTrach()));

        // 2. Tìm phản ánh
        PhanAnh pa = phanAnhRepository.findById(maPhanAnh)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Phản ánh với mã: " + maPhanAnh));

        // 3. Cập nhật thông tin phản ánh
        pa.setCanBoPhuTrach(canBoDuocGiao);
        pa.setThoiHanXuLy(request.getThoiHanXuLy());
        pa.setTrangThaiHienTai(EnumTrangThai.DANG_XU_LY); // Chuyển sang đang xử lý luôn

        // 4. Lưu phản ánh
        PhanAnh paDaCapNhat = phanAnhRepository.save(pa);

        // 5. Ghi lịch sử (Như cũ)
        LichSuPhanAnh ls = new LichSuPhanAnh();
        ls.setPhanAnh(paDaCapNhat);
        ls.setTaiKhoanThucHien(nguoiPhanCong); // Người thực hiện là Sếp (người phân công)
        ls.setThoiGian(new Date());
        ls.setHanhDong(EnumHanhDong.PHAN_CONG);
        ls.setTrangThaiMoi(EnumTrangThai.DANG_XU_LY);
        String tenCanBo = "Cán bộ"; // Giá trị mặc định

        // Kiểm tra xem Tài khoản có liên kết với Nhân khẩu không để tránh
        // NullPointerException
        if (canBoDuocGiao.getNhanKhau() != null && canBoDuocGiao.getNhanKhau().getHoTen() != null) {
            tenCanBo = canBoDuocGiao.getNhanKhau().getHoTen();
        }

        // Format nội dung log: "Phân công cho cán bộ: Nguyễn Văn A (001...)"
        String noiDungLog = String.format("Phân công cho cán bộ: %s (%s)",
                tenCanBo,
                canBoDuocGiao.getCccd());
        ls.setNoiDung(noiDungLog);
        lichSuRepository.save(ls);

        // --- 6. MỚI: TẠO THÔNG BÁO CHO CÁN BỘ ĐƯỢC GIAO ---
        ThongBao tb = new ThongBao();

        // Người nhận là Cán bộ cấp dưới (canBoDuocGiao)
        tb.setNguoiNhan(canBoDuocGiao);

        // Nội dung thông báo
        tb.setNoiDung("Bạn vừa được phân công xử lý hồ sơ: " + pa.getTieuDe());

        tb.setThoiGian(new Date());
        tb.setDaXem(false);
        tb.setMaPhanAnhLienQuan(pa.getMaPhanAnh()); // Để bấm vào thông báo thì nhảy tới hồ sơ

        thongBaoRepository.save(tb);
        // ---------------------------------------------------

        return paDaCapNhat;
    }

    @Override
    @Transactional
    public void capNhatXuLyNoiBo(String maPhanAnh, XuLyNoiBoRequest request, TaiKhoan canBoXuLy) {
        // 1. Tìm phản ánh
        PhanAnh pa = phanAnhRepository.findById(maPhanAnh)
                .orElseThrow(() -> new EntityNotFoundException("Khong tim thay Phan anh: " + maPhanAnh));

        // 2. CHECK QUYỀN: Đảm bảo đúng người mới được ghi nhật ký
        if (pa.getCanBoPhuTrach() == null || !pa.getCanBoPhuTrach().getMaTaiKhoan().equals(canBoXuLy.getMaTaiKhoan())) {
            throw new AccessDeniedException("Bạn không phải người phụ trách hồ sơ này!");
        }

        // 3. Cập nhật trạng thái nếu đang CHỜ
        if (pa.getTrangThaiHienTai() == EnumTrangThai.CHO) {
            pa.setTrangThaiHienTai(EnumTrangThai.DANG_XU_LY);
            phanAnhRepository.save(pa);
        }

        // 4. Ghi lịch sử
        LichSuPhanAnh ls = new LichSuPhanAnh();
        ls.setPhanAnh(pa);
        ls.setTaiKhoanThucHien(canBoXuLy);
        ls.setThoiGian(new Date());
        ls.setHanhDong(EnumHanhDong.XU_LY);

        ls.setNoiDung(request.getNoiDung());

        ls.setTrangThaiMoi(pa.getTrangThaiHienTai());
        lichSuRepository.save(ls);

        // 5. Xử lý file đính kèm
        if (request.getDanhSachFileUrl() != null && !request.getDanhSachFileUrl().isEmpty()) {
            List<TepDinhKem> tepMoiList = new ArrayList<>();
            for (String fileUrl : request.getDanhSachFileUrl()) {
                TepDinhKem tep = new TepDinhKem();
                tep.setPhanAnh(pa);
                tep.setUrl(fileUrl);
                tep.setTenFileGoc("File_tu_can_bo_xu_ly.jpg");
                tepMoiList.add(tep);
            }
            tepDinhKemRepository.saveAll(tepMoiList);
        }
    }

    @Override
    @Transactional
    public PhanAnh phanHoiCongDan(String maPhanAnh, PhanHoiRequest request, TaiKhoan canBoPhanHoi) {
        // 1.Tim phan anh
        PhanAnh pa = phanAnhRepository.findById((maPhanAnh))
                .orElseThrow(() -> new EntityNotFoundException("Khong tim thay Phan anh: " + maPhanAnh));

        // 2.Cap nhat trang thai
        pa.setTrangThaiHienTai(EnumTrangThai.DA_XU_LY);
        pa.setThoiGianHoanThanh(new Date());
        PhanAnh paDaCapNhat = phanAnhRepository.save(pa);

        // 3.Tao lich su ghi nhan phan hoi
        LichSuPhanAnh ls = new LichSuPhanAnh();
        ls.setPhanAnh(paDaCapNhat);
        ls.setTaiKhoanThucHien(canBoPhanHoi);
        ls.setThoiGian(new Date());
        ls.setHanhDong(EnumHanhDong.PHAN_HOI);
        ls.setNoiDung(request.getNoiDungPhanHoi());
        ls.setTrangThaiMoi(EnumTrangThai.DA_XU_LY);

        lichSuRepository.save(ls);

        // 4.Tao thong bao cho cong dan
        ThongBao tb = new ThongBao();
        tb.setNguoiNhan(pa.getNguoiGui()); // Gui cho nguoi tao phan anh
        tb.setNoiDung("Phản ánh: '" + pa.getTieuDe() + "' của bạn đã có kết quả xử lý.");
        tb.setThoiGian(new Date());
        tb.setDaXem(false);

        tb.setMaPhanAnhLienQuan(pa.getMaPhanAnh());

        thongBaoRepository.save(tb);

        return paDaCapNhat;
    }

    @Override
    @Transactional
    public PhanAnh danhGiaPhanHoi(String maPhanAnh, DanhGiaRequest request, TaiKhoan nguoiDanhGia) {
        // 1.TIm phan anh
        PhanAnh pa = phanAnhRepository.findById(maPhanAnh)
                .orElseThrow(() -> new EntityNotFoundException("Khong tim thay Phan anh: " + maPhanAnh));

        // 2.Kiem tra bao mat
        if (!pa.getNguoiGui().getMaTaiKhoan().equals(nguoiDanhGia.getMaTaiKhoan())) {
            throw new RuntimeException("Ban khong co quyen danh gia phan anh nay!");
        }

        // 3.Dam bao chi danh gia khi da xu ly xong
        if (pa.getTrangThaiHienTai() != EnumTrangThai.DA_XU_LY) {
            throw new RuntimeException("Phan anh nay chua xu ly xong!");
        }

        // 4.Cap nhat danh gia
        pa.setDanhGiaHaiLong(request.getDanhGiaHaiLong());
        pa.setGopY(request.getGopY());

        return phanAnhRepository.save(pa);
    }

    @Override
    public List<PhanAnh> layDanhSachPhanAnhCuaToi(TaiKhoan nguoiGui) {
        return phanAnhRepository.findAllByNguoiGui(nguoiGui); // Lay danh sach
    }

    @Override
    public PhanAnh layChiTietPhanAnh(String id) {
        return phanAnhRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay Phan anh"));
    }

    @Override
    public List<LichSuPhanAnh> layLichSuPhanAnh(String maPhanAnh) {
        PhanAnh pa = phanAnhRepository.findById(maPhanAnh)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phản ánh"));
        // Đảm bảo method này tồn tại trong LichSuPhanAnhRepository
        return lichSuRepository.findByPhanAnhOrderByThoiGianDesc(pa);
    }

    @Override
    public List<PhanAnh> layTatCaPhanAnh() {
        return phanAnhRepository.findAll();
    }

    @Override
    @Transactional
    public PhanAnh capNhatMucDoKhanCap(String maPhanAnh, EnumMucDoKhanCap mucDoMoi) {
        PhanAnh pa = phanAnhRepository.findById(maPhanAnh)
                .orElseThrow(() -> new EntityNotFoundException(("Không tìm thấy phản ánh: " + maPhanAnh)));

        if (pa.getTrangThaiHienTai() == EnumTrangThai.DA_XU_LY) {
            throw new RuntimeException("Hồ sơ đã đóng, không thể thay đổi mức độ khẩn cấp!");
        }

        pa.setMucDoKhanCap(mucDoMoi);
        return phanAnhRepository.save(pa);
    }
}
