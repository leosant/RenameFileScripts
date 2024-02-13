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
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static com.github.leosant.config.model.enums.MessageLogEnum.Constants;

public class ImageStrategy implements ITypeArchive {
    private final Logger log = Log.local(ImageStrategy.class);
    private final ExtensionEnum extensionEnum;

    public ImageStrategy(ExtensionEnum extensionEnum) {
        this.extensionEnum = extensionEnum;
    }

    @Override
    public Optional<ArchiveDataDto> convertTo(File file) {
        ITesseract tesseract = configuratorTesseract();
        log.info(Constants.STARTING_IMAGE + file.getName());

        try {
            String text = tesseract.doOCR(file);
            List<String> textList = Arrays.stream(text.split("\\n"))
                    .filter(StringUtils::isNotEmpty)
                    .toList();

            Optional<ArchiveDataDto> archiveDataDtoOptional = getArchiveDataDto(file, textList);

            if (archiveDataDtoOptional.isEmpty()) {
                return Optional.empty();
            }

            log.info(Constants.RENAMED_ARCHIVE + archiveDataDtoOptional.get().getNamedArchive());

            return archiveDataDtoOptional;
        } catch (IOException | TesseractException | InterruptedException e) {
            log.error(Constants.CONVERSION_FAIL + file.getName().concat(" ") + e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<ArchiveDataDto> getArchiveDataDto(File file, List<String> textOfPdf) throws IOException, InterruptedException {
        LinkedHashMap<String, String> textExtract = TextValidator.getExtractText(textOfPdf);

        if (FileUtil.isNameEmpty(textExtract.get("favorec"), file.getName())) {
            return Optional.empty();
        }

        return Optional.of(builderArchiveDataDto(file, textExtract));
    }

    private ArchiveDataDto builderArchiveDataDto(File file, LinkedHashMap<String, String> textExtract) throws InterruptedException, IOException {
        ArchiveDataDto archiveDataDto = new ArchiveDataDto(file, extensionEnum);
        archiveDataDto.setNamedArchive(textExtract.get("favorec"));
        archiveDataDto.setDestiny(FileUtil.getDestiny(file.getParent(), textExtract.get("favorec")));
        archiveDataDto.setTransactionDate(FileUtil.chooseTransactionDate(textExtract, file));
        return archiveDataDto;
    }

    private ITesseract configuratorTesseract() {
        ITesseract tesseract = new Tesseract1();
        tesseract.setLanguage("por");
        return tesseract;
    }
}
