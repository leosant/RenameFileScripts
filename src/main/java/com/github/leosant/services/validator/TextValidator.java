package com.github.leosant.services.validator;

import org.apache.commons.lang3.StringUtils;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextValidator {

    private static final String[] TEXT_TO_EXTRACT = {
            "para",
            "favorecido",
            "favoreci"
    };

    private static final String[] REGEX_OF_DATE = {
            "(\\d{2}/\\d{2}/\\d{4}) - (\\d{2}:\\d{2}:\\d{2})",
            "(\\d{2}\\s+[A-Z]{3}\\s+\\d{4}) - (\\d{2}:\\d{2}:\\d{2})"
    };

    private TextValidator() {
    }

    public static LinkedHashMap<String, String> getExtractText(List<String> textList) {
        LinkedHashMap<String, String> saveText = new LinkedHashMap<>();
        List<Pattern> patterns = patternsTypeOfDate();

        for (String textOfTo : TEXT_TO_EXTRACT) {
            saveText.putAll(matcherText(textList, textOfTo, patterns));
        }
        return saveText;
    }

    private static Map<String, String> matcherText(List<String> textList, String textOfTo, List<Pattern> patterns) {
        LinkedHashMap<String, String> saveText = new LinkedHashMap<>();

        textList.forEach(text -> {
            Map<String, String> saveTextDate = matcherOfDate(text, patterns);
            if (!saveTextDate.isEmpty()) {
                saveText.putAll(saveTextDate);
                return;
            }

            if (StringUtils.containsIgnoreCase(text, textOfTo)) {
                List<String> textEscape = Arrays.stream(text.split("\\n"))
                        .filter(StringUtils::isNotEmpty)
                        .toList();

                if (textEscape.isEmpty()) {
                    saveText.put(textOfTo, "DESCONHECIDO");
                    return;
                }

                saveText.put(textOfTo, getTextWithRecipient(textEscape)
                );
            }
        });

        return saveText;
    }

    private static String getTextWithRecipient(List<String> textEscape) {
        if (textEscape.size() > 1) {
            return textEscape.get(1).replaceAll(" ", "");
        }

        return Arrays.stream(textEscape.get(0).split(" "))
                .filter(StringUtils::isNotEmpty)
                .filter(text -> !StringUtils.containsIgnoreCase(text, "favoreci"))
                .collect(Collectors.joining());
    }

    private static List<Pattern> patternsTypeOfDate() {
        List<Pattern> patterns = new ArrayList<>();

        for (String regex : REGEX_OF_DATE) {
            Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
            patterns.add(pattern);
        }

        return patterns;
    }

    private static Map<String, String> matcherOfDate(String text, List<Pattern> patterns) {
        return Arrays.stream(text.split("\\n"))
                .map(textCurt -> getDateByText(textCurt, patterns))
                .toList()
                .stream()
                .filter(dateMap -> !dateMap.isEmpty())
                .findFirst()
                .orElse(new LinkedHashMap<>());
    }

    private static Map<String, String> getDateByText(String text, List<Pattern> patterns) {
        LinkedHashMap<String, String> saveTextOfDateFound = new LinkedHashMap<>();
        for (Pattern pattern : patterns) {
            if (text.length() == 22) {
                text = text.substring(0,2)
                        .replaceAll("O", "0")
                        .concat(text.substring(2, 22));
            }
            Matcher matcher = pattern.matcher(text);
            if (!matcher.find()) {
                continue;
            }

            saveTextOfDateFound = matcherGroupDate(matcher);
            break;
        }

        return saveTextOfDateFound;
    }

    private static LinkedHashMap<String, String> matcherGroupDate(Matcher matcher) {
        LinkedHashMap<String, String> saveTextOfDateFound = new LinkedHashMap<>();

        saveTextOfDateFound.put("data", formatDate(matcher.group(1)));
        saveTextOfDateFound.put("horario", formatTime(matcher.group(2)));

        return saveTextOfDateFound;
    }

    private static String formatDate(String date) {
        LocalDate localDate;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            localDate = LocalDate.parse(date, dateTimeFormatter);
        } catch (DateTimeException dateTimeException) {
            date = convertDate(date);
            localDate = LocalDate.parse(date, dateTimeFormatter);
        }
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd'D'MM'M'yyyy'Y'");

        return localDate.format(dateTimeFormatter);
    }

    private static String convertDate(String date) {
        String[] dateFormat = date.split(" ");
        Map<String, String> months = new HashMap<>();
        months.put("JAN", "01");
        months.put("FEV", "02");
        months.put("MAR", "03");
        months.put("ABR", "04");
        months.put("MAI", "05");
        months.put("JUN", "06");
        months.put("JUL", "07");
        months.put("AGO", "08");
        months.put("SET", "09");
        months.put("OUT", "10");
        months.put("NOV", "11");
        months.put("DEZ", "12");

        StringBuilder text = new StringBuilder(dateFormat[0]);
        for (String monthKey : months.keySet()) {
            if (monthKey.equalsIgnoreCase(dateFormat[1])) {
                text.append("/")
                        .append(months.get(dateFormat[1]))
                        .append("/")
                        .append(dateFormat[2]);
                break;
            }
        }

        return text.toString();
    }

    private static String formatTime(String time) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime localTime = LocalTime.parse(time, dateTimeFormatter);
        dateTimeFormatter = DateTimeFormatter.ofPattern("HH'h'mm'm'ss's'");
        return localTime.format(dateTimeFormatter);
    }
}
