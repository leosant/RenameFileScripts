package com.github.leosant.services.interfaces;

import com.github.leosant.model.dto.ArchiveDataDto;

import java.io.File;
import java.util.Optional;

public interface ITypeArchive {

    Optional<ArchiveDataDto> convertTo(File file);
}
