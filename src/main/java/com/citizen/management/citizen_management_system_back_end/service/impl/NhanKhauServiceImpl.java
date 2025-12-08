package com.citizen.management.citizen_management_system_back_end.service.impl;

import com.citizen.management.citizen_management_system_back_end.dto.*;
import com.citizen.management.citizen_management_system_back_end.entity.*;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThaiNhanKhau;
import com.citizen.management.citizen_management_system_back_end.repository.*;
import com.citizen.management.citizen_management_system_back_end.service.NhanKhauService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.*;
import java.util.ArrayList;
import java.util.List;


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
            throw new IllegalArgumentException("Số CCCD đã tồn tại");
        }
        NhanKhau ent = new NhanKhau();
        BeanUtils.copyProperties(dto, ent);
        // set HoKhau nếu có
        if (dto.getMaHoKhau() != null) {
            HoKhau hk = hoKhauRepository.findById(dto.getMaHoKhau())
                    .orElseThrow(() -> new RuntimeException("HoKhau không tồn tại"));
            ent.setHoKhau(hk);
        }
        // Set default status if null
        if (ent.getStatus() == null) ent.setStatus(EnumTrangThaiNhanKhau.THUONG_TRU.getValue());
        nhanKhauRepository.save(ent);
        return convertToDto(ent);
    }

    /**
     * Updates citizen information. Note: The 'status' field cannot be modified through this method.
     * Status changes must be performed through dedicated methods:
     * - registerTamTru() to set status to "TAM_TRU"
     * - registerTamVang() to set status to "TAM_VANG"
     * - declareDeath() to set status to "KHAI_TU"
     */
    @Override
    @Transactional
    public NhanKhauDto update(String maNhanKhau, NhanKhauDto dto) {
        NhanKhau ent = nhanKhauRepository.findById(maNhanKhau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu"));
        
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
                    .orElseThrow(() -> new RuntimeException("HoKhau không tồn tại"));
            ent.setHoKhau(hk);
        }
        // cập nhật soCCCD: kiểm tra trùng
        if (dto.getSoCCCD() != null && !dto.getSoCCCD().equals(ent.getSoCCCD())) {
            if (nhanKhauRepository.existsBySoCCCD(dto.getSoCCCD())) {
                throw new IllegalArgumentException("Số CCCD đã tồn tại");
            }
            ent.setSoCCCD(dto.getSoCCCD());
        }
        nhanKhauRepository.save(ent);
        return convertToDto(ent);
    }

    @Override
    @Transactional
    public void delete(String maNhanKhau) {
        // recommendation: soft-delete — nhưng nếu bạn muốn hard delete:
        nhanKhauRepository.deleteById(maNhanKhau);
    }

    @Override
    public NhanKhauDto getById(String maNhanKhau) {
        NhanKhau ent = nhanKhauRepository.findById(maNhanKhau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu"));
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
                preds.add(cb.equal(root.get("status"), criteria.getStatus()));
            }
            if (criteria.getMaHoKhau() != null && !criteria.getMaHoKhau().isBlank()) {
                preds.add(cb.equal(root.get("hoKhau").get("maHoKhau"), criteria.getMaHoKhau()));
            }
            // age filter
            if (criteria.getAgeFrom() != null || criteria.getAgeTo() != null) {
                // chuyển tuổi -> khoảng ngày sinh
                LocalDate now = LocalDate.now();
                if (criteria.getAgeFrom() != null) {
                    LocalDate maxDob = now.minusYears(criteria.getAgeFrom());
                    preds.add(cb.lessThanOrEqualTo(root.get("ngaySinh").as(LocalDate.class), maxDob));
                }
                if (criteria.getAgeTo() != null) {
                    LocalDate minDob = now.minusYears(criteria.getAgeTo() + 1).plusDays(1);
                    preds.add(cb.greaterThanOrEqualTo(root.get("ngaySinh").as(LocalDate.class), minDob));
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
        NhanKhau nk = nhanKhauRepository.findById(dto.getMaNhanKhau())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu"));
        
        // Kiểm tra xem đã có đăng ký tạm trú đang active không
        boolean hasActiveTamTru = nk.getDanhSachTamTru() != null && 
            nk.getDanhSachTamTru().stream()
                .anyMatch(tt -> tt.getNgayKetThuc() == null || 
                            tt.getNgayKetThuc().after(new java.util.Date()));
        
        if (hasActiveTamTru) {
            throw new RuntimeException("Nhân khẩu đã có đăng ký tạm trú đang hoạt động");
        }
        
        TamTru tt = new TamTru();
        BeanUtils.copyProperties(dto, tt);
        tt.setNhanKhau(nk);
        
        // Update citizen status and save within the same transaction
        nk.setStatus(EnumTrangThaiNhanKhau.TAM_TRU.getValue());
        TamTru saved = tamTruRepository.save(tt);
        nhanKhauRepository.save(nk);

        TamTruDto out = new TamTruDto();
        BeanUtils.copyProperties(saved, out);
        out.setMaNhanKhau(nk.getMaNhanKhau());
        return out;
    }

    @Override
    @Transactional
    public TamVangDto registerTamVang(TamVangDto dto) {
        NhanKhau nk = nhanKhauRepository.findById(dto.getMaNhanKhau())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu"));
        
        // Kiểm tra xem đã có đăng ký tạm vắng đang active không
        boolean hasActiveTamVang = nk.getDanhSachTamVang() != null && 
            nk.getDanhSachTamVang().stream()
                .anyMatch(tv -> tv.getNgayKetThuc() == null || 
                            tv.getNgayKetThuc().after(new java.util.Date()));
        
        if (hasActiveTamVang) {
            throw new RuntimeException("Nhân khẩu đã có đăng ký tạm vắng đang hoạt động");
        }
        
        TamVang tv = new TamVang();
        BeanUtils.copyProperties(dto, tv);
        tv.setNhanKhau(nk);
        
        // Update citizen status and save within the same transaction
        nk.setStatus(EnumTrangThaiNhanKhau.TAM_VANG.getValue());
        TamVang saved = tamVangRepository.save(tv);
        nhanKhauRepository.save(nk);

        TamVangDto out = new TamVangDto();
        BeanUtils.copyProperties(saved, out);
        out.setMaNhanKhau(nk.getMaNhanKhau());
        return out;
    }

    @Override
    @Transactional
    public NhanKhauDto declareDeath(String maNhanKhau) {
        NhanKhau nk = nhanKhauRepository.findById(maNhanKhau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu"));
        nk.setStatus(EnumTrangThaiNhanKhau.KHAI_TU.getValue());
        // Depending on business requirements, the hoKhau field can be set to null or the individual can be removed from the household.
        nhanKhauRepository.save(nk);
        return convertToDto(nk);
    }

    /**
     * Helper method to convert NhanKhau entity to NhanKhauDto.
     * Handles copying properties and extracting maHoKhau if hoKhau is present.
     * 
     * @param entity The NhanKhau entity to convert
     * @return NhanKhauDto with all properties copied from the entity
     */
    private NhanKhauDto convertToDto(NhanKhau entity) {
        NhanKhauDto dto = new NhanKhauDto();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getHoKhau() != null) {
            dto.setMaHoKhau(entity.getHoKhau().getMaHoKhau());
        }
        return dto;
    }
}
