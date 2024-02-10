package com.github.leosant.services.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class TextValidator {

    private static final String[] textToExtract = {
            "para",
            "data e hora"
    };

    private TextValidator() {
    }

    public static LinkedHashMap<String, String> getExtractText(String[] textDocument) {
        LinkedHashMap<String, String> textOrganizzed = new LinkedHashMap<>();

        for (String text : textToExtract) {
            Arrays.stream(textDocument).forEach(value -> {
                if (StringUtils.containsIgnoreCase(value, text)) {
                    List<String> valuesEscape = Arrays.stream(value.split("\\n")).toList();

                    if (valuesEscape.isEmpty()
                            || isTextNotChoose(text, valuesEscape)) {
                        return;
                    }

                    textOrganizzed.put(text.replaceAll(" ", ""),
                            valuesEscape.size() > 1
                                    ? valuesEscape.get(1).replaceAll(" ", "")
                                    : "DESCONHECIDO"
                    );
                }
            });
        }
        return textOrganizzed;
    }

    private static boolean isTextNotChoose(String text, List<String> valuesEscape) {
        if (StringUtils.equalsIgnoreCase(textToExtract[1], text)) {
            return !StringUtils.equalsIgnoreCase(valuesEscape.get(0).replaceAll(" ", "").substring(0, 9),
                    text.replaceAll(" ", ""));
        }

        return !StringUtils.equalsIgnoreCase(valuesEscape.get(0), text);
    }
}
