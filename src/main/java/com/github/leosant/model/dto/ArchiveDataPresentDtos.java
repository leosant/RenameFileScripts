package com.github.leosant.model.dto;

import java.util.LinkedList;
import java.util.List;

public class ArchiveDataPresentDtos {

    private final List<ArchiveDataDto> archiveDataDtos = new LinkedList<>();

    private final List<ArchiveErroDto> archiveErroDtos = new LinkedList<>();

    public List<ArchiveDataDto> getArchiveDataDtos() {
        return archiveDataDtos;
    }

    public List<ArchiveErroDto> getArchiveErroDtos() {
        return archiveErroDtos;
    }
}
