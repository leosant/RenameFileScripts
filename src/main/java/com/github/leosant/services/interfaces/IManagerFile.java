package com.github.leosant.services.interfaces;

import java.io.File;
import java.io.IOException;

public interface IManagerFile {

    void getFiles() throws IOException;
    void getFiles(String destinyPath, File files, String nameArchive) throws IOException;

}
