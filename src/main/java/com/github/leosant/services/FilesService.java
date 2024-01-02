package com.github.leosant.services;

import com.github.leosant.model.enums.InstanceArchiveEnum;
import com.github.leosant.model.enums.NameFileEnum;
import com.github.leosant.services.interfaces.IFileName;
import com.github.leosant.services.interfaces.IManagerFile;
import com.github.leosant.services.interfaces.ITypeArchive;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.tools.PDFText2HTML;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FilesService implements IManagerFile {
    private static final String PATH;
    private final File file;
    private final IFileName nameFile;
    private List<NameFileEnum> nameFileEnums;

    private FilesService() {
        this.file = new File(PATH);
        this.nameFile = new FileNameService();
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
    public void getFiles() throws IOException {
        getFiles(null, file, null);
    }

    @Override
    public void getFiles(String destinyPath, File files, String nameArchive) throws IOException {

        for (File file : Objects.requireNonNull(files.listFiles())) {
            if (file.isDirectory() && Objects.requireNonNull(file.list()).length > 0) {
                getFiles(destinyPath, file, nameArchive);
            } else {
                InstanceArchiveEnum.instanceOf(file.getPath().split("\\.")[1])
                        .ifPresent(ITypeArchive::convertTo);
                RandomAccessRead randomAccessRead = new RandomAccessReadBufferedFile(file);
                PDFParser parser = new PDFParser(randomAccessRead);
                PDDocument pdDocument = parser.parse();
                PDFText2HTML pdfText2HTML = new PDFText2HTML();

                String textoComprovateComTagsHtml = StringEscapeUtils.unescapeHtml4(pdfText2HTML.getText(pdDocument));

                String[] textoCompravateComTagsHtmlVector = textoComprovateComTagsHtml.replaceAll("\r","")
                        .replaceAll("</p>", "")
                        .split("<p>");

                Map<String, String> teste = new HashMap<>();
                for (String texto : textoCompravateComTagsHtmlVector) {
                    if (texto.contains("Para")) {
                        String valorPara = texto.split("\n")[1];

                        teste.put("para", valorPara);
                        return;
                    }
                }

                randomAccessRead.close();
                pdDocument.close();

//                String name = nameFile.fileName(destinyPath, file, nameArchive);

//                boolean isArchiveRenamed = renameToFile(name, file);
//                if (isArchiveRenamed) {
//                    System.out.println("ARQUIVO RENOMEADO COM SUCESSO" + name);
//                } else {
                    throw new IOException("NAO FOI POSSIVEL RENOMEAR O ARQUIVO");
//                }
            }
        }
    }

    private boolean renameToFile(String newName, File file) {
        return file.renameTo(new File(newName));
    }

    static {
        final String HOME = System.getProperty("user.home");
        PATH = HOME + "/temp/renameScript/comprovantes";
    }
}