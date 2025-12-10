package com.citizen.management.citizen_management_system_back_end.repository;

import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoKhauRepository extends JpaRepository<HoKhau, Long> {

    Long countByDiaChi(String diaChi);

}
