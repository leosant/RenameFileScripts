package com.github.leosant;

import com.github.leosant.model.enums.NameFileEnum;
import com.github.leosant.services.ConfigFileService;
import com.github.leosant.services.FilesService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {

    public static void main(String[] args) throws IOException {
        try {
            var confiFileService = new ConfigFileService();
            var nameFileEnums = confiFileService.loadText();
            renameWithValueDirectory(nameFileEnums);
        } catch (Exception exception) {
            Logger.getGlobal().log(Level.SEVERE, exception.getMessage());
        }
    }

    public static void renameWithValueDirectory(List<NameFileEnum> nameFileEnums) throws IOException {

         var filesService = FilesService.factoryFile();
         filesService.setNameFileEnums(nameFileEnums);
         filesService.getFiles();
    }

    public static void renameWithValueFile() throws IOException {

        FilesService.factoryFile()
                .getFiles("", new File(""), "TEST");
    }
}