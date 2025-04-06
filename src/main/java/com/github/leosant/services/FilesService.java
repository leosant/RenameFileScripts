package com.github.leosant.services;

import com.github.leosant.config.services.Log;
import com.github.leosant.model.dto.ArchiveDataDto;
import com.github.leosant.model.dto.ArchiveDataPresentDtos;
import com.github.leosant.model.dto.ArchiveErrorDto;
import com.github.leosant.model.enums.InstanceArchiveEnum;
import com.github.leosant.services.filenames.FileNameService;
import com.github.leosant.services.interfaces.IFileName;
import com.github.leosant.services.interfaces.IManagerFile;
import com.github.leosant.services.interfaces.ITypeArchive;
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

  private FilesService() {
    this.file = new File(PATH);
    this.fileName = new FileNameService();
  }

  public static IManagerFile factoryFile() {
    return new FilesService();
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
        log.info(Constants.FINDING_ARCHIVE);
        getFiles(destinyPath, file, nameArchive);
      } else {
        preparedArchiveData(file);
      }
    }

    renameArchives();
  }

  private void renameArchives() {
    if (isArchivesDataEmpty()) {
      return;
    }

    try {
      renameArchiveDataDtos(archiveDataPresentDtos.getArchiveDataDtos());
      logMessageOfErrors(archiveDataPresentDtos.getArchiveErroDtos());
      clearInstanceArchiveDataDPresentDtos();
    } catch (InterruptedException e) {
      log.error(Constants.THREAD_FAIL);
    }

    log.info(Constants.QUIT);
  }

  private void clearInstanceArchiveDataDPresentDtos() {
    log.info(Constants.CLEANING_ARCHIVE);
    archiveDataPresentDtos.getArchiveDataDtos().clear();
    archiveDataPresentDtos.getArchiveErroDtos().clear();
  }

  private boolean isArchivesDataEmpty() {
    return archiveDataPresentDtos.getArchiveErroDtos().isEmpty()
        && archiveDataPresentDtos.getArchiveDataDtos().isEmpty();
  }

  private void preparedArchiveData(File file) {
    List<String> path = Arrays.stream(file.getPath().split("\\.")).toList();

    if (path.isEmpty()) {
      return;
    }

    Optional<String> isPasteRenamed = Arrays.stream(path.get(0).split("\\\\"))
        .filter(namedArchives -> namedArchives.equalsIgnoreCase("renomeados"))
        .findFirst();

    if (isPasteRenamed.isPresent()) {
      return;
    }

    int indexOfExtension = path.size() - 1;
    InstanceArchiveEnum.instanceOf(path.get(indexOfExtension))
        .ifPresentOrElse(typeInstance -> preparedArchiveByExtension(file, typeInstance),
            () -> archiveDataPresentDtos.getArchiveErroDtos()
                .add(getArchiveErroDtoToExtensionNotFound(file, "Sem implementação para esse tipo de extensão do arquivo")));
  }

  private void preparedArchiveByExtension(File file, ITypeArchive typeInstance) {
    ArchiveDataDto archiveDataDto = typeInstance.convertTo(file).orElse(null);
    if (Objects.nonNull(archiveDataDto)) {
      archiveDataPresentDtos.getArchiveDataDtos().add(archiveDataDto);
    } else {
      archiveDataPresentDtos.getArchiveErroDtos()
          .add(getArchiveErroDtoToExtensionNotFound(file, "arquivo está nulo"));
    }
  }

  private ArchiveErrorDto getArchiveErroDtoToExtensionNotFound(File file, String message) {
    ArchiveErrorDto archiveErrorDto = new ArchiveErrorDto();
    archiveErrorDto.setPath(file.getPath());
    archiveErrorDto.setMessageError(message);
    return archiveErrorDto;
  }

  private void renameArchiveDataDtos(List<ArchiveDataDto> archiveDataDtos) throws InterruptedException {
    log.info("{}{}", Constants.ARCHIVE_DONE_RENAME, archiveDataDtos.size());

    for (ArchiveDataDto archiveDataDto : archiveDataDtos) {
      File file = new File(archiveDataDto.getDestiny());
      if (file.isDirectory()) {
        executeRenamed(archiveDataDto);
      } else if (file.mkdirs()) {
        executeRenamed(archiveDataDto);
      }
    }
  }

  private void executeRenamed(ArchiveDataDto archiveDataDto) throws InterruptedException {
    boolean isRenamed = renameToFile(
        fileName.formatName(archiveDataDto), archiveDataDto.getFile());

    logMessageRenamed(archiveDataDto, isRenamed);

    if (isRenamed) {
      return;
    }

    failRenamed(archiveDataDto);
  }

  private void failRenamed(ArchiveDataDto archiveDataDto) throws InterruptedException {
    Thread.sleep(1000);
    boolean isRenamed = renameToFile(fileName.formatNameToFileRepeated(archiveDataDto), archiveDataDto.getFile());
    log.warn("{}{}", Constants.TRY_RENAMED_AGAIN, archiveDataDto.getNamedArchive());
    logMessageRenamed(archiveDataDto, isRenamed);
  }

  private boolean renameToFile(String newName, File file) {
    return file.renameTo(new File(newName));
  }

  private void logMessageRenamed(ArchiveDataDto archiveDataDto, boolean isRenamed) {
    if (isRenamed) {
      log.info("{}{}", Constants.RENAMED_SUCCESS, archiveDataDto.getNamedArchive());
      return;
    }
    log.error("{}{}", Constants.RENAMED_FAIL, archiveDataDto.getFile().getName());
  }

  private void logMessageOfErrors(List<ArchiveErrorDto> archiveErrorDtos) {
    log.info("{}{}", Constants.ARCHIVE_FAIL_RENAME, archiveErrorDtos.size());

    archiveErrorDtos.forEach(archiveErrorDto ->
        log.error(archiveErrorDto.getMessageError().concat(" - ")
            .concat(archiveErrorDto.getPath())));
  }

  static {
    final String HOME = System.getProperty("user.home");
    PATH = HOME + "/temp/renameScript/comprovantes";
  }
}