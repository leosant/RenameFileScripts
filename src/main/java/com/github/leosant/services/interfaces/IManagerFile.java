package com.github.leosant.services.interfaces;

import com.github.leosant.model.enums.NameFileEnum;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IManagerFile {

    void setNameFileEnums(List<NameFileEnum> nameFileEnums);
    void getFiles();
    void getFiles(String destinyPath, File files, String nameArchive);

}
