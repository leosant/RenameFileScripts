package com.github.leosant.model.enums;

import org.apache.commons.lang3.StringUtils;
@SuppressWarnings(value = "unused")
public enum PermissionInstituction {

    SANTANDER("santander");

    private final String name;

    PermissionInstituction(String name) {
        this.name = name;
    }

    public static boolean isValid(String name) {
        for (PermissionInstituction permissionInstituction : PermissionInstituction.values()) {
            if (StringUtils.containsIgnoreCase(name, permissionInstituction.name)) {
                return true;
            }
        }
        return false;
    }
}
