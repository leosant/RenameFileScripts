package com.github.leosant.model.enums;

import java.util.Arrays;
import java.util.Optional;

public enum MonthEnum {
    JANUARY("JAN", "01"),
    FEBRUARY("FEV", "02"),
    MARCH("MAR", "03"),
    APRIL("ABR", "04"),
    MAY("MAI", "05"),
    JUNE("JUN", "06"),
    JULY("JUL", "07"),
    AUGUST("AGO", "08"),
    SEPTEMBER("SET", "09"),
    OCTOBER("OUT", "10"),
    NOVEMBER("NOV", "11"),
    DECEMBER("DEZ", "12");

    private final String month;

    private final String number;

    MonthEnum(String month, String number) {
        this.month = month;
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public static Optional<MonthEnum> getMonthValid(String text) {
        return Arrays.stream(MonthEnum.values())
                .filter(monthEnum -> monthEnum.month.equalsIgnoreCase(text))
                .findFirst();
    }
}
