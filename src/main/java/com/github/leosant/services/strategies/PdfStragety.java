package com.github.leosant.services.strategies;

import com.github.leosant.config.services.Log;
import com.github.leosant.model.dto.ArchiveDataDto;
import com.github.leosant.model.enums.ExtensionEnum;
import com.github.leosant.services.interfaces.ITypeArchive;
import com.github.leosant.services.validator.TextValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.tools.PDFText2HTML;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Optional;

import static com.github.leosant.config.model.enums.MessageLogEnum.Constants;

public class PdfStragety implements ITypeArchive {
    private final Logger log = Log.local(PdfStragety.class);

    @Override
    public Optional<ArchiveDataDto> convertTo(File file) {

        try (RandomAccessReadBufferedFile randomAccessRead = new RandomAccessReadBufferedFile(file)) {
            Thread.sleep(1000);

            String textInHtml = getTextInHtml(randomAccessRead);
            String[] textOfPdf = getTextWithTagsSubstract(textInHtml);

            if (isPdfEmpty(textOfPdf)
                    || isPdfProgramatic(textOfPdf)) {
                log.warn(Constants.QUIT);
                return Optional.empty();
            }

            ArchiveDataDto archiveDataDto = new ArchiveDataDto(file, ExtensionEnum.PDF);

            LinkedHashMap<String, String> textExtract = TextValidator.getExtractText(textOfPdf);

            archiveDataDto.setNamedArchive(textExtract.get("para"));
            archiveDataDto.setDestiny(getDestiny(file.getParent(), textExtract.get("para")));
            archiveDataDto.setTransactionDate(getDateFormatted(textExtract.get("dataehora")));

            log.info(Constants.RENAMEDING_ARCHIVE + archiveDataDto.getNamedArchive());
            randomAccessRead.close();

            return Optional.of(archiveDataDto);
        } catch (Exception e) {
            log.error(Constants.CONVERSION_FAIL + e.getCause());
            return Optional.empty();
        }
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

    private String getDateFormatted(String dataEhora) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss");
        LocalDateTime localDate = LocalDateTime.parse(dataEhora, dateTimeFormatter);
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd'D'MM'M'yyyy'Y'-HH'h'mm'm'ss's'");
        return localDate.format(dateTimeFormatter);
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

    private String getDestiny(String path, String newNamedDirectory) {
        return path.concat("/")
                .concat("renomeados/")
                .concat(newNamedDirectory.replaceAll(" ", ""));
    }
}
