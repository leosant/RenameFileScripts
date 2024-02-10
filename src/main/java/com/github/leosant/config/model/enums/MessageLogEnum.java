package com.github.leosant.config.model.enums;

public enum MessageLogEnum {

    RENAMED_SUCCESS("ARQUIVO RENOMEADO COM SUCESSO: "),
    RENAMED_FAIL("NAO FOI POSSIVEL RENOMEAR O ARQUIVO: "),
    INITIAL_AUTOMATIZED("INICIANDO AUTOMATIZAÇÃO DE RENOME DE COMPROVANTES..."),
    PREPARED_ARCHIVE("PREPARANDO ARQUIVO..."),
    RENAMEDING_ARCHIVE("ARQUIVO ESTÁ SENDO NOMEADO PARA -> "),
    CONVERSION_FAIL("CONVERSÃO DO PDF FALHOU: "),
    PROGRAMATIC_FILE("ARQUIVO DE AGENDAMENTO. SUSPENDENDO ALTERAÇÃO..."),
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
        public static final String RENAMEDING_ARCHIVE = MessageLogEnum.RENAMEDING_ARCHIVE.name;
        public static final String CONVERSION_FAIL = MessageLogEnum.CONVERSION_FAIL.name;
        public static final String PROGRAMATIC_FILE = MessageLogEnum.PROGRAMATIC_FILE.name;
        public static final String QUIT = MessageLogEnum.QUIT.name;
    }
}
