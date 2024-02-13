package com.github.leosant.model.dto;

public class ArchiveErrorDto {

    private String path;

    private String messageError;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageErro) {
        this.messageError = messageErro;
    }
}
