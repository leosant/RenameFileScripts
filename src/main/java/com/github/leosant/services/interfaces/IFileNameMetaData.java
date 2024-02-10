package com.github.leosant.services.interfaces;

import java.io.File;

public interface IFileNameMetaData {

    String formatNameMetaData(File file);

    String formatNameMetaData(String destinyPath, File file, String fileName);
}
