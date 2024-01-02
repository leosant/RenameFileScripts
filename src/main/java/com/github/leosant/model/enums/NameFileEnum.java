package com.github.leosant.model.dto;

public enum NameFileEnum {

    ENEL_ENERGIA("ENEL DISTRIBUICAO GO");

    private final String name;

    NameFileEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static NameFileEnum convertStringToEnum(String name) {
        for (NameFileEnum value : NameFileEnum.values()) {
            if (value.getName().equalsIgnoreCase(name)) {
                return value;
            }

            return null;
        }

        return null;
    }
}
