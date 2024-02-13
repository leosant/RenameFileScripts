package com.github.leosant.services.interfaces;

import java.io.File;
import java.io.IOException;

public interface IFileNameMetaData {

    String creationDateTimeMetaData(File file) throws IOException;

    String formatNameMetaData(File file);

    String formatNameMetaData(String destinyPath, File file, String fileName);
}
