package com.github.leosant.services.interfaces;

import com.github.leosant.model.dto.ArchiveDataDto;

public interface IFileName extends IFileNameMetaData {

    String formatName(ArchiveDataDto archiveDataDto);
}
