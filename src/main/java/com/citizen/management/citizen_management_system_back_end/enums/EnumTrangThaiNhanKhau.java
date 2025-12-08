package com.citizen.management.citizen_management_system_back_end.enums;

/**
 * Enum representing the residential status of a citizen (NhanKhau).
 * These values correspond to the person_status enum type in the database.
 */
public enum EnumTrangThaiNhanKhau {
    /**
     * Permanent residence status
     */
    THUONG_TRU("THUONG_TRU"),
    
    /**
     * Temporary residence status
     */
    TAM_TRU("TAM_TRU"),
    
    /**
     * Temporary absence status
     */
    TAM_VANG("TAM_VANG"),
    
    /**
     * Deceased status
     */
    KHAI_TU("KHAI_TU");

    private final String value;

    EnumTrangThaiNhanKhau(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
