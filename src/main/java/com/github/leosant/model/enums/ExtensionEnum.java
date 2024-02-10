package com.github.leosant.model.enums;

public enum ExtensionEnum {

    PDF(".pdf");

    private final String name;

    ExtensionEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
