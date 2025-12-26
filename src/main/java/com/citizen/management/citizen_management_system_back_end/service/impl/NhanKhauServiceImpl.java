package com.citizen.management.citizen_management_system_back_end.service.impl;

import com.citizen.management.citizen_management_system_back_end.dto.NhanKhauDto;
import com.citizen.management.citizen_management_system_back_end.dto.SearchNhanKhauCriteria;
import com.citizen.management.citizen_management_system_back_end.dto.TamTruDto;
import com.citizen.management.citizen_management_system_back_end.dto.TamVangDto;
import com.citizen.management.citizen_management_system_back_end.entity.*;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThaiNhanKhau;
import com.citizen.management.citizen_management_system_back_end.repository.HoKhauRepository;
import com.citizen.management.citizen_management_system_back_end.repository.NhanKhauRepository;
import com.citizen.management.citizen_management_system_back_end.repository.TamTruRepository;
import com.citizen.management.citizen_management_system_back_end.repository.TamVangRepository;
import com.citizen.management.citizen_management_system_back_end.service.NhanKhauService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NhanKhauServiceImpl implements NhanKhauService {

    private final NhanKhauRepository nhanKhauRepository;
    private final HoKhauRepository hoKhauRepository;
    private final TamTruRepository tamTruRepository;
    private final TamVangRepository tamVangRepository;

    @Override
    @Transactional
    public NhanKhauDto create(NhanKhauDto dto) {
        // Validate CCCD trùng
        if (dto.getSoCCCD() != null && nhanKhauRepository.existsBySoCCCD(dto.getSoCCCD())) {
            throw new IllegalArgumentException("Số CCCD " + dto.getSoCCCD() + " đã tồn tại trong hệ thống");
        }
        
        NhanKhau ent = new NhanKhau();
        BeanUtils.copyProperties(dto, ent);
        
        // Sinh mã nhân khẩu tự động
        ent.setMaNhanKhau(generateMaNhanKhau());
        
        // Set trạng thái mặc định
        if (ent.getTrangThai() == null) {
            ent.setTrangThai(EnumTrangThaiNhanKhau.THUONG_TRU);
        }
        
        // Set hộ khẩu
        if (dto.getMaHoKhau() != null) {
            HoKhau hk = hoKhauRepository.findById(dto.getMaHoKhau())
                    .orElseThrow(() -> new RuntimeException("HoKhau với mã " + dto.getMaHoKhau() + " không tồn tại"));
            ent.setHoKhau(hk);
        }

        try {
            nhanKhauRepository.save(ent);
        } catch (DataIntegrityViolationException e) {
            if (dto.getSoCCCD() != null && e.getMessage() != null && e.getMessage().contains("so_cccd")) {
                throw new IllegalArgumentException("Số CCCD " + dto.getSoCCCD() + " đã tồn tại trong hệ thống");
            }
            throw e;
        }
        return convertToDto(ent);
    }

    @Override
    @Transactional
    public NhanKhauDto update(String maNhanKhau, NhanKhauDto dto) {
        if (maNhanKhau == null) throw new IllegalArgumentException("Mã nhân khẩu không được null");
        
        NhanKhau ent = nhanKhauRepository.findById(maNhanKhau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với mã " + maNhanKhau));

        ent.setHoTen(dto.getHoTen());
        ent.setNgaySinh(dto.getNgaySinh());
        ent.setGioiTinh(dto.getGioiTinh());
        ent.setQueQuan(dto.getQueQuan());
        ent.setDanToc(dto.getDanToc());
        ent.setQuanHeVoiChuHo(dto.getQuanHeVoiChuHo());
        
        if (dto.getMaHoKhau() != null) {
            HoKhau hk = hoKhauRepository.findById(dto.getMaHoKhau())
                    .orElseThrow(() -> new RuntimeException("HoKhau với mã " + dto.getMaHoKhau() + " không tồn tại"));
            ent.setHoKhau(hk);
        }
        
        if (dto.getSoCCCD() != null && !dto.getSoCCCD().equals(ent.getSoCCCD())) {
            if (nhanKhauRepository.existsBySoCCCD(dto.getSoCCCD())) {
                throw new IllegalArgumentException("Số CCCD " + dto.getSoCCCD() + " đã tồn tại trong hệ thống");
            }
            ent.setSoCCCD(dto.getSoCCCD());
        }

        nhanKhauRepository.save(ent);
        return convertToDto(ent);
    }

    @Override
    @Transactional
    public void delete(String maNhanKhau) {
        if (maNhanKhau == null) throw new IllegalArgumentException("Mã nhân khẩu không được null");
        nhanKhauRepository.deleteById(maNhanKhau);
    }

    @Override
    public NhanKhauDto getById(String maNhanKhau) {
        NhanKhau ent = nhanKhauRepository.findById(maNhanKhau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với mã " + maNhanKhau));
        return convertToDto(ent);
    }

    @Override
    public Page<NhanKhauDto> search(SearchNhanKhauCriteria criteria, Pageable pageable) {
        Specification<NhanKhau> spec = (root, query, cb) -> {
            List<Predicate> preds = new ArrayList<>();
            if (criteria.getQ() != null && !criteria.getQ().isBlank()) {
                String like = "%" + criteria.getQ().toLowerCase() + "%";
                preds.add(cb.or(
                        cb.like(cb.lower(root.get("hoTen")), like),
                        cb.like(cb.lower(root.get("soCCCD")), like)));
            }
            if (criteria.getGioiTinh() != null && !criteria.getGioiTinh().isBlank()) {
                preds.add(cb.equal(root.get("gioiTinh"), criteria.getGioiTinh()));
            }
            if (criteria.getStatus() != null && !criteria.getStatus().isBlank()) {
                try {
                    EnumTrangThaiNhanKhau trangThai = EnumTrangThaiNhanKhau.valueOf(criteria.getStatus());
                    preds.add(cb.equal(root.get("trangThai"), trangThai));
                } catch (IllegalArgumentException e) { }
            }
            if (criteria.getMaHoKhau() != null && !criteria.getMaHoKhau().isBlank()) {
                preds.add(cb.equal(root.get("hoKhau").get("maHoKhau"), criteria.getMaHoKhau()));
            }
            if (criteria.getAgeFrom() != null || criteria.getAgeTo() != null) {
                LocalDate now = LocalDate.now();
                if (criteria.getAgeFrom() != null) {
                    java.util.Date maxDob = java.sql.Date.valueOf(now.minusYears(criteria.getAgeFrom()));
                    preds.add(cb.lessThanOrEqualTo(root.get("ngaySinh"), maxDob));
                }
                if (criteria.getAgeTo() != null) {
                    java.util.Date minDob = java.sql.Date.valueOf(now.minusYears(criteria.getAgeTo() + 1).plusDays(1));
                    preds.add(cb.greaterThanOrEqualTo(root.get("ngaySinh"), minDob));
                }
            }
            return cb.and(preds.toArray(new Predicate[0]));
        };

        return nhanKhauRepository.findAll(spec, pageable).map(this::convertToDto);
    }

    @Override
    @Transactional
    public TamTruDto registerTamTru(TamTruDto dto) {
        if (dto.getMaNhanKhau() == null) throw new IllegalArgumentException("Mã nhân khẩu không được null");
        
        NhanKhau nk = nhanKhauRepository.findById(dto.getMaNhanKhau())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với mã " + dto.getMaNhanKhau()));

        // Validate trạng thái
        EnumTrangThaiNhanKhau trangThaiHienTai = nk.getTrangThai();
        if (EnumTrangThaiNhanKhau.TAM_VANG.equals(trangThaiHienTai)) {
            throw new IllegalStateException("Không thể đăng ký tạm trú cho nhân khẩu đang tạm vắng");
        }
        if (EnumTrangThaiNhanKhau.KHAI_TU.equals(trangThaiHienTai)) {
            throw new IllegalStateException("Không thể đăng ký tạm trú cho nhân khẩu đã khai tử");
        }

        // Tự động kết thúc các đăng ký tạm trú cũ đang active
        if (nk.getDanhSachTamTru() != null) {
            java.util.Date now = new java.util.Date();
            nk.getDanhSachTamTru().stream()
                .filter(tt -> tt.getNgayKetThuc() == null || tt.getNgayKetThuc().after(now))
                .forEach(tt -> {
                    tt.setNgayKetThuc(now);
                    tamTruRepository.save(tt);
                });
        }

        TamTru tt = new TamTru();
        BeanUtils.copyProperties(dto, tt);
        tt.setMaTamTru(generateMaTamTru());
        tt.setNhanKhau(nk);

        // Cập nhật trạng thái nhân khẩu trước
        nk.setTrangThai(EnumTrangThaiNhanKhau.TAM_TRU);
        nhanKhauRepository.save(nk);
        nhanKhauRepository.flush(); // Đẩy xuống DB ngay

        TamTru saved = tamTruRepository.save(tt);

        TamTruDto out = new TamTruDto();
        BeanUtils.copyProperties(saved, out);
        out.setMaNhanKhau(nk.getMaNhanKhau());
        return out;
    }

    @Override
    @Transactional
    public TamVangDto registerTamVang(TamVangDto dto) {
        if (dto.getMaNhanKhau() == null) throw new IllegalArgumentException("Mã nhân khẩu không được null");
        
        NhanKhau nk = nhanKhauRepository.findById(dto.getMaNhanKhau())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với mã " + dto.getMaNhanKhau()));

        EnumTrangThaiNhanKhau trangThaiHienTai = nk.getTrangThai();
        if (EnumTrangThaiNhanKhau.TAM_TRU.equals(trangThaiHienTai)) {
            throw new IllegalStateException("Không thể đăng ký tạm vắng cho nhân khẩu đang tạm trú");
        }
        if (EnumTrangThaiNhanKhau.KHAI_TU.equals(trangThaiHienTai)) {
            throw new IllegalStateException("Không thể đăng ký tạm vắng cho nhân khẩu đã khai tử");
        }

        // Tự động kết thúc các đăng ký tạm vắng cũ
        if (nk.getDanhSachTamVang() != null) {
            java.util.Date now = new java.util.Date();
            nk.getDanhSachTamVang().stream()
                .filter(tv -> tv.getNgayKetThuc() == null || tv.getNgayKetThuc().after(now))
                .forEach(tv -> {
                    tv.setNgayKetThuc(now);
                    tamVangRepository.save(tv);
                });
        }

        TamVang tv = new TamVang();
        BeanUtils.copyProperties(dto, tv);
        tv.setMaTamVang(generateMaTamVang());
        tv.setNhanKhau(nk);

        nk.setTrangThai(EnumTrangThaiNhanKhau.TAM_VANG);
        nhanKhauRepository.save(nk);
        nhanKhauRepository.flush();

        TamVang saved = tamVangRepository.save(tv);

        TamVangDto out = new TamVangDto();
        BeanUtils.copyProperties(saved, out);
        out.setMaNhanKhau(nk.getMaNhanKhau());
        return out;
    }

    @Override
    @Transactional
    public NhanKhauDto declareDeath(String maNhanKhau) {
        if (maNhanKhau == null) throw new IllegalArgumentException("Mã nhân khẩu không được null");
        NhanKhau nk = nhanKhauRepository.findById(maNhanKhau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với mã " + maNhanKhau));

        if (EnumTrangThaiNhanKhau.KHAI_TU.equals(nk.getTrangThai())) {
            throw new IllegalStateException("Nhân khẩu đã được khai tử trước đó");
        }

        nk.setTrangThai(EnumTrangThaiNhanKhau.KHAI_TU);
        nhanKhauRepository.save(nk);
        return convertToDto(nk);
    }

    // === Helper Methods ===

    private NhanKhauDto convertToDto(NhanKhau entity) {
        NhanKhauDto dto = new NhanKhauDto();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getHoKhau() != null) {
            dto.setMaHoKhau(entity.getHoKhau().getMaHoKhau());
        }
        return dto;
    }
    
    private String generateMaNhanKhau() {
        Long count = nhanKhauRepository.count();
        int nextNumber = count.intValue() + 1;
        return String.format("NK%03d", nextNumber);
    }
    
    private String generateMaTamTru() {
        Long count = tamTruRepository.count();
        int nextNumber = count.intValue() + 1;
        return String.format("TT%03d", nextNumber);
    }
    
    private String generateMaTamVang() {
        Long count = tamVangRepository.count();
        int nextNumber = count.intValue() + 1;
        return String.format("TV%03d", nextNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> layThongTinNhanKhauVaHoKhau(String maNhanKhau) {
        NhanKhau nhanKhau = nhanKhauRepository.findById(maNhanKhau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với mã " + maNhanKhau));
        
        Map<String, Object> result = new HashMap<>();
        result.put("nhanKhau", convertToDto(nhanKhau));
        
        if (nhanKhau.getHoKhau() != null) {
            HoKhau hoKhau = nhanKhau.getHoKhau();
            
            Map<String, Object> hoKhauInfo = new HashMap<>();
            hoKhauInfo.put("maHoKhau", hoKhau.getMaHoKhau());
            hoKhauInfo.put("diaChi", hoKhau.getDiaChi());
            hoKhauInfo.put("ngayDangKy", hoKhau.getNgayDangKy());
            
            if (hoKhau.getChuHo() != null) {
                hoKhauInfo.put("chuHo", convertToDto(hoKhau.getChuHo()));
            }
            
            result.put("hoKhau", hoKhauInfo);
            
            List<NhanKhauDto> thanhVienCungHo = hoKhau.getDanhSachThanhVien().stream()
                    .filter(tv -> !tv.getMaNhanKhau().equals(maNhanKhau))
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            
            result.put("thanhVienCungHo", thanhVienCungHo);
        } else {
            result.put("hoKhau", null);
            result.put("thanhVienCungHo", new ArrayList<>());
        }
        
        return result;
    }
}