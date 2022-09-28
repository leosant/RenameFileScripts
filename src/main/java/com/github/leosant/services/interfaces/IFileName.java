package com.github.leosant.services.interfaces;

import java.io.File;

public interface IFileName {

    String fileName(File file);

    String fileName(String destinyPath, File file, String fileName);
}
