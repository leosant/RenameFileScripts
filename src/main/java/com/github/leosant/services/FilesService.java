package com.github.leosant.services;

import com.github.leosant.config.model.enums.MessageLogEnum;
import com.github.leosant.config.services.Log;
import com.github.leosant.model.dto.ArchiveDataDto;
import com.github.leosant.model.dto.ArchiveDataPresentDtos;
import com.github.leosant.model.dto.ArchiveErroDto;
import com.github.leosant.model.enums.InstanceArchiveEnum;
import com.github.leosant.model.enums.NameFileEnum;
import com.github.leosant.services.filenames.FileNameService;
import com.github.leosant.services.interfaces.IFileName;
import com.github.leosant.services.interfaces.IManagerFile;
import org.slf4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.github.leosant.config.model.enums.MessageLogEnum.Constants;

public class FilesService implements IManagerFile {
    private static final String PATH;
    private final Logger log = Log.local(FilesService.class);
    private final ArchiveDataPresentDtos archiveDataPresentDtos = new ArchiveDataPresentDtos();
    private final File file;
    private final IFileName fileName;
    private List<NameFileEnum> nameFileEnums;

    private FilesService() {
        this.file = new File(PATH);
        this.fileName = new FileNameService();
    }

    public static IManagerFile factoryFile() {
        return new FilesService();
    }

    /**
     * Salva a instancia de configuracao para de para dos nomes dos comprovantes
     */
    @Override
    public void setNameFileEnums(List<NameFileEnum> nameFileEnums) {
        this.nameFileEnums = nameFileEnums;
    }

    /**
     * Altera o nome do arquivo segundo o nome de sua respectiva pasta
     * E encaminha para o mesmo local de destino
     */
    @Override
    public void getFiles() {
        getFiles(null, file, null);
    }

    @Override
    public void getFiles(String destinyPath, File files, String nameArchive) {
        for (File file : Objects.requireNonNull(files.listFiles())) {
            if (file.isDirectory() && Objects.requireNonNull(file.list()).length > 0) {
                getFiles(destinyPath, file, nameArchive);
            } else {
                log.info(Constants.PREPARED_ARCHIVE);
                preparedArchiveData(file);
            }
        }

        if (archiveDataPresentDtos.getArchiveErroDtos().isEmpty()
                || archiveDataPresentDtos.getArchiveDataDtos().isEmpty()) {
            return;
        }

        renameArchiveDataDtos(archiveDataPresentDtos.getArchiveDataDtos());
        logMessageOfErrors(archiveDataPresentDtos.getArchiveErroDtos());

        log.info(MessageLogEnum.Constants.QUIT);
    }

    private void preparedArchiveData(File file) {
        List<String> path = Arrays.stream(file.getPath().split("\\.")).toList();

        if (path.isEmpty()) {
            return;
        }

        Optional<String> isArchivePermitted = Arrays.stream(path.get(0).split("\\\\"))
                .filter(namedArchives -> namedArchives.equalsIgnoreCase("renomeados"))
                .findFirst();

        if (isArchivePermitted.isPresent()) {
            return;
        }

        int indexOfExtension = path.size()-1;
        InstanceArchiveEnum.instanceOf(path.get(indexOfExtension))
                .ifPresentOrElse(typeInstance -> {
                            ArchiveDataDto archiveDataDto = typeInstance.convertTo(file).orElse(null);
                            if (Objects.nonNull(archiveDataDto)) {
                                archiveDataPresentDtos.getArchiveDataDtos().add(archiveDataDto);
                            }
                        },
                        () -> archiveDataPresentDtos.getArchiveErroDtos()
                                .add(getArchiveErroDtoToExtensionNotFound(file)));
    }

    private ArchiveErroDto getArchiveErroDtoToExtensionNotFound(File file) {
        ArchiveErroDto archiveErroDto = new ArchiveErroDto();
        archiveErroDto.setPath(file.getPath());
        archiveErroDto.setMessageErro("Sem implementação para esse tipo de extensão do arquivo");
        return archiveErroDto;
    }

    private void renameArchiveDataDtos(List<ArchiveDataDto> archiveDataDtos) {
        log.info("QUANTIDADE DE ARQUIVOS PRONTOS PARA SER RENOMEADOS É DE: " + archiveDataDtos.size());

        archiveDataDtos.forEach(archiveDataDto -> {
            File file = new File(archiveDataDto.getDestiny());
            if (file.isDirectory()) {
                executeRenamed(archiveDataDto);
                return;
            }

            if (file.mkdirs()) {
                executeRenamed(archiveDataDto);
            }
        });
    }

    private void executeRenamed(ArchiveDataDto archiveDataDto) {
        boolean isRenamed = renameToFile(
                fileName.formatName(archiveDataDto), archiveDataDto.getFile());
        logMessageRenamed(archiveDataDto, isRenamed);
    }

    private boolean renameToFile(String newName, File file) {
        return file.renameTo(new File(newName));
    }

    private void logMessageRenamed(ArchiveDataDto archiveDataDto, boolean isRenamed) {
        if (isRenamed) {
            log.info(Constants.RENAMED_SUCCESS + archiveDataDto.getNamedArchive());
        } else {
            log.error(Constants.RENAMED_FAIL + archiveDataDto.getFile().getName());
        }
    }

    private void logMessageOfErrors(List<ArchiveErroDto> archiveErroDtos) {
        log.info("QUANTIDADE DE ARQUIVOS COM ERROS É DE: " + archiveErroDtos.size());

        archiveErroDtos.forEach(archiveErroDto -> log.error(archiveErroDto.getMessageErro().concat(" - ")
                .concat(archiveErroDto.getPath())));
    }

    static {
        final String HOME = System.getProperty("user.home");
        PATH = HOME + "/temp/renameScript/comprovantes";
    }
}