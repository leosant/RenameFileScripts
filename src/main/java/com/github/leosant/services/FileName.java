package com.github.leosant.services;

import com.github.leosant.services.interfaces.IFileName;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileName implements IFileName {

    private final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public String fileName(File file) {

        return fileName(null, file, null);
    }

    @Override
    public String fileName(String destinyPath, File file, String fileName) {

        try {
            String dateMetadata = getFileCreationTime(Files.readAttributes(file.toPath(), BasicFileAttributes.class));

            if (fileName != null) {
                return renameToFile(destinyPath,fileName.concat("_") + dateMetadata, file);
            }
            return renameToFile(destinyPath, dateMetadata, file);

        } catch (IOException e) {
            String onlyNumber = onlyNumberString(file);
            return validString(onlyNumber);
        }
    }

    private String getFileCreationTime(BasicFileAttributes fileAttributes) {

        Date date = new Date(fileAttributes.creationTime().toMillis());
        return formatDate.format(date);
    }

    private String renameToFile(String destinyPath, String extractFile, File file) {

        if (destinyPath != null) {
            return destinyPath.concat(extractFile);
        }

        String[] nameSplitFile = file.getParent().split("/");
        return file.getParent() + "/" + nameSplitFile[nameSplitFile.length - 1] + "_" + extractFile;
    }

        private String onlyNumberString(File file) {
        return file.getName().replaceAll("[^\\d-]", "");
    }

    private String validString(String s) {

        if (StringUtils.isNotBlank(s)) {
            if (!s.contains("-")) {
                long timestamp = Long.parseLong(s);
                return formatDate.format(new Date(timestamp));
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
}
