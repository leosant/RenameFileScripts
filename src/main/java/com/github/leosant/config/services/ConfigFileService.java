package com.github.leosant.config.services;

import com.github.leosant.model.enums.NameFileEnum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ConfigFileService {

    private static final String PATH;

    private final File file;

    public ConfigFileService() {
        this.file = new File(PATH);
    }

    public List<NameFileEnum> loadText() throws IOException {
        List<NameFileEnum> nameFileEnums = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))){
            String line;
            while (Objects.nonNull(line = bufferedReader.readLine())) {
                convertToNameFile(line).ifPresent(nameFileEnums::add);
            }

        } catch (IOException ioException) {
            throw new IOException("Error ao converter o arquivo de configuração de nomes", ioException.getCause());
        }
        return nameFileEnums;
    }

    private Optional<NameFileEnum> convertToNameFile(String line) {
        return Optional.ofNullable(NameFileEnum.convertStringToEnum(line));
    }

    static {
        final String HOME = System.getProperty("user.home");
        PATH = HOME + "/temp/renameScript/config/Lista_ItemsFinanceiro.txt";
    }
}
