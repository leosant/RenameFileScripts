package com.github.leosant.services.strategies;

import com.github.leosant.config.services.Log;
import com.github.leosant.model.dto.ArchiveDataDto;
import com.github.leosant.model.enums.ExtensionEnum;
import com.github.leosant.services.interfaces.ITypeArchive;
import com.github.leosant.services.validator.TextValidator;
import com.github.leosant.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.tools.PDFText2HTML;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.github.leosant.config.model.enums.MessageLogEnum.Constants;

public class PdfStragety implements ITypeArchive {
    private final Logger log = Log.local(PdfStragety.class);
    private final ExtensionEnum extensionEnum;

    public PdfStragety(ExtensionEnum extensionEnum) {
        this.extensionEnum = extensionEnum;
    }

    @Override
    public Optional<ArchiveDataDto> convertTo(File file) {
        log.info(Constants.STARTING_PDF + file.getName());

        try (RandomAccessReadBufferedFile randomAccessRead = new RandomAccessReadBufferedFile(file)) {
            Thread.sleep(1000);

            String textInHtml = getTextInHtml(randomAccessRead);
            List<String> textOfPdf = Arrays.stream(getTextWithTagsSubstract(textInHtml)).toList();

            if (isPdfEmpty(textOfPdf) || isPdfProgramatic(textOfPdf)) {
                log.warn(Constants.QUIT);
                return Optional.empty();
            }

            Optional<ArchiveDataDto> archiveDataDtoOptional = getArchiveDataDto(file, textOfPdf);

            if (archiveDataDtoOptional.isEmpty()) {
                return Optional.empty();
            }

            log.info(Constants.RENAMED_ARCHIVE + archiveDataDtoOptional.get().getNamedArchive());
            randomAccessRead.close();

            return archiveDataDtoOptional;
        } catch (Exception e) {
            log.error(Constants.CONVERSION_FAIL + e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<ArchiveDataDto> getArchiveDataDto(File file, List<String> textOfPdf) throws InterruptedException, IOException {
        LinkedHashMap<String, String> textExtract = TextValidator.getExtractText(textOfPdf);

        if (FileUtil.isNameEmpty(textExtract.get("para"), file.getName())) {
            return Optional.empty();
        }

        return Optional.of(builderArchiveDataDto(file, textExtract));
    }

    private ArchiveDataDto builderArchiveDataDto(File file, LinkedHashMap<String, String> textExtract) throws IOException, InterruptedException {
        ArchiveDataDto archiveDataDto = new ArchiveDataDto(file, extensionEnum);
        archiveDataDto.setNamedArchive(textExtract.get("para"));
        archiveDataDto.setDestiny(FileUtil.getDestiny(file.getParent(), textExtract.get("para")));
        archiveDataDto.setTransactionDate(FileUtil.chooseTransactionDate(textExtract, file));
        return archiveDataDto;
    }


    private boolean isPdfProgramatic(List<String> textOfPdf) throws InterruptedException {
        boolean isProgrammaticFile = textOfPdf.stream()
                .anyMatch(text -> StringUtils.containsIgnoreCase(text, "agendamento"));

        if (isProgrammaticFile) {
            log.warn(Constants.PROGRAMMATIC_FILE);
            Thread.sleep(1000);
            return true;
        }
        return false;
    }

    private static boolean isPdfEmpty(List<String> textOfPdf) {
        return textOfPdf.isEmpty();
    }

    private static String[] getTextWithTagsSubstract(String textInHtml) {
        return textInHtml.replaceAll("\r", "")
                .replaceAll("</p>", "")
                .split("<p>");
    }

    private String getTextInHtml(RandomAccessReadBufferedFile randomAccessRead) throws IOException {
        PDFParser parser = new PDFParser(randomAccessRead);
        PDDocument pdDocument = parser.parse();
        PDFText2HTML pdfText2HTML = new PDFText2HTML();

        String textInHtml = StringEscapeUtils.unescapeHtml4(pdfText2HTML.getText(pdDocument));
        pdDocument.close();

        return textInHtml;
    }
}
