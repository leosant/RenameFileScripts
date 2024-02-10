package com.github.leosant.model.enums;

import com.github.leosant.services.interfaces.ITypeArchive;
import com.github.leosant.services.strategies.PdfStragety;

import java.util.Objects;
import java.util.Optional;

public enum InstanceArchiveEnum {

    PDF("pdf", new PdfStragety());

    private final String extension;

    private final ITypeArchive iTypeArchive;

    InstanceArchiveEnum(String extension, ITypeArchive iTypeArchive) {
        this.extension = extension;
        this.iTypeArchive = iTypeArchive;
    }

    public static Optional<ITypeArchive> instanceOf(String extension) {
        for (InstanceArchiveEnum instanceArchiveEnum : InstanceArchiveEnum.values()) {
            if (Objects.equals(instanceArchiveEnum.getExtension(), extension)) {
                return Optional.ofNullable(instanceArchiveEnum.getTypeArchive());
            }
        }
        return Optional.empty();
    }

    public String getExtension() {
        return extension;
    }

    public ITypeArchive getTypeArchive() {
        return iTypeArchive;
    }
}
