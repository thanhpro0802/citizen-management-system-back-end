-- Sample data inserts for ho_khau, nhan_khau, tam_tru, tam_vang
-- Generated to match schema in your script

-- ---------------------------
-- HO_KHAU (20)
-- ---------------------------
INSERT INTO ho_khau (ma_ho_khau, dia_chi, ngay_dang_ky, ma_nhan_khau_chu_ho)
VALUES
    ('HK001', 'Hà Nội', '2020-01-01', 'NK001'),
    ('HK002', 'Đà Nẵng', '2020-02-02', 'NK003'),
    ('HK003', 'Hải Phòng', '2020-03-03', 'NK005'),
    ('HK004', 'Bình Dương', '2020-04-04', 'NK007'),
    ('HK005', 'Vĩnh Phúc', '2020-05-05', 'NK008'),
    ('HK006', 'Quảng Ngãi', '2020-06-06', 'NK009'),
    ('HK007', 'TP HCM', '2020-07-07', 'NK011'),
    ('HK008', 'Nghệ An', '2020-08-08', 'NK013'),
    ('HK009', 'Quảng Bình', '2020-09-09', 'NK015'),
    ('HK010', 'Bắc Ninh', '2020-10-10', 'NK016'),
    ('HK011', 'Hòa Bình', '2020-11-11', 'NK017'),
    ('HK012', 'Bình Thuận', '2020-12-12', 'NK019'),
    ('HK013', 'Thái Bình', '2021-01-01', 'NK020');


-- ---------------------------
-- NHAN_KHAU (20)
-- ---------------------------
-- Note: so_cccd must be unique
INSERT INTO nhan_khau (ma_nhan_khau, ho_ten, ngay_sinh, gioi_tinh, so_cccd, que_quan, dan_toc, quan_he_voi_chu_ho, trang_thai, ma_ho_khau)
VALUES
('NK001', 'Nguyễn Văn A', '1990-05-12', 'Nam', '012345678901', 'Quảng Trị', 'Kinh', 'Chủ hộ', 'THUONG_TRU'::person_status, 'HK001'),
('NK002', 'Trần Thị B', '1992-09-03', 'Nữ', '112345678902', 'Hà Nội', 'Kinh', 'Vợ', 'THUONG_TRU'::person_status, 'HK001'),
('NK003', 'Lê Văn C', '1985-02-20', 'Nam', '212345678903', 'Đà Nẵng', 'Kinh', 'Con trai', 'THUONG_TRU'::person_status, 'HK002'),
('NK004', 'Phạm Thị D', '2000-12-01', 'Nữ', '312345678904', 'Huế', 'Kinh', 'Con gái', 'THUONG_TRU'::person_status, 'HK002'),
('NK005', 'Hoàng Văn E', '1975-07-07', 'Nam', '412345678905', 'Hải Phòng', 'Kinh', 'Chủ hộ', 'THUONG_TRU'::person_status, 'HK003'),
('NK006', 'Đặng Thị F', '1980-03-30', 'Nữ', '512345678906', 'Nam Định', 'Kinh', 'Vợ', 'THUONG_TRU'::person_status, 'HK003'),
('NK007', 'Võ Văn G', '1995-11-11', 'Nam', '612345678907', 'Bình Dương', 'Kinh', 'Con trai', 'TAM_TRU'::person_status, 'HK004'),
('NK008', 'Bùi Thị H', '1988-06-25', 'Nữ', '712345678908', 'Vĩnh Phúc', 'Kinh', 'Chủ hộ', 'THUONG_TRU'::person_status, 'HK005'),
('NK009', 'Phan Văn I', '1965-01-09', 'Nam', '812345678909', 'Quảng Ngãi', 'Kinh', 'Chủ hộ', 'THUONG_TRU'::person_status, 'HK006'),
('NK010', 'Ngô Thị J', '1970-04-14', 'Nữ', '912345678910', 'Thanh Hóa', 'Kinh', 'Vợ', 'THUONG_TRU'::person_status, 'HK006'),
('NK011', 'Trương Văn K', '1998-08-08', 'Nam', '022345678911', 'TP HCM', 'Kinh', 'Chủ hộ', 'THUONG_TRU'::person_status, 'HK007'),
('NK012', 'Lưu Thị L', '2002-10-20', 'Nữ', '132345678912', 'Cần Thơ', 'Kinh', 'Con', 'TAM_TRU'::person_status, 'HK007'),
('NK013', 'Đỗ Văn M', '1986-05-05', 'Nam', '242345678913', 'Nghệ An', 'Kinh', 'Chủ hộ', 'THUONG_TRU'::person_status, 'HK008'),
('NK014', 'Hà Thị N', '1994-03-03', 'Nữ', '352345678914', 'Hải Dương', 'Kinh', 'Vợ', 'TAM_TRU'::person_status, 'HK008'),
('NK015', 'Mai Văn O', '2010-09-09', 'Nam', '462345678915', 'Quảng Bình', 'Kinh', 'Con', 'THUONG_TRU'::person_status, 'HK009'),
('NK016', 'Phùng Thị P', '1983-02-17', 'Nữ', '572345678916', 'Bắc Ninh', 'Kinh', 'Chủ hộ', 'THUONG_TRU'::person_status, 'HK010'),
('NK017', 'Nguyễn Thị Q', '1991-06-30', 'Nữ', '682345678917', 'Hòa Bình', 'Kinh', 'Chủ hộ', 'THUONG_TRU'::person_status, 'HK011'),
('NK018', 'Lê Thị R', '1978-11-22', 'Nữ', '792345678918', 'Nam Định', 'Kinh', 'Khác', 'TAM_TRU'::person_status, NULL), -- không thuộc hộ nào
('NK019', 'Phạm Văn S', '2004-07-16', 'Nam', '892345678919', 'Bình Thuận', 'Kinh', 'Thuê trọ', 'TAM_TRU'::person_status, 'HK012'),
-- Một nhân khẩu đã KHAI_TU (khai tử) — status không bị trigger ghi đè
('NK020', 'Trần Văn T', '1950-01-01', 'Nam', '992345678920', 'Thái Bình', 'Kinh', 'Chủ hộ', 'KHAI_TU'::person_status, 'HK013');

-- ---------------------------
-- TAM_TRU (20)
-- ---------------------------
-- Mix of active (ngay_ket_thuc IS NULL or future) and ended entries
INSERT INTO tam_tru (ma_tam_tru, ngay_bat_dau, ngay_ket_thuc, ly_do, ma_nhan_khau) VALUES
('TT001','2024-01-01', NULL, 'Công tác dài hạn', 'NK007'),
('TT002','2025-10-01','2026-01-01','Học tập', 'NK012'), -- active (ends 2026-01-01)
('TT003','2023-05-10','2023-11-01','Thử việc', 'NK015'), -- ended
('TT004','2025-07-01', NULL, 'Làm việc tạm thời', 'NK018'), -- active
('TT005','2022-09-01','2023-09-01','Đi công tác', 'NK003'),
('TT006','2025-12-01', NULL, 'Đi học', 'NK014'), -- active (starts recently)
('TT007','2021-03-01','2021-12-31','Ở nhờ', 'NK009'),
('TT008','2024-08-15','2024-12-31','Chữa bệnh', 'NK010'),
('TT009','2025-02-20','2025-08-20','Gia đình', 'NK004'),
('TT010','2020-06-01','2021-06-01','Làm ăn', 'NK005'),
('TT011','2023-11-11','2024-11-10','Học nghề', 'NK011'),
('TT012','2024-12-01','2025-12-31','Công tác', 'NK002'), -- ends 2025-12-31 (may be active depending on current date)
('TT013','2022-01-05','2024-01-04','Ở nước ngoài', 'NK001'),
('TT014','2025-04-10','2025-07-10','Tạm cư', 'NK016'),
('TT015','2024-05-05','2024-11-05','Giải quyết thủ tục', 'NK017'),
('TT016','2025-09-01', NULL, 'Làm dự án', 'NK019'), -- active
('TT017','2019-10-01','2020-03-01','Đổi chỗ ở', 'NK006'),
('TT018','2023-02-14','2023-08-14','Đi học', 'NK013'),
('TT019','2025-01-01','2025-02-01','Thăm thân', 'NK008'),
('TT020','2024-10-10','2025-10-09','Thăm con', 'NK020'); -- ended or nearly ended

-- ---------------------------
-- TAM_VANG (20)
-- ---------------------------
-- Mix of active and ended (tam vắng typically requires person be THUONG_TRU originally)
INSERT INTO tam_vang (ma_tam_vang, ngay_bat_dau, ngay_ket_thuc, ly_do, ma_nhan_khau) VALUES
('TV001','2025-06-01','2025-06-30','Đi công tác ngắn ngày','NK001'),
('TV002','2025-11-20', NULL,'Đi làm ăn xa','NK002'), -- active
('TV003','2023-07-01','2023-09-01','Đi học','NK003'),
('TV004','2024-12-01','2025-12-31','Đi làm xa','NK004'), -- might be active depending on date
('TV005','2021-05-05','2021-08-05','Thăm nhà','NK005'),
('TV006','2025-10-15','2026-04-15','Làm dự án','NK006'), -- active (ends 2026)
('TV007','2022-03-03','2022-06-03','Thăm họ hàng','NK007'),
('TV008','2025-01-01','2025-03-01','Đi học','NK008'),
('TV009','2024-05-20','2024-06-20','Công tác','NK009'),
('TV010','2020-09-09','2020-11-09','Đi vắng','NK010'),
('TV011','2025-12-01', NULL,'Đi công tác dài hạn','NK011'), -- active (starts 2025-12-01)
('TV012','2024-02-14','2024-02-28','Công tác','NK012'),
('TV013','2023-11-11','2024-01-10','Đi học','NK013'),
('TV014','2025-03-03','2025-05-03','Đi chữa bệnh','NK014'),
('TV015','2024-07-07','2024-08-07','Đi học','NK015'),
('TV016','2023-04-04','2023-04-20','Thăm gia đình','NK016'),
('TV017','2025-09-09','2025-10-09','Làm dự án ngắn','NK017'),
('TV018','2021-12-12','2022-01-12','Thăm nhà','NK018'),
('TV019','2025-02-02','2025-05-02','Thi công','NK019'),
('TV020','2024-11-11','2025-11-10','Thăm con','NK020');

-- End of sample inserts

-- A. TẠO USER
INSERT INTO tai_khoan (ma_tai_khoan, so_cccd, mat_khau, vai_tro) VALUES
    ('user_congdan', '001200000001', '$2a$10$EP6.1.M4e8d3r3r3r3r3r3r3r3r3r3r3r3r3r3r3r3r3r3r3r3r3', 'CONG_DAN'),
    ('user_canbo', '001200000002',  '$2a$10$EP6.1.M4e8d3r3r3r3r3r3r3r3r3r3r3r3r3r3r3r3r3r3r3r3r3', 'CAN_BO'),
    ('user_admin', '001200000003',  '$2a$10$EP6.1.M4e8d3r3r3r3r3r3r3r3r3r3r3r3r3r3r3r3r3r3r3r3r3', 'ADMIN');

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