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

        try (RandomAccessReadBufferedFile randomAccessRead = new RandomAccessReadBufferedFile(file)) {
            log.info("INICIANDO TRATATIVA POR PDF PARA O ARQUIVO: " + file.getName());
            Thread.sleep(1000);

            String textInHtml = getTextInHtml(randomAccessRead);
            String[] textOfPdf = getTextWithTagsSubstract(textInHtml);

            if (isPdfEmpty(textOfPdf)
                    || isPdfProgramatic(textOfPdf)) {
                log.warn(Constants.QUIT);
                Thread.sleep(1000);
                return Optional.empty();
            }

            ArchiveDataDto archiveDataDto = getArchiveDataDto(file, Arrays.asList(textOfPdf));

            if (Objects.isNull(archiveDataDto)) {
                return Optional.empty();
            }

            log.info(Constants.RENAMEDING_ARCHIVE + archiveDataDto.getNamedArchive());
            randomAccessRead.close();

            return Optional.of(archiveDataDto);
        } catch (Exception e) {
            log.error(Constants.CONVERSION_FAIL + e.getMessage());
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

        archiveDataDto.setNamedArchive(textExtract.get("para"));
        archiveDataDto.setDestiny(FileUtil.getDestiny(file.getParent(), textExtract.get("para")));
        archiveDataDto.setTransactionDate(FileUtil.getDateFormatted(textExtract.get("data"), textExtract.get("horario")));
        return archiveDataDto;
    }

    private boolean isPdfProgramatic(String[] textOfPdf) throws InterruptedException {
        boolean isProgrammaticFile = Arrays.stream(textOfPdf)
                .anyMatch(text -> StringUtils.containsIgnoreCase(text, "agendamento"));

        if (isProgrammaticFile) {
            log.warn(Constants.PROGRAMATIC_FILE);
            Thread.sleep(1000);
            return true;
        }
        return false;
    }

    private static boolean isPdfEmpty(String[] textOfPdf) {
        return textOfPdf.length == 0;
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
