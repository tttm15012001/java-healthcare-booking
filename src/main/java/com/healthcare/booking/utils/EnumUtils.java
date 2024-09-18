package com.healthcare.booking.utils;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumUtils {

    // Lấy label theo code cho các enum implement CodeLabelEnum
    public static <E extends Enum<E> & StatusLabelEnum> String getLabelByCode(Class<E> enumClass, int code) {
        for (E enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.getCode() == code) {
                return enumConstant.getLabel();
            }
        }
        return "Unknown";
    }

    // Lấy tất cả các cặp code-label cho các enum implement CodeLabelEnum
    public static <E extends Enum<E> & StatusLabelEnum> Map<Integer, String> getAllOptions(Class<E> enumClass) {
        return Stream.of(enumClass.getEnumConstants())
                .collect(Collectors.toMap(StatusLabelEnum::getCode, StatusLabelEnum::getLabel));
    }
}
