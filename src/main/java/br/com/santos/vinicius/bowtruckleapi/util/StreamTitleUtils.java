package br.com.santos.vinicius.bowtruckleapi.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StreamTitleUtils {

    public static List<String> collectTittles(String titles) {
        if (titles == null || StringUtils.isAllBlank(titles)) {
            return new ArrayList<>();
        }

        List<String> titleList = new ArrayList<>();
        StringBuilder tempTitle = null;

        for (char titleChar : titles.toCharArray()) {
            if (titleChar == '[') {
                tempTitle = new StringBuilder();
                continue;
            }
            if (titleChar == ']') {
                String formattedTitle = replaceCurlyBrackets(tempTitle.toString());
                titleList.add(formattedTitle);
                continue;
            }

            tempTitle.append(titleChar);
        }

        return titleList;
    }

    public static String replaceCurlyBrackets(String str) {
        Map<String, String> keysToBeReplaced = new HashMap<>();
        keysToBeReplaced.put("{{", "[");
        keysToBeReplaced.put("}}", "]");
        return replaceFor(str, keysToBeReplaced);
    }

    public static String replaceBrackets(String str) {
        Map<String, String> keysToBeReplaced = new HashMap<>();
        keysToBeReplaced.put("[", "{{");
        keysToBeReplaced.put("]", "}}");
        return replaceFor(str, keysToBeReplaced);
    }

    private static String replaceFor(String str, Map<String, String> keysToBeReplaced) {
        if (keysToBeReplaced.isEmpty()) return str;

        final String[] stringToBeReplaced = {str};

        keysToBeReplaced.forEach((keyToBeReplaced, keyToReplace) -> stringToBeReplaced[0] = stringToBeReplaced[0].replace(keyToBeReplaced, keyToReplace));

        return stringToBeReplaced[0];
    }

}
