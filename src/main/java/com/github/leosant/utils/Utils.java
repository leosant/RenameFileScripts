package com.github.leosant.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class Utils {

    private Utils() {}
    private static final String PATH;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy");

    public static void renameFileStream() {

        File filePath = new File(PATH);
        String nameType = null;
        List<String> nameFile;
        Date date = null;


        //TODO: Tratar com timezone

        try
        {
            for (File file : Objects.requireNonNull(filePath.listFiles())) {

                for (File f : Objects.requireNonNull(file.listFiles())) {

                    nameFile = List.of(f.getName().split("[^\\d-.]"));

                    for (String s : nameFile) {

                        if (StringUtils.isNotBlank(s)) {

                            if (!s.contains("-")) {
                                long timestamp = Long.parseLong(s);
                                date = new Date(timestamp);
                            }
                            else {

                                nameType = s;
                            }
                        }

                    }

                    String[] splitParent = f.getParent().split("/");
                    String dateFormat = nameType != null ? nameType : SDF.format(date);
                    String nameDocument =  f.getParent() + "/" + splitParent[splitParent.length-1] + "_" + dateFormat;

                    if (f.renameTo(new File(nameDocument))) {
                        nameType = "NODATE";
                        Logger.getLogger("RENOMEADO COM SUCESSO!");
                    }

                    Logger.getLogger("OCORREU UM ERROR");
                }
            }

        }
        catch (Exception e)
        {
            e.getMessage();
        }
    }

    static {
        final String HOME = System.getProperty("user.home");
        PATH = HOME+"/temp/comprovantes";
    }
}