package com.github.leosant.services.validator;

import com.github.leosant.model.enums.MonthEnum;
import org.apache.commons.lang3.StringUtils;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextValidator {
    private static final String[] TEXT_TO_EXTRACT = {
            "para",
            "favorec"
    };

    private static final String[] REGEX_OF_DATE = {
            "(\\d{2}/\\d{2}/\\d{4}) - (\\d{2}:\\d{2}:\\d{2})",
            "(\\d{2}\\s+[A-Z]{3}\\s+\\d{4}) - (\\d{2}:\\d{2}:\\d{2})",
            "(\\d{2}\\s+[A-Z]{3}\\s+\\d{4}) - (\\d{2}:\\d{2})"
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

        return Normalizer.normalize(Arrays.stream(textEscape.get(0).split(" "))
                        .filter(StringUtils::isNotEmpty)
                        .filter(text -> !StringUtils.containsIgnoreCase(text, "favorec"))
                        .collect(Collectors.joining()), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}", "")
                .replaceAll("\\|", "")
                .toUpperCase();
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
            text = replacedTextIncomplete(text);

            Matcher matcher = pattern.matcher(text);
            if (!matcher.find()
                    || Objects.nonNull(saveTextOfDateFound.get("data"))) {
                continue;
            }

            saveTextOfDateFound = matcherGroupDate(matcher);
            break;
        }

        return saveTextOfDateFound;
    }

    private static String replacedTextIncomplete(String text) {
        if (text.length() >= 22) {
            text = text.substring(0, 2)
                    .replaceAll("O", "0")
                    .replaceAll("S", "")
                    .replaceAll("B", "")
                    .concat(text.substring(2, 22));
        }
        return text;
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
        } catch (DateTimeParseException dateTimeParseException) {
            date = convertDate(date);
            localDate = LocalDate.parse(date, dateTimeFormatter);
        }
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd'D'MM'M'yyyy'Y'");

        return localDate.format(dateTimeFormatter);
    }

    private static String convertDate(String date) {
        String[] dateFormat = date.split(" ");

        StringBuilder text = new StringBuilder(dateFormat[0]);
        MonthEnum.getMonthValid(dateFormat[1])
                .ifPresent(monthNumber ->
                        text.append("/")
                                .append(monthNumber.getNumber())
                                .append("/")
                                .append(dateFormat[2]));

        return text.toString();
    }

    private static String formatTime(String time) {
        LocalTime localTime;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        try {
            localTime = LocalTime.parse(time, dateTimeFormatter);
        } catch (DateTimeParseException dateTimeParseException) {
            DateTimeFormatter dateTimeFormatterNoSeconds = DateTimeFormatter.ofPattern("HH:mm");
            localTime = LocalTime.parse(time, dateTimeFormatterNoSeconds);
        }
        dateTimeFormatter = DateTimeFormatter.ofPattern("HH'h'mm'm'ss's'");
        return localTime.format(dateTimeFormatter);
    }
}
