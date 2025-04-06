package com.github.leosant.model.enums;

import org.apache.commons.lang3.StringUtils;
@SuppressWarnings(value = "unused")
public enum PermissionInstitution {

    SANTANDER("santander");

    private final String name;

    PermissionInstitution(String name) {
        this.name = name;
    }

    public static boolean isValid(String name) {
        for (PermissionInstitution permissionInstitution : PermissionInstitution.values()) {
            if (StringUtils.containsIgnoreCase(name, permissionInstitution.name)) {
                return true;
            }
        }
        return false;
    }
}
