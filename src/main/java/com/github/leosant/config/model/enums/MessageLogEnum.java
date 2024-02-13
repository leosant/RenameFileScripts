package com.github.leosant.config.model.enums;

public enum MessageLogEnum {

    RENAMED_SUCCESS("ARQUIVO RENOMEADO COM SUCESSO: "),
    RENAMED_FAIL("NÃO FOI POSSIVEL RENOMEAR O ARQUIVO(PROVAVELMENTE O ARQUIVO ESTÁ REPETIDO): "),
    INITIAL_AUTOMATIZED("INICIANDO AUTOMATIZAÇÃO DE RENOME DE COMPROVANTES..."),
    PREPARED_ARCHIVE("PREPARANDO ARQUIVO..."),
    RENAMED_ARCHIVE("ARQUIVO ESTÁ SENDO NOMEADO PARA -> "),
    CONVERSION_FAIL("CONVERSÃO DO ARQUIVO FALHOU: "),
    PROGRAMMATIC_FILE("ARQUIVO DE AGENDAMENTO. SUSPENDENDO ALTERAÇÃO..."),
    INFORMATION_FILE_METADATA("OBTENDO INFORMAÇÃO DE DATA ATRAVÉS DE METADATA PARA O ARQUIVO: "),
    STARTING_IMAGE("INICIANDO TRATATIVA POR IMAGEM PARA O ARQUIVO: "),
    STARTING_PDF("INICIANDO TRATATIVA POR PDF PARA O ARQUIVO: "),
    FINDING_ARCHIVE("BUSCANDO ARQUIVO..."),
    CLEANING_ARCHIVE("LIMPANDO ARQUIVOS..."),
    ARCHIVE_DONE_RENAME("QUANTIDADE DE ARQUIVOS PRONTOS PARA SEREM RENOMEADOS É DE: "),
    THREAD_FAIL("THREAD FALHOU"),
    TRY_RENAMED_AGAIN("TENTANDO RENOMEAR NOVAMENTE... "),
    ARCHIVE_FAIL_RENAME("QUANTIDADE DE ARQUIVOS COM ERROS É DE: "),
    ARCHIVE_NO_NAME("ARQUIVO NÃO POSSUI NOME PARA O ARQUIVO: "),
    ARCHIVE_NO_DATE("ARQUIVO NÃO POSSUI DATA - ARQUIVO: "),
    QUIT("ENCERRANDO...");

    private final String name;

    MessageLogEnum(String name) {
        this.name = name;
    }

    public static class Constants {
        public static final String RENAMED_SUCCESS = MessageLogEnum.RENAMED_SUCCESS.name;
        public static final String RENAMED_FAIL = MessageLogEnum.RENAMED_FAIL.name;
        public static final String INITIAL_AUTOMATIZED = MessageLogEnum.INITIAL_AUTOMATIZED.name;
        public static final String PREPARED_ARCHIVE = MessageLogEnum.PREPARED_ARCHIVE.name;
        public static final String RENAMED_ARCHIVE = MessageLogEnum.RENAMED_ARCHIVE.name;
        public static final String CONVERSION_FAIL = MessageLogEnum.CONVERSION_FAIL.name;
        public static final String PROGRAMMATIC_FILE = MessageLogEnum.PROGRAMMATIC_FILE.name;
        public static final String INFORMATION_FILE_METADATA = MessageLogEnum.INFORMATION_FILE_METADATA.name;
        public static final String STARTING_IMAGE = MessageLogEnum.STARTING_IMAGE.name;
        public static final String STARTING_PDF = MessageLogEnum.STARTING_PDF.name;
        public static final String FINDING_ARCHIVE = MessageLogEnum.FINDING_ARCHIVE.name;
        public static final String CLEANING_ARCHIVE = MessageLogEnum.CLEANING_ARCHIVE.name;
        public static final String ARCHIVE_DONE_RENAME = MessageLogEnum.ARCHIVE_DONE_RENAME.name;
        public static final String THREAD_FAIL = MessageLogEnum.THREAD_FAIL.name;
        public static final String TRY_RENAMED_AGAIN = MessageLogEnum.TRY_RENAMED_AGAIN.name;
        public static final String ARCHIVE_FAIL_RENAME = MessageLogEnum.ARCHIVE_FAIL_RENAME.name;
        public static final String ARCHIVE_NO_NAME = MessageLogEnum.ARCHIVE_NO_NAME.name;
        public static final String ARCHIVE_NO_DATE = MessageLogEnum.ARCHIVE_NO_DATE.name;
        public static final String QUIT = MessageLogEnum.QUIT.name;
    }
}
