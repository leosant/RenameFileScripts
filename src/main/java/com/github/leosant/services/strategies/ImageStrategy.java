package com.github.leosant.services.strategies;

import com.github.leosant.config.services.Log;
import com.github.leosant.model.dto.ArchiveDataDto;
import com.github.leosant.model.enums.ExtensionEnum;
import com.github.leosant.services.interfaces.ITypeArchive;
import com.github.leosant.services.validator.TextValidator;
import com.github.leosant.util.FileUtil;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.File;
import java.util.*;

import static com.github.leosant.config.model.enums.MessageLogEnum.Constants;

public class ImageStrategy implements ITypeArchive {
    private final Logger log = Log.local(ImageStrategy.class);
    private final ExtensionEnum extensionEnum;

    public ImageStrategy(ExtensionEnum extensionEnum) {
        this.extensionEnum = extensionEnum;
    }

    @Override
    public Optional<ArchiveDataDto> convertTo(File file) {
        ITesseract tesseract = new Tesseract1();
        tesseract.setLanguage("por");

        try {
            log.info("INICIANDO TRATATIVA POR IMAGEM PARA O ARQUIVO: " + file.getName());

            String text = tesseract.doOCR(file);
            List<String> textList = Arrays.stream(text.split("\\n"))
                    .filter(StringUtils::isNotEmpty)
                    .toList();

            ArchiveDataDto archiveDataDto = getArchiveDataDto(file, textList);

            if (Objects.isNull(archiveDataDto)) {
                return Optional.empty();
            }

            log.info(Constants.RENAMEDING_ARCHIVE + archiveDataDto.getNamedArchive());

            return Optional.of(archiveDataDto);
        } catch (InterruptedException | TesseractException e) {
            log.error(Constants.CONVERSION_FAIL + file.getName().concat(" ") + e.getMessage());
            return Optional.empty();
        }
    }

    private ArchiveDataDto getArchiveDataDto(File file, List<String> textOfPdf) throws InterruptedException {
        ArchiveDataDto archiveDataDto = new ArchiveDataDto(file, extensionEnum);

        LinkedHashMap<String, String> textExtract = TextValidator.getExtractText(textOfPdf);

        if (FileUtil.isTextIncomplete(textExtract.get("data"), textExtract.get("favoreci"))) {
            log.error("ARQUIVO NÃO POSSUI DATA OU NOME - ARQUIVO: " + file.getName());
            Thread.sleep(1000);
            return null;
        }

        archiveDataDto.setNamedArchive(textExtract.get("favoreci"));
        archiveDataDto.setDestiny(FileUtil.getDestiny(file.getParent(), textExtract.get("favoreci")));
        archiveDataDto.setTransactionDate(FileUtil.getDateFormatted(textExtract.get("data"), textExtract.get("horario")));

        return archiveDataDto;
    }
}
