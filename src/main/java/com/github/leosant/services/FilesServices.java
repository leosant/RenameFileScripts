package com.github.leosant.services;

import com.github.leosant.services.interfaces.IFileName;
import com.github.leosant.services.interfaces.IManagerFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FilesServices implements IManagerFile {
    private static final String PATH;
    private final File files;
    private final IFileName nameFile;

    private FilesServices() {
        this.files = new File(PATH);
        this.nameFile = new FileName();
    }

    public static IManagerFile factoryFile() {
        return new FilesServices();
    }

    /**
     * Altera o nome do arquivo segundo o nome de sua respectiva pasta
     * E encaminha para o mesmo local de destino
     * @throws IOException
     */
    @Override
    public void getFiles() throws IOException {

        getFiles(null, files, null);
    }

    @Override
    public void getFiles(String destinyPath, File files, String nameArchive) throws IOException {

        for (File f : Objects.requireNonNull(files.listFiles())) {

            if (f.isDirectory() && Objects.requireNonNull(f.list()).length > 0)
            {
                getFiles(destinyPath, f, nameArchive);
            }
            else
            {
                String name = nameFile.fileName(destinyPath, f, nameArchive);

                if (f.renameTo(new File(name)))
                {
                    System.out.println("ARQUIVO RENOMEADO COM SUCESSO" + name);
                }
                else
                {
                    throw new IOException("NAO FOI POSSIVEL RENOMEAR O ARQUIVO");
                }
            }
        }
    }

    static {
        final String HOME = System.getProperty("user.home");
        PATH = HOME+"/temp/comprovantes";
    }
}