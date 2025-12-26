package com.citizen.management.citizen_management_system_back_end.service.impl;

import com.citizen.management.citizen_management_system_back_end.dto.NhanKhauDto;
import com.citizen.management.citizen_management_system_back_end.dto.SearchNhanKhauCriteria;
import com.citizen.management.citizen_management_system_back_end.dto.TamTruDto;
import com.citizen.management.citizen_management_system_back_end.dto.TamVangDto;
import com.citizen.management.citizen_management_system_back_end.entity.*;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThaiNhanKhau;
import com.citizen.management.citizen_management_system_back_end.repository.NhanKhauRepository;
import com.citizen.management.citizen_management_system_back_end.repository.HoKhauRepository;
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
        // validate
        if (dto.getSoCCCD() != null && nhanKhauRepository.existsBySoCCCD(dto.getSoCCCD())) {
            throw new IllegalArgumentException("Số CCCD " + dto.getSoCCCD() + " đã tồn tại trong hệ thống");
        }
        NhanKhau ent = new NhanKhau();
        BeanUtils.copyProperties(dto, ent); // Giờ trangThai đã cùng kiểu enum
        
        // Sinh mã nhân khẩu tự động (NK + 3 chữ số)
        ent.setMaNhanKhau(generateMaNhanKhau());
        
        // Set default status if null
        if (ent.getTrangThai() == null) {
            ent.setTrangThai(EnumTrangThaiNhanKhau.THUONG_TRU);
        }
        
        // set HoKhau nếu có
        if (dto.getMaHoKhau() != null) {
            HoKhau hk = hoKhauRepository.findById(dto.getMaHoKhau())
                    .orElseThrow(() -> new RuntimeException("HoKhau với mã " + dto.getMaHoKhau() + " không tồn tại"));
            ent.setHoKhau(hk);
        }
        
        try {
            nhanKhauRepository.save(ent);
        } catch (DataIntegrityViolationException e) {
            // Handle race condition: another transaction inserted duplicate CCCD
            if (dto.getSoCCCD() != null && e.getMessage() != null && e.getMessage().contains("so_cccd")) {
                throw new IllegalArgumentException("Số CCCD " + dto.getSoCCCD() + " đã tồn tại trong hệ thống");
            }
            throw e;
        }
        return convertToDto(ent);
    }

    /**
     * Cập nhật thông tin nhân khẩu. Lưu ý: Trường 'trangThai' không thể thay đổi thông qua phương thức này.
     * Thay đổi trạng thái phải được thực hiện thông qua các phương thức chuyên biệt:
     * - registerTamTru() để đặt trạng thái thành "TAM_TRU"
     * - registerTamVang() để đặt trạng thái thành "TAM_VANG"
     * - declareDeath() để đặt trạng thái thành "KHAI_TU"
     */
    @Override
    @Transactional
    public NhanKhauDto update(String maNhanKhau, NhanKhauDto dto) {
        if (maNhanKhau == null) {
            throw new IllegalArgumentException("Mã nhân khẩu không được null");
        }
        NhanKhau ent = nhanKhauRepository.findById(maNhanKhau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với mã " + maNhanKhau));
        
        // Update allowed fields (status field is intentionally not updated here)
        ent.setHoTen(dto.getHoTen());
        ent.setNgaySinh(dto.getNgaySinh());
        ent.setGioiTinh(dto.getGioiTinh());
        ent.setQueQuan(dto.getQueQuan());
        ent.setDanToc(dto.getDanToc());
        ent.setQuanHeVoiChuHo(dto.getQuanHeVoiChuHo());
        // nếu đổi hộ khẩu
        if (dto.getMaHoKhau() != null) {
            HoKhau hk = hoKhauRepository.findById(dto.getMaHoKhau())
                    .orElseThrow(() -> new RuntimeException("HoKhau với mã " + dto.getMaHoKhau() + " không tồn tại"));
            ent.setHoKhau(hk);
        }
        // cập nhật soCCCD: kiểm tra trùng
        if (dto.getSoCCCD() != null && !dto.getSoCCCD().equals(ent.getSoCCCD())) {
            if (nhanKhauRepository.existsBySoCCCD(dto.getSoCCCD())) {
                throw new IllegalArgumentException("Số CCCD " + dto.getSoCCCD() + " đã tồn tại trong hệ thống");
            }
            ent.setSoCCCD(dto.getSoCCCD());
        }
        
        try {
            nhanKhauRepository.save(ent);
        } catch (DataIntegrityViolationException e) {
            // Handle race condition: another transaction inserted duplicate CCCD
            if (dto.getSoCCCD() != null && e.getMessage() != null && e.getMessage().contains("so_cccd")) {
                throw new IllegalArgumentException("Số CCCD " + dto.getSoCCCD() + " đã tồn tại trong hệ thống");
            }
            throw e;
        }
        return convertToDto(ent);
    }

    @Override
    @Transactional
    public void delete(String maNhanKhau) {
        if (maNhanKhau == null) {
            throw new IllegalArgumentException("Mã nhân khẩu không được null");
        }
        // recommendation: soft-delete — nhưng nếu bạn muốn hard delete:
        nhanKhauRepository.deleteById(maNhanKhau);
    }

    @Override
    public NhanKhauDto getById(String maNhanKhau) {
        if (maNhanKhau == null) {
            throw new IllegalArgumentException("Mã nhân khẩu không được null");
        }
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
                        cb.like(cb.lower(root.get("soCCCD")), like)
                ));
            }
            if (criteria.getGioiTinh() != null && !criteria.getGioiTinh().isBlank()) {
                preds.add(cb.equal(root.get("gioiTinh"), criteria.getGioiTinh()));
            }
            if (criteria.getStatus() != null && !criteria.getStatus().isBlank()) {
                try {
                    EnumTrangThaiNhanKhau trangThai = EnumTrangThaiNhanKhau.valueOf(criteria.getStatus());
                    preds.add(cb.equal(root.get("trangThai"), trangThai));
                } catch (IllegalArgumentException e) {
                    // Invalid status value, skip this filter
                }
            }
            if (criteria.getMaHoKhau() != null && !criteria.getMaHoKhau().isBlank()) {
                preds.add(cb.equal(root.get("hoKhau").get("maHoKhau"), criteria.getMaHoKhau()));
            }
            // Age filter - convert LocalDate to java.util.Date for comparison
            if (criteria.getAgeFrom() != null || criteria.getAgeTo() != null) {
                LocalDate now = LocalDate.now();
                if (criteria.getAgeFrom() != null) {
                    // Calculate maximum date of birth (oldest person in age range)
                    LocalDate maxDobLocal = now.minusYears(criteria.getAgeFrom());
                    java.util.Date maxDob = java.sql.Date.valueOf(maxDobLocal);
                    preds.add(cb.lessThanOrEqualTo(root.get("ngaySinh"), maxDob));
                }
                if (criteria.getAgeTo() != null) {
                    // Calculate minimum date of birth (youngest person in age range)
                    LocalDate minDobLocal = now.minusYears(criteria.getAgeTo() + 1).plusDays(1);
                    java.util.Date minDob = java.sql.Date.valueOf(minDobLocal);
                    preds.add(cb.greaterThanOrEqualTo(root.get("ngaySinh"), minDob));
                }
            }
            return cb.and(preds.toArray(new Predicate[0]));
        };

        Page<NhanKhau> page = nhanKhauRepository.findAll(spec, pageable);
        return page.map(this::convertToDto);
    }

    @Override
    @Transactional
    public TamTruDto registerTamTru(TamTruDto dto) {
        if (dto.getMaNhanKhau() == null) {
            throw new IllegalArgumentException("Mã nhân khẩu không được null");
        }
        NhanKhau nk = nhanKhauRepository.findById(dto.getMaNhanKhau())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với mã " + dto.getMaNhanKhau()));
        
        // Validate current status before allowing transition to TAM_TRU
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
        
        // Update citizen status FIRST and save to ensure status is persisted
        nk.setTrangThai(EnumTrangThaiNhanKhau.TAM_TRU);
        nhanKhauRepository.save(nk);
        nhanKhauRepository.flush(); // Force immediate commit to database
        
        // Then save the TamTru record
        TamTru saved = tamTruRepository.save(tt);

        TamTruDto out = new TamTruDto();
        BeanUtils.copyProperties(saved, out);
        out.setMaNhanKhau(nk.getMaNhanKhau());
        return out;
    }

    @Override
    @Transactional
    public TamVangDto registerTamVang(TamVangDto dto) {
        if (dto.getMaNhanKhau() == null) {
            throw new IllegalArgumentException("Mã nhân khẩu không được null");
        }
        NhanKhau nk = nhanKhauRepository.findById(dto.getMaNhanKhau())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với mã " + dto.getMaNhanKhau()));
        
        // Validate current status before allowing transition to TAM_VANG
        EnumTrangThaiNhanKhau trangThaiHienTai = nk.getTrangThai();
        if (EnumTrangThaiNhanKhau.TAM_TRU.equals(trangThaiHienTai)) {
            throw new IllegalStateException("Không thể đăng ký tạm vắng cho nhân khẩu đang tạm trú");
        }
        if (EnumTrangThaiNhanKhau.KHAI_TU.equals(trangThaiHienTai)) {
            throw new IllegalStateException("Không thể đăng ký tạm vắng cho nhân khẩu đã khai tử");
        }
        
        // Tự động kết thúc các đăng ký tạm vắng cũ đang active
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
        
        // Update citizen status FIRST and save to ensure status is persisted
        nk.setTrangThai(EnumTrangThaiNhanKhau.TAM_VANG);
        nhanKhauRepository.save(nk);
        nhanKhauRepository.flush(); // Force immediate commit to database
        
        // Then save the TamVang record
        TamVang saved = tamVangRepository.save(tv);

        TamVangDto out = new TamVangDto();
        BeanUtils.copyProperties(saved, out);
        out.setMaNhanKhau(nk.getMaNhanKhau());
        return out;
    }

    @Override
    @Transactional
    public NhanKhauDto declareDeath(String maNhanKhau) {
        if (maNhanKhau == null) {
            throw new IllegalArgumentException("Mã nhân khẩu không được null");
        }
        NhanKhau nk = nhanKhauRepository.findById(maNhanKhau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với mã " + maNhanKhau));
        
        // Validate current status before allowing death declaration
        EnumTrangThaiNhanKhau trangThaiHienTai = nk.getTrangThai();
        if (EnumTrangThaiNhanKhau.KHAI_TU.equals(trangThaiHienTai)) {
            throw new IllegalStateException("Nhân khẩu đã được khai tử trước đó");
        }
        
        nk.setTrangThai(EnumTrangThaiNhanKhau.KHAI_TU);
        // Depending on business requirements, the hoKhau field can be set to null or the individual can be removed from the household.
        nhanKhauRepository.save(nk);
        return convertToDto(nk);
    }

    /**
     * Phương thức helper để chuyển đổi NhanKhau entity sang NhanKhauDto.
     * Xử lý sao chép thuộc tính và trích xuất maHoKhau nếu hoKhau tồn tại.
     * 
     * @param entity Entity NhanKhau cần chuyển đổi
     * @return NhanKhauDto với tất cả thuộc tính được sao chép từ entity
     */
    private NhanKhauDto convertToDto(NhanKhau entity) {
        NhanKhauDto dto = new NhanKhauDto();
        BeanUtils.copyProperties(entity, dto); // Giờ trangThai cùng kiểu enum
        if (entity.getHoKhau() != null) {
            dto.setMaHoKhau(entity.getHoKhau().getMaHoKhau());
        }
        return dto;
    }
    
    /**
     * Sinh mã nhân khẩu tự động dạng NK001, NK002, ...
     */
    private String generateMaNhanKhau() {
        // Lấy số thứ tự lớn nhất hiện có
        Long count = nhanKhauRepository.count();
        int nextNumber = count.intValue() + 1;
        
        // Sinh mã với 3 chữ số, padding 0 bên trái
        return String.format("NK%03d", nextNumber);
    }
    
    /**
     * Sinh mã tạm trú tự động dạng TT001, TT002, ...
     */
    private String generateMaTamTru() {
        Long count = tamTruRepository.count();
        int nextNumber = count.intValue() + 1;
        return String.format("TT%03d", nextNumber);
    }
    
    /**
     * Sinh mã tạm vắng tự động dạng TV001, TV002, ...
     */
    private String generateMaTamVang() {
        Long count = tamVangRepository.count();
        int nextNumber = count.intValue() + 1;
        return String.format("TV%03d", nextNumber);
    }

    /**
     * Lấy thông tin nhân khẩu và danh sách thành viên cùng hộ khẩu
     * Dùng cho công dân xem thông tin của mình và hộ khẩu
     */
    @Override
    public Map<String, Object> layThongTinNhanKhauVaHoKhau(String maNhanKhau) {
        // Lấy thông tin nhân khẩu
        NhanKhau nhanKhau = nhanKhauRepository.findById(maNhanKhau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu với mã " + maNhanKhau));
        
        Map<String, Object> result = new HashMap<>();
        result.put("nhanKhau", convertToDto(nhanKhau));
        
        // Lấy thông tin hộ khẩu và thành viên
        if (nhanKhau.getHoKhau() != null) {
            HoKhau hoKhau = nhanKhau.getHoKhau();
            
            // Thông tin hộ khẩu
            Map<String, Object> hoKhauInfo = new HashMap<>();
            hoKhauInfo.put("maHoKhau", hoKhau.getMaHoKhau());
            hoKhauInfo.put("diaChi", hoKhau.getDiaChi());
            hoKhauInfo.put("ngayDangKy", hoKhau.getNgayDangKy());
            
            // Thông tin chủ hộ
            if (hoKhau.getChuHo() != null) {
                hoKhauInfo.put("chuHo", convertToDto(hoKhau.getChuHo()));
            }
            
            result.put("hoKhau", hoKhauInfo);
            
            // Danh sách thành viên cùng hộ khẩu (không bao gồm bản thân)
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
