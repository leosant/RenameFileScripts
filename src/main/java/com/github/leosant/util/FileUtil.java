package com.github.leosant.util;

import org.apache.commons.lang3.StringUtils;

public class FileUtil {

    public static String getDestiny(String path, String newNamedDirectory) {
        return path.concat("/")
                .concat("renomeados/")
                .concat(newNamedDirectory.replaceAll(" ", ""));
    }

    public static String getDateFormatted(String date, String time) {
        return date.concat("-")
                .concat(time);
    }

    public static boolean isTextIncomplete(String date, String recipient) {
        return StringUtils.isEmpty(date)
                || StringUtils.isEmpty(recipient);
    }
}
