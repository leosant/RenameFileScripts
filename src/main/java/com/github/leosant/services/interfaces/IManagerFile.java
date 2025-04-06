package com.github.leosant.services.interfaces;

import java.io.File;

public interface IManagerFile {
    void getFiles();
    void getFiles(String destinyPath, File files, String nameArchive);

}
