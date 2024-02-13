package com.github.leosant.services.filenames;

import com.github.leosant.config.services.Log;
import com.github.leosant.services.interfaces.IFileNameMetaData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.github.leosant.config.model.enums.MessageLogEnum.Constants;

class FileNameMetaDataService implements IFileNameMetaData {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd'D'MM'M'yyyy'Y'");
    private final Logger log = Log.local(FileNameMetaDataService.class);

    @Override
    public String creationDateTimeMetaData(File file) throws IOException {
        log.warn(Constants.INFORMATION_FILE_METADATA + file.getName());
        return creationDateTime(file);
    }

    @Override
    public String formatNameMetaData(File file) {
        return formatNameMetaData(null, file, null);
    }

    @Override
    public String formatNameMetaData(String destinyPath, File file, String fileName) {

        try {
            String dateMetadata = creationDateTime(file);

            if (fileName != null) {
                return renameToFile(destinyPath,fileName + dateMetadata, file);
            }
            return renameToFile(destinyPath, dateMetadata, file);

        } catch (IOException e) {
            String onlyNumber = onlyNumberString(file);
            return validString(onlyNumber);
        }
    }

    private String creationDateTime(File file) throws IOException {
        return getFileCreationTime(Files.readAttributes(file.toPath(), BasicFileAttributes.class));
    }

    private String getFileCreationTime(BasicFileAttributes fileAttributes) {
        Date date = new Date(fileAttributes.creationTime().toMillis());
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

        return localDateTime.format(dateTimeFormatter);
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
                LocalDateTime localDateTime = LocalDateTime.ofInstant(
                        new Date(timestamp).toInstant(),
                        ZoneId.systemDefault());

                return localDateTime.format(dateTimeFormatter);
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
