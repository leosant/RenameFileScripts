package com.github.leosant;

import com.github.leosant.config.services.ConfigFileService;
import com.github.leosant.config.services.Log;
import com.github.leosant.model.enums.NameFileEnum;
import com.github.leosant.services.FilesService;
import com.github.leosant.services.interfaces.IManagerFile;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.github.leosant.config.model.enums.MessageLogEnum.Constants;

public class Application {

    public static void main(String[] args) {
        Logger log = Log.local(Application.class);
        try {
            log.info(Constants.INITIAL_AUTOMATIZED);
            Thread.sleep(1000);

            ConfigFileService confiFileService = new ConfigFileService();
            List<NameFileEnum> nameFileEnums = confiFileService.loadText();
            renameWithValueDirectory(nameFileEnums);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }

    public static void renameWithValueDirectory(List<NameFileEnum> nameFileEnums) {
         IManagerFile filesService = FilesService.factoryFile();
         filesService.setNameFileEnums(nameFileEnums);
         filesService.getFiles();
    }

}