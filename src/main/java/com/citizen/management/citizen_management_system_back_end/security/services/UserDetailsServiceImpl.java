package com.citizen.management.citizen_management_system_back_end.security.services;

import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String cccd) throws UsernameNotFoundException {
        // Tìm user
        TaiKhoan user = taiKhoanRepository.findByCccd(cccd)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản với số CCCD: " + cccd));

        // Gọi hàm build để chuyển đổi sang UserDetailsImpl
        return UserDetailsImpl.build(user);
    }
}