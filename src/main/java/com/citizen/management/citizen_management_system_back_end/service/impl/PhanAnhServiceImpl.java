package com.citizen.management.citizen_management_system_back_end.service.impl;

import com.citizen.management.citizen_management_system_back_end.dto.*;
import com.citizen.management.citizen_management_system_back_end.entity.*;
import com.citizen.management.citizen_management_system_back_end.enums.EnumHanhDong;
import com.citizen.management.citizen_management_system_back_end.enums.EnumMucDoKhanCap;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThai;
import com.citizen.management.citizen_management_system_back_end.repository.*;
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
    private final ThongBaoReposity thongBaoReposity;

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

        //3.Xu ly file dinh kem
        if (request.getDanhSachFileUrl() != null && !request.getDanhSachFileUrl().isEmpty()) {
            List<TepDinhKem> listTep = new ArrayList<>();

            for (String url : request.getDanhSachFileUrl()) {
                TepDinhKem tep = new TepDinhKem();
                tep.setPhanAnh(paDaLuu);
                tep.setUrl(url);
                tep.setTenFileGoc("Anh_dinh_kem_cong_dan"); //Temp co dinh
                listTep.add(tep);
            }

            //Luu file vao tep_dinh_kem
            tepDinhKemRepository.saveAll(listTep);
        }

        return paDaLuu;
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
        return lichSuRepository.findByPhanAnhOrderByThoiGianDesc(pa);
    }


}
