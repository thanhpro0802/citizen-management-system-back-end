-- =====================================================
-- FILE TEST DATA CHO HỆ THỐNG QUẢN LÝ NHÂN KHẨU
-- Phù hợp với JPA Entities
-- =====================================================

-- Xóa dữ liệu cũ (nếu có)
TRUNCATE TABLE thong_bao CASCADE;
TRUNCATE TABLE tep_dinh_kem CASCADE;
TRUNCATE TABLE lich_su_phan_anh CASCADE;
TRUNCATE TABLE phan_anh CASCADE;
TRUNCATE TABLE tam_vang CASCADE;
TRUNCATE TABLE tam_tru CASCADE;
TRUNCATE TABLE tai_khoan CASCADE;
TRUNCATE TABLE nhan_khau CASCADE;
TRUNCATE TABLE ho_khau CASCADE;

INSERT INTO ho_khau (ma_ho_khau, dia_chi, ngay_dang_ky, ma_nhan_khau_chu_ho) VALUES
('HK001', '123 Nguyễn Trãi, Phường 2, Quận 5, TP.HCM', '2020-01-15', NULL), -- Sẽ update sau khi có nhân khẩu
('HK002', '456 Lê Lợi, Phường Bến Thành, Quận 1, TP.HCM', '2019-05-20', NULL),
('HK003', '789 Trần Hưng Đạo, Phường 1, Quận 5, TP.HCM', '2021-03-10', NULL),
('HK004', '321 Võ Văn Tần, Phường 5, Quận 3, TP.HCM', '2018-11-25', NULL),
('HK005', '654 Phan Xích Long, Phường 2, Quận Phú Nhuận, TP.HCM', '2022-07-01', NULL);


INSERT INTO nhan_khau (
    ma_nhan_khau, ho_ten, ngay_sinh, gioi_tinh, so_cccd,
    que_quan, dan_toc, quan_he_voi_chu_ho, trang_thai, ma_ho_khau
) VALUES
-- Hộ HK001
('NK001', 'Nguyễn Văn An', '1980-05-15', 'Nam', '001080012345', 'Hà Nội', 'Kinh', 'Chủ hộ', 'THUONG_TRU', 'HK001'),
('NK002', 'Trần Thị Bình', '1982-08-20', 'Nữ', '001082023456', 'TP.HCM', 'Kinh', 'Vợ', 'THUONG_TRU', 'HK001'),
('NK003', 'Nguyễn Văn Cường', '2005-03-12', 'Nam', '001005034567', 'TP.HCM', 'Kinh', 'Con trai', 'THUONG_TRU', 'HK001'),
('NK004', 'Nguyễn Thị Dung', '2008-11-25', 'Nữ', '001008045678', 'TP.HCM', 'Kinh', 'Con gái', 'THUONG_TRU', 'HK001'),

-- Hộ HK002
('NK005', 'Lê Văn Hùng', '1975-02-28', 'Nam', '001075056789', 'Đà Nẵng', 'Kinh', 'Chủ hộ', 'THUONG_TRU', 'HK002'),
('NK006', 'Phạm Thị Lan', '1978-07-10', 'Nữ', '001078067890', 'Huế', 'Kinh', 'Vợ', 'THUONG_TRU', 'HK002'),

-- TAM_VANG
('NK007', 'Lê Văn Minh', '2002-09-05', 'Nam', '001002078901', 'TP.HCM', 'Kinh', 'Con trai', 'TAM_VANG', 'HK002'),

-- Hộ HK003
('NK008', 'Trần Văn Nam', '1990-12-20', 'Nam', '001090089012', 'Nghệ An', 'Kinh', 'Chủ hộ', 'THUONG_TRU', 'HK003'),
('NK009', 'Hoàng Thị Oanh', '1992-04-15', 'Nữ', '001092090123', 'Thanh Hóa', 'Kinh', 'Vợ', 'THUONG_TRU', 'HK003'),
('NK010', 'Trần Văn Phúc', '2015-06-08', 'Nam', '001015001234', 'TP.HCM', 'Kinh', 'Con trai', 'THUONG_TRU', 'HK003'),

-- Hộ HK004
('NK011', 'Võ Văn Quang', '1985-01-30', 'Nam', '001085012345', 'Bình Định', 'Kinh', 'Chủ hộ', 'THUONG_TRU', 'HK004'),

-- TAM_TRU
('NK012', 'Nguyễn Thị Thanh', '1987-09-22', 'Nữ', '001087023456', 'TP.HCM', 'Kinh', 'Vợ', 'TAM_TRU', 'HK004'),

-- Hộ HK005
('NK013', 'Phan Văn Sơn', '1995-11-11', 'Nam', '001095034567', 'Quảng Nam', 'Kinh', 'Chủ hộ', 'THUONG_TRU', 'HK005'),

-- KHAI_TU (TEST KHAI TỬ)
('NK014', 'Lý Thị Tuyết', '1997-02-14', 'Nữ', '001097045678', 'TP.HCM', 'Kinh', 'Vợ', 'KHAI_TU', 'HK005'),

-- Người đơn thân
('NK015', 'Đỗ Văn Tùng', '1998-07-07', 'Nam', '001098056789', 'Hà Nội', 'Kinh', NULL, 'TAM_TRU', NULL);

-- =====================================================
-- 2. CẬP NHẬT CHỦ HỘ
-- =====================================================

UPDATE ho_khau SET ma_nhan_khau_chu_ho = 'NK001' WHERE ma_ho_khau = 'HK001';
UPDATE ho_khau SET ma_nhan_khau_chu_ho = 'NK005' WHERE ma_ho_khau = 'HK002';
UPDATE ho_khau SET ma_nhan_khau_chu_ho = 'NK008' WHERE ma_ho_khau = 'HK003';
UPDATE ho_khau SET ma_nhan_khau_chu_ho = 'NK011' WHERE ma_ho_khau = 'HK004';
UPDATE ho_khau SET ma_nhan_khau_chu_ho = 'NK013' WHERE ma_ho_khau = 'HK005';

-- =====================================================
-- 3. DỮ LIỆU TẠM TRÚ
-- =====================================================

INSERT INTO tam_tru (
    ma_tam_tru, ma_nhan_khau, ngay_bat_dau, ngay_ket_thuc, ly_do
) VALUES
('TTR001', 'NK012', '2024-01-01', '2024-12-31', 'Công tác tại TP.HCM'),
('TTR002', 'NK015', '2024-06-01', '2025-06-01', 'Học tập và làm việc tại TP.HCM');

-- =====================================================
-- 4. DỮ LIỆU TẠM VẮNG
-- =====================================================

INSERT INTO tam_vang (
    ma_tam_vang, ma_nhan_khau, ngay_bat_dau, ngay_ket_thuc, ly_do
) VALUES
('TV001', 'NK007', '2024-09-01', '2025-05-31', 'Đi du học tại nước ngoài');

-- =====================================================
-- 7. THÊM PHẢN ÁNH
-- =====================================================



-- =====================================================
-- HOÀN TẤT
-- =====================================================
-- Dữ liệu test đã được thêm thành công!
-- Tài khoản test:
--   Admin: CCCD 001080012345 / password: password123
--   Cán bộ: CCCD 001075056789 / password: password123
--   Công dân: CCCD 001082023456 / password: password123
-- =====================================================

-- End of sample inserts

-- ===================================================================
-- A. TẠO USER TEST
-- Mật khẩu: "123456" (BCrypt hash)
-- QUAN TRỌNG: Các tài khoản được liên kết với nhân khẩu để có thể load thông tin cá nhân
-- ===================================================================
INSERT INTO tai_khoan (ma_tai_khoan, so_cccd, mat_khau, so_dien_thoai, vai_tro, nhan_khau_id) VALUES
    ('user_congdan', '001095034567', '$2a$10$YDB3ULisq.dbvNBoZ/NB.OKoAmEsvi6s22R3dA3J2enm/kVHZJdTW', '0901234567', 'CONG_DAN', 'NK013'),  -- Phan Văn Sơn
    ('user_canbo',   '001080012345', '$2a$10$YDB3ULisq.dbvNBoZ/NB.OKoAmEsvi6s22R3dA3J2enm/kVHZJdTW', '0912345678', 'CAN_BO',   'NK001'),  -- Nguyễn Văn An (Chủ hộ HK001)
    ('user_admin',   '001090089012', '$2a$10$YDB3ULisq.dbvNBoZ/NB.OKoAmEsvi6s22R3dA3J2enm/kVHZJdTW', '0923456789', 'ADMIN',    'NK008');  -- Trần Văn Nam (Chủ hộ HK003)

-- B. TẠO PHẢN ÁNH (25 Bản ghi)

-- 1. Nhóm QUÁ HẠN (5 hồ sơ) -> Deadline trong quá khứ
INSERT INTO phan_anh (ma_phan_anh, tieu_de, noi_dung, linh_vuc, trang_thai_hien_tai, muc_do_khan_cap, thoi_gian_tao, thoi_han_xu_ly, ma_tai_khoan_gui, ma_can_bo_phu_trach) VALUES
    ('PA-OVER-01', 'Karaoke ồn ào quá hạn', 'Hát suốt đêm ngày này qua ngày khác.', 'AN_NINH_TRAT_TU', 'DANG_XU_LY', 'CAO', CURRENT_TIMESTAMP - INTERVAL '10 days', (CURRENT_TIMESTAMP - INTERVAL '2 days')::date, 'user_congdan', 'user_canbo'),
    ('PA-OVER-02', 'Lấn chiếm vỉa hè bán quán', 'Không còn lối cho người đi bộ.', 'AN_NINH_TRAT_TU', 'DANG_XU_LY', 'TRUNG_BINH', CURRENT_TIMESTAMP - INTERVAL '12 days', (CURRENT_TIMESTAMP - INTERVAL '5 days')::date, 'user_congdan', 'user_canbo'),
    ('PA-OVER-03', 'Xả rác bừa bãi đầu ngõ', 'Mùi hôi thối nồng nặc.', 'MOI_TRUONG', 'DANG_XU_LY', 'CAO', CURRENT_TIMESTAMP - INTERVAL '15 days', (CURRENT_TIMESTAMP - INTERVAL '1 day')::date, 'user_congdan', 'user_canbo'),
    ('PA-OVER-04', 'Đèn đường hỏng ngã tư', 'Nguy hiểm giao thông.', 'HA_TANG_DO_THI', 'DANG_XU_LY', 'TRUNG_BINH', CURRENT_TIMESTAMP - INTERVAL '20 days', (CURRENT_TIMESTAMP - INTERVAL '10 days')::date, 'user_congdan', 'user_canbo'),
    ('PA-OVER-05', 'Hố ga mất nắp nguy hiểm', 'Đề nghị khắc phục ngay.', 'HA_TANG_DO_THI', 'DANG_XU_LY', 'CAO', CURRENT_TIMESTAMP - INTERVAL '8 days', (CURRENT_TIMESTAMP - INTERVAL '3 days')::date, 'user_congdan', 'user_canbo');

-- 2. Nhóm CHỜ TIẾP NHẬN (8 hồ sơ) -> Mới nhất
INSERT INTO phan_anh (ma_phan_anh, tieu_de, noi_dung, linh_vuc, trang_thai_hien_tai, muc_do_khan_cap, thoi_gian_tao, ma_tai_khoan_gui) VALUES
    ('PA-NEW-01', 'Cây đổ sau bão', 'Cây chắn ngang đường đi.', 'HA_TANG_DO_THI', 'CHO', 'CAO', CURRENT_TIMESTAMP, 'user_congdan'),
    ('PA-NEW-02', 'Yêu cầu cắt tỉa cây xanh', 'Cành cây vướng dây điện.', 'HA_TANG_DO_THI', 'CHO', 'TRUNG_BINH', CURRENT_TIMESTAMP - INTERVAL '1 hour', 'user_congdan'),
    ('PA-NEW-03', 'Nhà máy xả khói đen', 'Ô nhiễm không khí nghiêm trọng.', 'MOI_TRUONG', 'CHO', 'CAO', CURRENT_TIMESTAMP - INTERVAL '2 hours', 'user_congdan'),
    ('PA-NEW-04', 'Hỏi thủ tục tạm trú', 'Tôi cần giấy tờ gì?', 'HANH_CHINH_CONG', 'CHO', 'THAP', CURRENT_TIMESTAMP - INTERVAL '5 hours', 'user_congdan'),
    ('PA-NEW-05', 'Mất nước toàn khu', 'Đã mất nước 2 ngày.', 'HA_TANG_DO_THI', 'CHO', 'CAO', CURRENT_TIMESTAMP - INTERVAL '6 hours', 'user_congdan'),
    ('PA-NEW-06', 'Chó thả rông', 'Nhiều chó không rọ mõm.', 'AN_NINH_TRAT_TU', 'CHO', 'TRUNG_BINH', CURRENT_TIMESTAMP - INTERVAL '1 day', 'user_congdan'),
    ('PA-NEW-07', 'Thu phí sai quy định', 'Tổ trưởng thu tiền lạ.', 'HANH_CHINH_CONG', 'CHO', 'THAP', CURRENT_TIMESTAMP - INTERVAL '1 day', 'user_congdan'),
    ('PA-NEW-08', 'Biển báo bị che khuất', 'Cây che mất biển báo.', 'GIAO_THONG', 'CHO', 'THAP', CURRENT_TIMESTAMP - INTERVAL '2 days', 'user_congdan');

-- 3. Nhóm ĐANG XỬ LÝ (7 hồ sơ) -> Deadline tương lai
INSERT INTO phan_anh (ma_phan_anh, tieu_de, noi_dung, linh_vuc, trang_thai_hien_tai, muc_do_khan_cap, thoi_gian_tao, thoi_han_xu_ly, ma_tai_khoan_gui, ma_can_bo_phu_trach) VALUES
    ('PA-PROC-01', 'Tranh chấp đất đai', 'Hàng xóm lấn tường rào.', 'AN_NINH_TRAT_TU', 'DANG_XU_LY', 'TRUNG_BINH', CURRENT_TIMESTAMP - INTERVAL '2 days', (CURRENT_TIMESTAMP + INTERVAL '5 days')::date, 'user_congdan', 'user_canbo'),
    ('PA-PROC-02', 'Thủ tục kết hôn nước ngoài', 'Hướng dẫn hồ sơ.', 'HANH_CHINH_CONG', 'DANG_XU_LY', 'THAP', CURRENT_TIMESTAMP - INTERVAL '3 days', (CURRENT_TIMESTAMP + INTERVAL '2 days')::date, 'user_congdan', 'user_canbo'),
    ('PA-PROC-03', 'Bụi từ công trình xây dựng', 'Công trình không che chắn.', 'MOI_TRUONG', 'DANG_XU_LY', 'TRUNG_BINH', CURRENT_TIMESTAMP - INTERVAL '3 days', (CURRENT_TIMESTAMP + INTERVAL '3 days')::date, 'user_congdan', 'user_canbo'),
    ('PA-PROC-04', 'Lắp camera an ninh', 'Đề nghị lắp thêm camera.', 'AN_NINH_TRAT_TU', 'DANG_XU_LY', 'THAP', CURRENT_TIMESTAMP - INTERVAL '4 days', (CURRENT_TIMESTAMP + INTERVAL '7 days')::date, 'user_congdan', 'user_canbo'),
    ('PA-PROC-05', 'Sửa chữa vỉa hè', 'Gạch lát bị vỡ.', 'HA_TANG_DO_THI', 'DANG_XU_LY', 'THAP', CURRENT_TIMESTAMP - INTERVAL '4 days', (CURRENT_TIMESTAMP + INTERVAL '10 days')::date, 'user_congdan', 'user_canbo'),
    ('PA-PROC-06', 'Tiêm chủng mở rộng', 'Hỏi lịch tiêm cho trẻ.', 'Y_TE', 'DANG_XU_LY', 'THAP', CURRENT_TIMESTAMP - INTERVAL '5 days', (CURRENT_TIMESTAMP + INTERVAL '1 day')::date, 'user_congdan', 'user_canbo'),
    ('PA-PROC-07', 'Vệ sinh an toàn thực phẩm', 'Quán ăn mất vệ sinh.', 'Y_TE', 'DANG_XU_LY', 'CAO', CURRENT_TIMESTAMP - INTERVAL '5 days', (CURRENT_TIMESTAMP + INTERVAL '1 day')::date, 'user_congdan', 'user_canbo');

-- 4. Nhóm ĐÃ XỬ LÝ (5 hồ sơ)
INSERT INTO phan_anh (ma_phan_anh, tieu_de, noi_dung, linh_vuc, trang_thai_hien_tai, muc_do_khan_cap, thoi_gian_tao, thoi_han_xu_ly, danh_gia_hai_long, gop_y, ma_tai_khoan_gui, ma_can_bo_phu_trach) VALUES
    ('PA-DONE-01', 'Làm giấy khai sinh', 'Đã nhận kết quả.', 'HANH_CHINH_CONG', 'DA_XU_LY', 'THAP', CURRENT_TIMESTAMP - INTERVAL '20 days', (CURRENT_TIMESTAMP - INTERVAL '18 days')::date, 5, 'Rất nhanh', 'user_congdan', 'user_canbo'),
    ('PA-DONE-02', 'Phun thuốc muỗi', 'Đã thực hiện xong.', 'Y_TE', 'DA_XU_LY', 'TRUNG_BINH', CURRENT_TIMESTAMP - INTERVAL '25 days', (CURRENT_TIMESTAMP - INTERVAL '24 days')::date, 4, 'Cảm ơn', 'user_congdan', 'user_canbo'),
    ('PA-DONE-03', 'Dọn rác tồn đọng', 'Đã sạch sẽ.', 'MOI_TRUONG', 'DA_XU_LY', 'CAO', CURRENT_TIMESTAMP - INTERVAL '30 days', (CURRENT_TIMESTAMP - INTERVAL '28 days')::date, 5, NULL, 'user_congdan', 'user_canbo'),
    ('PA-DONE-04', 'Đánh nhau gây rối', 'Đã hòa giải.', 'AN_NINH_TRAT_TU', 'DA_XU_LY', 'CAO', CURRENT_TIMESTAMP - INTERVAL '40 days', (CURRENT_TIMESTAMP - INTERVAL '39 days')::date, 3, 'Hơi chậm', 'user_congdan', 'user_canbo'),
    ('PA-DONE-05', 'Vỡ ống nước', 'Đã sửa.', 'HA_TANG_DO_THI', 'DA_XU_LY', 'CAO', CURRENT_TIMESTAMP - INTERVAL '50 days', (CURRENT_TIMESTAMP - INTERVAL '49 days')::date, 5, 'Tốt', 'user_congdan', 'user_canbo');

-- C. TẠO THÔNG BÁO
INSERT INTO thong_bao (ma_thong_bao, noi_dung, thoi_gian, da_xem, ma_phan_anh_lien_quan, ma_nguoi_nhan) VALUES
    ('TB-01', '⚠️ CẢNH BÁO: Hồ sơ PA-OVER-01 đã quá hạn!', CURRENT_TIMESTAMP, FALSE, 'PA-OVER-01', 'user_canbo'),
    ('TB-02', 'Bạn được phân công xử lý hồ sơ PA-PROC-01', CURRENT_TIMESTAMP, FALSE, 'PA-PROC-01', 'user_canbo'),
    ('TB-03', 'Thông báo cũ đã xem', CURRENT_TIMESTAMP - INTERVAL '1 day', TRUE, 'PA-DONE-01', 'user_canbo');

-- D. TẠO LỊCH SỬ
INSERT INTO lich_su_phan_anh (ma_lich_su, ma_phan_anh, ma_tai_khoan_thuc_hien, hanh_dong, trang_thai_moi, noi_dung, thoi_gian) VALUES
    ('LS-01', 'PA-OVER-01', 'user_congdan', 'TAO_MOI', 'CHO', 'Công dân gửi phản ánh', CURRENT_TIMESTAMP - INTERVAL '10 days'),
    ('LS-02', 'PA-OVER-01', 'user_admin', 'PHAN_CONG', 'DANG_XU_LY', 'Phân công cho cán bộ xử lý', CURRENT_TIMESTAMP - INTERVAL '9 days');