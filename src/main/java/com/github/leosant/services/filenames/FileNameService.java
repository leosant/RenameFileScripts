package com.github.leosant.services.filenames;

import com.github.leosant.model.dto.ArchiveDataDto;
import com.github.leosant.services.interfaces.IFileName;
import com.github.leosant.services.interfaces.IFileNameMetaData;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileNameService implements IFileName {
    IFileNameMetaData fileNameMetaDataService;

    public FileNameService() {
        this.fileNameMetaDataService = new FileNameMetaDataService();
    }

    @Override
    public String formatName(ArchiveDataDto archiveDataDto) {
        return archiveDataDto.getDestiny()
                .concat("/")
                .concat(archiveDataDto.getNamedArchive())
                .concat("(")
                .concat(archiveDataDto.getTransactionDate())
                .concat(")")
                .concat(archiveDataDto.getExtensionEnum().getName());
    }

    @Override
    public String formatNameToFileRepeated(ArchiveDataDto archiveDataDto) {
         String identificador = UUID.randomUUID().toString();

        return archiveDataDto.getDestiny()
                .replace("/renomeados", "")
                .concat("(ARQUIVO_REPETIDO)")
                .concat(identificador.substring(0, 4))
                .concat(archiveDataDto.getExtensionEnum().getName());
    }

    @Override
    public String creationDateTimeMetaData(File file) throws IOException {
        return fileNameMetaDataService.creationDateTimeMetaData(file);
    }

    @Override
    public String formatNameMetaData(File file) {
        return fileNameMetaDataService.formatNameMetaData(file);
    }

    @Override
    public String formatNameMetaData(String destinyPath, File file, String fileName) {
        return fileNameMetaDataService.formatNameMetaData(destinyPath, file, fileName);
    }
}
