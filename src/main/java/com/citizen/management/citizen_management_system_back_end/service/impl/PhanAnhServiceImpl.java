package com.citizen.management.citizen_management_system_back_end.service.impl;

import com.citizen.management.citizen_management_system_back_end.dto.*;
import com.citizen.management.citizen_management_system_back_end.entity.LichSuPhanAnh;
import com.citizen.management.citizen_management_system_back_end.entity.PhanAnh;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.entity.TepDinhKem;
import com.citizen.management.citizen_management_system_back_end.enums.EnumHanhDong;
import com.citizen.management.citizen_management_system_back_end.enums.EnumMucDoKhanCap;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThai;
import com.citizen.management.citizen_management_system_back_end.repository.LichSuPhanAnhRepository;
import com.citizen.management.citizen_management_system_back_end.repository.PhanAnhRepository;
import com.citizen.management.citizen_management_system_back_end.repository.TaiKhoanRepository;
import com.citizen.management.citizen_management_system_back_end.repository.TepDinhKemRepository;
import com.citizen.management.citizen_management_system_back_end.service.IPhanAnhService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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

    @Override
    @Transactional
    public PhanAnh guiPhanAnh(GuiPhanAnhRequest request, TaiKhoan nguoiGui) {
        //He thong tiep nhan
        //1.Tao phan anh moi
        PhanAnh pa = new PhanAnh();
        pa.setTieuDe(request.getTieuDe());
        pa.setLinhVuc(request.getLinhVuc());
        pa.setNguoiGui(nguoiGui);

        //Set trang thai dau
        pa.setTrangThaiHienTai(EnumTrangThai.CHO);
        pa.setMucDoKhanCap(EnumMucDoKhanCap.THAP);

        //Luu de lay ID
        PhanAnh paDaLuu = phanAnhRepository.save(pa);

        //2.Tao lich su dau tien
        LichSuPhanAnh ls = new LichSuPhanAnh();
        ls.setPhanAnh(paDaLuu);
        ls.setTaiKhoanThucHien(nguoiGui);
        ls.setThoiGian(new Date());
        ls.setHanhDong(EnumHanhDong.TAO_MOI);
        ls.setNoiDung(request.getNoiDung());

        lichSuRepository.save(ls);

        return paDaLuu;
    }

    @Override
    @Transactional
    public PhanAnh phanCongXuLy(String maPhanAnh, PhanCongRequest request, TaiKhoan nguoiPhanCong) {
        //1.Tim can bo nhan viec
        TaiKhoan canBoDuocGiao = taiKhoanRepository.findById(request.getMaCanBoPhuTrach()).orElseThrow(() -> new EntityNotFoundException("Khong tim thay can bo voi ma: " + request.getMaCanBoPhuTrach()));

        //2.Tim phan anh
        PhanAnh pa = phanAnhRepository.findById(maPhanAnh).orElseThrow(() -> new EntityNotFoundException("Khong tim thay Phan anh voi ma: " + maPhanAnh));

        //3.Cap nhat thong tin
        pa.setCanBoPhuTrach(canBoDuocGiao);
        pa.setThoiHanXuLy(request.getThoiHanXuLy());
        pa.setTrangThaiHienTai(EnumTrangThai.DANG_XU_LY);

        //4.Luu
        PhanAnh paDaCapNhat = phanAnhRepository.save(pa);

        //5.Tao lich su nhan viec
        LichSuPhanAnh ls = new LichSuPhanAnh();
        ls.setPhanAnh(paDaCapNhat);
        ls.setTaiKhoanThucHien(nguoiPhanCong);
        ls.setThoiGian(new Date());
        ls.setHanhDong(EnumHanhDong.PHAN_CONG);
        ls.setTrangThaiMoi(EnumTrangThai.DANG_XU_LY);
        //Ghi chu
        ls.setNoiDung("Phan cong cho can bo: " + canBoDuocGiao.getTenDangNhap());

        lichSuRepository.save(ls);
        return paDaCapNhat;
    }

    @Override
    @Transactional
    public void capNhatXuLyNoiBo(String maPhanAnh, XuLyNoiBoRequest request, TaiKhoan canBoXuLy) {
        //1.Tim phan anh
        PhanAnh pa = phanAnhRepository.findById(maPhanAnh).orElseThrow(() -> new EntityNotFoundException("Khong tim thay Phan anh: " + maPhanAnh));

        if (pa.getTrangThaiHienTai() == EnumTrangThai.CHO) {
            pa.setTrangThaiHienTai(EnumTrangThai.DANG_XU_LY);
            phanAnhRepository.save(pa);
        }

        //2.Tao lich su ghi nhan viec xu ly noi bo
        LichSuPhanAnh ls = new LichSuPhanAnh();
        ls.setPhanAnh(pa);
        ls.setTaiKhoanThucHien(canBoXuLy);
        ls.setThoiGian(new Date());
        ls.setHanhDong(EnumHanhDong.XU_LY);
        ls.setNoiDung(request.getNoiDungCapNhat());
        ls.setTrangThaiMoi(pa.getTrangThaiHienTai());

        lichSuRepository.save(ls);

        //3.Xu ly file dinh kem
        if (request.getDanhSachFileUrl() != null && !request.getDanhSachFileUrl().isEmpty()) {
            List<TepDinhKem> tepMoiList = new ArrayList<>();
            for (String fileUrl : request.getDanhSachFileUrl()) {
                TepDinhKem tep = new TepDinhKem();
                tep.setPhanAnh(pa);
                tep.setUrl(fileUrl);
                tep.setTenFileGoc("File_tu_can_bo_xu_ly.jpg"); //FE xu ly sau
                tepMoiList.add(tep);
            }
            //Luu tat ca file vao CSDL
            tepDinhKemRepository.saveAll(tepMoiList);
        }
    }

    @Override
    @Transactional
    public PhanAnh phanHoiCongDan(String maPhanAnh, PhanHoiRequest request, TaiKhoan canBoPhanHoi) {
        //1.Tim phan anh
        PhanAnh pa = phanAnhRepository.findById((maPhanAnh)).orElseThrow(() -> new EntityNotFoundException("Khong tim thay Phan anh: " + maPhanAnh));

        //2.Cap nhat trang thai
        pa.setTrangThaiHienTai(EnumTrangThai.DA_XU_LY);
        PhanAnh paDaCapNhat = phanAnhRepository.save(pa);

        //3.Tao lich su ghi nhan phan hoi
        LichSuPhanAnh ls = new LichSuPhanAnh();
        ls.setPhanAnh(paDaCapNhat);
        ls.setTaiKhoanThucHien(canBoPhanHoi);
        ls.setThoiGian(new Date());
        ls.setHanhDong(EnumHanhDong.PHAN_HOI);
        ls.setNoiDung(request.getNoiDungPhanHoi());
        ls.setTrangThaiMoi(EnumTrangThai.DA_XU_LY);

        lichSuRepository.save(ls);

        //Todo: NotificationService de gui Email/SMS cho cong dan
        //notificationService.send(pa.getNguoiGui(), "Phản ánh của bạn đã được trả lời.");

        return paDaCapNhat;
    }

    @Override
    @Transactional
    public PhanAnh danhGiaPhanHoi(String maPhanAnh, DanhGiaRequest request, TaiKhoan nguoiDanhGia) {
        //1.TIm phan anh
        PhanAnh pa = phanAnhRepository.findById(maPhanAnh).orElseThrow(() -> new EntityNotFoundException("Khong thay Phan anh: " + maPhanAnh));

        //2.Kiem tra bao mat
        if (!pa.getNguoiGui().getMaTaiKhoan().equals(nguoiDanhGia.getMaTaiKhoan())) {
            throw new RuntimeException("Ban khong co quyen danh gia phan anh nay!");
        }

        //3.Dam bao chi danh gia khi da xu ly xong
        if (pa.getTrangThaiHienTai() != EnumTrangThai.DA_XU_LY) {
            throw new RuntimeException("Phan anh nay chua xu ly xong!");
        }

        //4.Cap nhat danh gia
        pa.setDanhGiaHaiLong(request.getDanhGiaHaiLong());
        pa.setGopY(request.getGopY());

        return phanAnhRepository.save(pa);
    }

    @Override
    public List<PhanAnh> layDanhSachPhanAnhCuaToi(TaiKhoan nguoiGui) {
        return phanAnhRepository.findAllByNguoiGui(nguoiGui); // Lay danh sach
    }
}
