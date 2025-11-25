package com.citizen.management.citizen_management_system_back_end.enums;

/**
 * Enum representing the urgency levels for a given context.
 * <p>
 * Each value indicates a different level of urgency:
 * <ul>
 *   <li>{@link #THAP} - Low urgency</li>
 *   <li>{@link #TRUNG_BINH} - Medium urgency</li>
 *   <li>{@link #CAO} - High urgency</li>
 * </ul>
 */
public enum EnumMucDoKhanCap {
    /**
     * Low urgency level.
     * <p>
     * Indicates that the situation is not urgent and can be addressed at a later time.
     */
    THAP,
    /**
     * Medium urgency level.
     * <p>
     * Indicates that the situation should be addressed in a timely manner, but is not critical.
     */
    TRUNG_BINH,
    /**
     * High urgency level.
     * <p>
     * Indicates that the situation is critical and requires immediate attention.
     */
    CAO
}
