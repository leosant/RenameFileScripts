package com.github.leosant.model.dto;

import com.github.leosant.model.enums.ExtensionEnum;

import java.io.File;

public class ArchiveDataDto {

    public ArchiveDataDto(File file, ExtensionEnum extensionEnum) {
        this.file = file;
        this.extensionEnum = extensionEnum;
    }

    private final ExtensionEnum extensionEnum;

    private String transactionDate;

    private String namedArchive;

    private String destiny;

    private File file;

    public ExtensionEnum getExtensionEnum() {
        return extensionEnum;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getNamedArchive() {
        return namedArchive;
    }

    public void setNamedArchive(String namedArchive) {
        this.namedArchive = namedArchive;
    }

    public String getDestiny() {
        return destiny;
    }

    public void setDestiny(String destiny) {
        this.destiny = destiny;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
