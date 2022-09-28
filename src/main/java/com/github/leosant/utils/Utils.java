package com.github.leosant.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Utils {

    private Utils() {}
    private static final String PATH;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy");

    public static void renameFileStream() {

        File filePath = new File(PATH);
        getFiles(filePath);

    }

    public static void getFiles(File file) {

        for (File f : Objects.requireNonNull(file.listFiles())) {

            if (f.isDirectory() && Objects.requireNonNull(f.list()).length > 0) {
                getFiles(f);
            }

                String name = onlyNumberString(f);
                String nameFileDate = validString(name);

                if (StringUtils.isNotBlank(nameFileDate)) {
                    renameToFile(nameFileDate, f);
                }

        }
    }

    private static boolean renameToFile(String extractFile, File file) {

        String[] splitParent = file.getParent().split("/");
        String nameDocument =  file.getParent() + "/" + splitParent[splitParent.length-1] + "_" + extractFile;

        return file.renameTo(new File(nameDocument));
    }

    private static String onlyNumberString(File file) {
        return file.getName().replaceAll("[^\\d-]", "");
    }

    private static String validString(String s) {

        if (StringUtils.isNotBlank(s)) {
            if (!s.contains("-")) {
                long timestamp = Long.parseLong(s);
                return SDF.format(new Date(timestamp));
            }
            else {
                if (s.length() > 8) {

                    return s.substring(0, 10);
                }
                else {
                    return s.substring(0, 6).concat("20" + s.substring(6, 8));
                }
            }
        }
        return "";
    }

    static {
        final String HOME = System.getProperty("user.home");
        PATH = HOME+"/temp/comprovantes";
    }
}