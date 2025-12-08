-- ===================================================================
-- COMPLETE POSTGRES SQL FOR QUẢN LÝ NHÂN KHẨU
-- Includes: ho_khau, nhan_khau, tam_tru, tam_vang
-- With status = UNKNOWN | THUONG_TRU | TAM_TRU | TAM_VANG | KHAI_TU
-- Trigger logic updated: does NOT override KHAI_TU
-- ===================================================================

-- Drop existing objects (optional for testing)
-- DROP TABLE IF EXISTS tam_vang CASCADE;
-- DROP TABLE IF EXISTS tam_tru CASCADE;
-- DROP TABLE IF EXISTS nhan_khau CASCADE;
-- DROP TABLE IF EXISTS ho_khau CASCADE;
-- DROP TYPE IF EXISTS person_status;

----------------------------------------------------------
-- ENUM type for status
----------------------------------------------------------
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'person_status') THEN
        CREATE TYPE person_status AS ENUM ('UNKNOWN','THUONG_TRU','TAM_TRU','TAM_VANG','KHAI_TU');
    ELSE
        -- add value if missing
        BEGIN
            ALTER TYPE person_status ADD VALUE 'KHAI_TU';
        EXCEPTION WHEN duplicate_object THEN null;
        END;
    END IF;
END$$;

----------------------------------------------------------
-- TABLE: ho_khau
----------------------------------------------------------
CREATE TABLE IF NOT EXISTS ho_khau (
    ma_ho_khau VARCHAR(100) PRIMARY KEY,
    dia_chi TEXT,
    ngay_lap DATE
);

----------------------------------------------------------
-- TABLE: nhan_khau
----------------------------------------------------------
CREATE TABLE IF NOT EXISTS nhan_khau (
    ma_nhan_khau VARCHAR(100) PRIMARY KEY,
    ho_ten VARCHAR(255) NOT NULL,
    ngay_sinh DATE,
    gioi_tinh VARCHAR(20),
    so_cccd VARCHAR(30) UNIQUE,
    que_quan TEXT,
    dan_toc VARCHAR(100),
    quan_he_voi_chu_ho VARCHAR(100),
    status person_status DEFAULT 'UNKNOWN',
    ma_ho_khau VARCHAR(100) REFERENCES ho_khau(ma_ho_khau) ON DELETE SET NULL,
    created_at timestamptz DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_nk_cccd ON nhan_khau(so_cccd);

----------------------------------------------------------
-- TABLE: tam_tru
----------------------------------------------------------
CREATE TABLE IF NOT EXISTS tam_tru (
    ma_tam_tru VARCHAR(100) PRIMARY KEY,
    ngay_bat_dau DATE,
    ngay_ket_thuc DATE,
    ly_do TEXT,
    ma_nhan_khau VARCHAR(100) NOT NULL REFERENCES nhan_khau(ma_nhan_khau) ON DELETE CASCADE,
    created_at timestamptz DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_tt_nk ON tam_tru(ma_nhan_khau);

----------------------------------------------------------
-- TABLE: tam_vang
----------------------------------------------------------
CREATE TABLE IF NOT EXISTS tam_vang (
    ma_tam_vang VARCHAR(100) PRIMARY KEY,
    ngay_bat_dau DATE,
    ngay_ket_thuc DATE,
    ly_do TEXT,
    ma_nhan_khau VARCHAR(100) NOT NULL REFERENCES nhan_khau(ma_nhan_khau) ON DELETE CASCADE,
    created_at timestamptz DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_tv_nk ON tam_vang(ma_nhan_khau);

----------------------------------------------------------
-- FUNCTION: Recalculate status for person
-- IMPORTANT: If person is KHAI_TU, DO NOT change status
----------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_recalc_status_for_person(p_ma_nk VARCHAR)
RETURNS VOID AS $$
DECLARE
    current_status person_status;
    has_household BOOLEAN;
    has_active_tt BOOLEAN;
    has_active_tv BOOLEAN;
BEGIN
    -- fetch current status
    SELECT status INTO current_status FROM nhan_khau WHERE ma_nhan_khau = p_ma_nk;

    -- Do not change if already KHAI_TU
    IF current_status = 'KHAI_TU' THEN
        RETURN;
    END IF;

    -- Check if has household
    SELECT (ma_ho_khau IS NOT NULL) INTO has_household
    FROM nhan_khau WHERE ma_nhan_khau = p_ma_nk;

    -- Check active tam trú
    SELECT EXISTS (
        SELECT 1 FROM tam_tru
        WHERE ma_nhan_khau = p_ma_nk
        AND (ngay_ket_thuc IS NULL OR ngay_ket_thuc >= CURRENT_DATE)
    ) INTO has_active_tt;

    -- Check active tam vắng
    SELECT EXISTS (
        SELECT 1 FROM tam_vang
        WHERE ma_nhan_khau = p_ma_nk
        AND (ngay_ket_thuc IS NULL OR ngay_ket_thuc >= CURRENT_DATE)
    ) INTO has_active_tv;

    -- Priority logic
    IF has_household THEN
        UPDATE nhan_khau SET status = 'THUONG_TRU' WHERE ma_nhan_khau = p_ma_nk;
    ELSIF has_active_tt THEN
        UPDATE nhan_khau SET status = 'TAM_TRU' WHERE ma_nhan_khau = p_ma_nk;
    ELSIF has_active_tv THEN
        UPDATE nhan_khau SET status = 'TAM_VANG' WHERE ma_nhan_khau = p_ma_nk;
    ELSE
        UPDATE nhan_khau SET status = 'UNKNOWN' WHERE ma_nhan_khau = p_ma_nk;
    END IF;
END;
$$ LANGUAGE plpgsql;

----------------------------------------------------------
-- TRIGGERS: tam_tru
----------------------------------------------------------
CREATE OR REPLACE FUNCTION trg_tt_after_insert() RETURNS trigger AS $$
BEGIN
    PERFORM fn_recalc_status_for_person(NEW.ma_nhan_khau);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trig_tt_ins
AFTER INSERT ON tam_tru
FOR EACH ROW EXECUTE FUNCTION trg_tt_after_insert();

CREATE OR REPLACE FUNCTION trg_tt_after_delete() RETURNS trigger AS $$
BEGIN
    PERFORM fn_recalc_status_for_person(OLD.ma_nhan_khau);
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trig_tt_del
AFTER DELETE ON tam_tru
FOR EACH ROW EXECUTE FUNCTION trg_tt_after_delete();

----------------------------------------------------------
-- TRIGGERS: tam_vang
----------------------------------------------------------
CREATE OR REPLACE FUNCTION trg_tv_after_insert() RETURNS trigger AS $$
BEGIN
    PERFORM fn_recalc_status_for_person(NEW.ma_nhan_khau);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trig_tv_ins
AFTER INSERT ON tam_vang
FOR EACH ROW EXECUTE FUNCTION trg_tv_after_insert();

CREATE OR REPLACE FUNCTION trg_tv_after_delete() RETURNS trigger AS $$
BEGIN
    PERFORM fn_recalc_status_for_person(OLD.ma_nhan_khau);
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trig_tv_del
AFTER DELETE ON tam_vang
FOR EACH ROW EXECUTE FUNCTION trg_tv_after_delete();

----------------------------------------------------------
-- TRIGGER: nhan_khau update ma_ho_khau
----------------------------------------------------------
CREATE OR REPLACE FUNCTION trg_nk_after_update() RETURNS trigger AS $$
BEGIN
    PERFORM fn_recalc_status_for_person(NEW.ma_nhan_khau);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trig_nk_update
AFTER UPDATE OF ma_ho_khau ON nhan_khau
FOR EACH ROW EXECUTE FUNCTION trg_nk_after_update();

-- END
