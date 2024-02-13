package com.github.leosant;

import com.github.leosant.config.services.Log;
import com.github.leosant.services.FilesService;
import com.github.leosant.services.interfaces.IManagerFile;
import org.slf4j.Logger;

import static com.github.leosant.config.model.enums.MessageLogEnum.Constants;

public class Application {
    private static final Logger log = Log.local(Application.class);

    public static void main(String[] args) {
        try {
            log.info(Constants.INITIAL_AUTOMATIZED);
            Thread.sleep(1000);

            renameWithValueDirectory();
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }

    public static void renameWithValueDirectory() {
        log.info(Constants.PREPARED_ARCHIVE);
         IManagerFile filesService = FilesService.factoryFile();
         filesService.getFiles();
    }

}