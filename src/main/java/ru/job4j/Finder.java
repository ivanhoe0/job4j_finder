package ru.job4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Finder {
    public static void main(String[] args) {
        var parser = ArgsParser.of(args);
        Predicate<Path> condition;
        if ("name".equals(parser.get("t"))) {
            condition = p -> p.toFile().getName().equals(parser.get("n"));
        } else {
            String mask = parser.get("n");
            if ("mask".equals(parser.get("t"))) {
                mask = mask.replaceAll("\\*", "[\\\\wА-Яа-я]*");
                mask = mask.replaceAll("\\?", "[\\\\wА-Яа-я]?");
            }
            Pattern pattern = Pattern.compile(mask);
            condition = (f) -> {
                Matcher matcher = pattern.matcher(f.toFile().getName());
                return matcher.find();
            };
        }
        SearchFile search = new SearchFile(condition);
        try {
            Files.walkFileTree(Path.of(parser.get("d")), search);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Path> paths = search.getPaths();
        try (BufferedWriter bfw = new BufferedWriter(new FileWriter(parser.get("o")))) {
            for (var p : paths) {
                bfw.write(p.toFile().getAbsolutePath());
                bfw.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
