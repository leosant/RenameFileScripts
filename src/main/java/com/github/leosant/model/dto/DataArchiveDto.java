package com.github.leosant.model.dto;

public class DataArchiveDto {
    private String transactionDate;

    private String namedArchive;

    private String pathArchive;

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

    public String getPathArchive() {
        return pathArchive;
    }

    public void setPathArchive(String pathArchive) {
        this.pathArchive = pathArchive;
    }
}
