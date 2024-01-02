package com.github.leosant.services;

import com.github.leosant.services.interfaces.IFileName;
import com.github.leosant.services.interfaces.IManagerFile;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.PDFBox;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;

public class FilesServices implements IManagerFile {
    private static final String PATH;
    private final File files;
    private final IFileName nameFile;

    private FilesServices() {
        this.files = new File(PATH);
        this.nameFile = new FileName();
    }

    public static IManagerFile factoryFile() {
        return new FilesServices();
    }

    /**
     * Altera o nome do arquivo segundo o nome de sua respectiva pasta
     * E encaminha para o mesmo local de destino
     */
    @Override
    public void getFiles() throws IOException {
        getFiles(null, files, null);
    }

    @Override
    public void getFiles(String destinyPath, File files, String nameArchive) throws IOException {

        for (File f : Objects.requireNonNull(files.listFiles())) {
            if (f.isDirectory() && Objects.requireNonNull(f.list()).length > 0) {
                getFiles(destinyPath, f, nameArchive);
            } else {
//                PDFParser parser;
//                RandomAccessRead randomAccessRead = new RandomAccessReadBufferedFile(f);
//                parser = new PDFParser(randomAccessRead);
//                PDDocument ttt = parser.parse();

//                PDFTextStripper pdfTextStripper = new PDFTextStripper();
//                String text = pdfTextStripper.getText(ttt);

                String text = "Comprovante do Pagamento 14/04/2023 - 15:43:54 Valor pago R$ 58,13 Identificação do pagamento 9hRvok6q1OlcfiKpZiPX2vpqRDR6yYf1tNq Forma de pagamento Ag 2327 Cc 1023266-4 Dados do recebedor Para Tesoura De Ouro CNPJ 36***.***/0001-5* Instituição ITAU UNIBANCO S.A.Dados do pagador De Leonardo Dos Santos Bonfim CPF ***.056.991-**Instituição BCO SANTANDER (BRASIL) S.A.ID/Transação E9040088820230414184310258449438 Data e hora da transação 14/04/2023 - 15:43:54 Comprovante do Pagamento 1/2 Código de autenticação B0D04360582843251587107 4004-3535 (Capitais e Regiões Metropolitanas)0800-702-3535 (Demais Localidades)SAC 0800-762-7777 Ouvidoria 0800-726-0332 Central de Atendimento Santander Comprovante do Pagamento 2/2";

                System.out.println(text);

                String name = nameFile.fileName(destinyPath, f, nameArchive);
                if (renameToFile(name, f)) {
                    System.out.println("ARQUIVO RENOMEADO COM SUCESSO" + name);
                } else {
                    throw new IOException("NAO FOI POSSIVEL RENOMEAR O ARQUIVO");
                }
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