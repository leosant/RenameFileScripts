package com.github.leosant;

import com.github.leosant.services.FilesServices;
import com.github.leosant.services.interfaces.IManagerFile;

import java.io.File;
import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {

        renameWithValueDirectory();
    }

    public static void renameWithValueDirectory() throws IOException {

        IManagerFile file = FilesServices.factoryFile();
        file.getFiles();
    }

    public static void renameWithValueFile() throws IOException {

        IManagerFile file = FilesServices.factoryFile();
        file.getFiles("", new File(""), "TEST");
    }
}