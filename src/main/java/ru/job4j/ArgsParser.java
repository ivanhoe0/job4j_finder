package ru.job4j;

import java.util.HashMap;
import java.util.Map;

public class ArgsParser {
    private Map<String, String> values = new HashMap<>();

    public String get(String key) {
        if (!values.containsKey(key)) {
            throw new IllegalArgumentException(String.format("This key: '%s' is missing", key));
        }
         return values.get(key);
    }

    private void parse(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Arguments have not been passed to program. Please specify arguments");
        }
        if (args.length > 4) {
            throw new IllegalArgumentException("There is more arguments then need to be. Please specify four arguments: -d, -n, -t, -o");
        }
        for (var s : args) {
            if (!s.startsWith("-")) {
                throw new IllegalArgumentException(String.format("Error: This argument '%s' does not start with a '-' character", s));
            }
            if (!s.contains("=")) {
                throw new IllegalArgumentException(String.format("Error: This argument '%s' does not contain an equal sign", s));
            }
            String[] str = s.substring(1).split("=", 2);
            if (str[0].isEmpty()) {
                throw new IllegalArgumentException(String.format("Error: This argument '%s' does not contain a key", s));
            }
            if (str[1].isEmpty()) {
                throw new IllegalArgumentException(String.format("Error: This argument '%s' does not contain a value", s));
            }
            values.putIfAbsent(str[0], str[1]);
        }
        if (!values.containsKey("d")) {
            throw new IllegalArgumentException("Error: There is no argument 'd'. You should specify directory where program start search");
        }
        if (!values.containsKey("n")) {
            throw new IllegalArgumentException("Error: There is no argument 'n'. You should specify file name to search");
        }
        if (!values.containsKey("t")) {
            throw new IllegalArgumentException("Error: There is no argument 't'. You should specify type of search");
        }
        if (!values.containsKey("o")) {
            throw new IllegalArgumentException("Error: There is no argument 'o'. You should specify output file for search results");
        }
    }

    public static ArgsParser of(String[] args) {
        ArgsParser result = new ArgsParser();
        result.parse(args);
        return result;
    }
}
