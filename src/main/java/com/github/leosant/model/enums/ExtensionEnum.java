package com.github.leosant.model.enums;

public enum ExtensionEnum {

    PDF(".pdf"),
    JPEG(".jpeg"),
    JPG(".jpg"),
    PNG(".png");

    private final String name;

    ExtensionEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
