package com.citizen.management.citizen_management_system_back_end.service.impl;

import com.citizen.management.citizen_management_system_back_end.dto.*;
import com.citizen.management.citizen_management_system_back_end.entity.*;
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
        // mặc định status nếu null
        if (ent.getStatus() == null) ent.setStatus("THUONG_TRU");
        nhanKhauRepository.save(ent);

        NhanKhauDto out = new NhanKhauDto();
        BeanUtils.copyProperties(ent, out);
        if (ent.getHoKhau() != null) out.setMaHoKhau(ent.getHoKhau().getMaHoKhau());
        return out;
    }

    @Override
    @Transactional
    public NhanKhauDto update(String maNhanKhau, NhanKhauDto dto) {
        NhanKhau ent = nhanKhauRepository.findById(maNhanKhau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu"));
        // cập nhật các field cho phép
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
        NhanKhauDto out = new NhanKhauDto();
        BeanUtils.copyProperties(ent, out);
        if (ent.getHoKhau() != null) out.setMaHoKhau(ent.getHoKhau().getMaHoKhau());
        return out;
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
        NhanKhauDto dto = new NhanKhauDto();
        BeanUtils.copyProperties(ent, dto);
        if (ent.getHoKhau() != null) dto.setMaHoKhau(ent.getHoKhau().getMaHoKhau());
        return dto;
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
        return page.map(ent -> {
            NhanKhauDto dto = new NhanKhauDto();
            BeanUtils.copyProperties(ent, dto);
            if (ent.getHoKhau() != null) dto.setMaHoKhau(ent.getHoKhau().getMaHoKhau());
            return dto;
        });
    }

    @Override
    @Transactional
    public TamTruDto registerTamTru(TamTruDto dto) {
        NhanKhau nk = nhanKhauRepository.findById(dto.getMaNhanKhau())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu"));
        TamTru tt = new TamTru();
        BeanUtils.copyProperties(dto, tt);
        tt.setNhanKhau(nk);
        // cập nhật trạng thái nhân khẩu
        nk.setStatus("TAM_TRU");
        tamTruRepository.save(tt);
        nhanKhauRepository.save(nk);

        TamTruDto out = new TamTruDto();
        BeanUtils.copyProperties(tt, out);
        out.setMaNhanKhau(nk.getMaNhanKhau());
        return out;
    }

    @Override
    @Transactional
    public TamVangDto registerTamVang(TamVangDto dto) {
        NhanKhau nk = nhanKhauRepository.findById(dto.getMaNhanKhau())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu"));
        TamVang tv = new TamVang();
        BeanUtils.copyProperties(dto, tv);
        tv.setNhanKhau(nk);
        // cập nhật trạng thái nhân khẩu
        nk.setStatus("TAM_VANG");
        tamVangRepository.save(tv);
        nhanKhauRepository.save(nk);

        TamVangDto out = new TamVangDto();
        BeanUtils.copyProperties(tv, out);
        out.setMaNhanKhau(nk.getMaNhanKhau());
        return out;
    }

    @Override
    @Transactional
    public NhanKhauDto declareDeath(String maNhanKhau) {
        NhanKhau nk = nhanKhauRepository.findById(maNhanKhau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân khẩu"));
        nk.setStatus("KHAI_TU");
        // tùy business: có thể set hoKhau = null hoặc xóa khỏi ho khau
        nhanKhauRepository.save(nk);
        NhanKhauDto dto = new NhanKhauDto();
        BeanUtils.copyProperties(nk, dto);
        if (nk.getHoKhau() != null) dto.setMaHoKhau(nk.getHoKhau().getMaHoKhau());
        return dto;
    }
}
