package com.github.leosant.util;

import com.github.leosant.config.services.Log;
import com.github.leosant.services.filenames.FileNameService;
import com.github.leosant.services.interfaces.IFileName;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import static com.github.leosant.config.model.enums.MessageLogEnum.Constants;

public class FileUtil {
    private static final Logger log = Log.local(FileUtil.class);

    public static String getDestiny(String path, String newNamedDirectory) {
        return path.concat("/")
                .concat("renomeados/")
                .concat(newNamedDirectory.replaceAll(" ", ""));
    }

    public static String chooseTransactionDate(LinkedHashMap<String, String> dateTime, File file) throws IOException, InterruptedException {
        IFileName fileName = new FileNameService();

        if (isDateEmpty(dateTime.get("data"), file.getName())) {
            return fileName.creationDateTimeMetaData(file);
        }

        return getDateFormatted(dateTime.get("data"), dateTime.get("horario"));
    }

    public static boolean isNameEmpty(String namedArchive, String nameOld) throws InterruptedException {
        if (StringUtils.isNotEmpty(namedArchive)) {
            return false;
        }
        Thread.sleep(1000);
        log.error(Constants.ARCHIVE_NO_NAME + nameOld);
        return true;
    }

    private static boolean isDateEmpty(String date, String namedArchive) throws InterruptedException {
        if (StringUtils.isNotEmpty(date)) {
            return false;
        }
        Thread.sleep(1000);
        log.error(Constants.ARCHIVE_NO_DATE + namedArchive);
        return true;
    }

    private static String getDateFormatted(String date, String time) {
        return date.concat("-")
                .concat(time);
    }
}
