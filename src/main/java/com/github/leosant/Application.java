package com.github.leosant;

import com.github.leosant.services.FilesServices;

import java.io.File;
import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {

        renameWithValueDirectory();
    }

    public static void renameWithValueDirectory() throws IOException {

         FilesServices.factoryFile()
                 .getFiles();
    }

    public static void renameWithValueFile() throws IOException {

        FilesServices.factoryFile()
                .getFiles("", new File(""), "TEST");
    }
}