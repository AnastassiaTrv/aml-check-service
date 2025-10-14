package ana.tret.amlcheckservice.service;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.text.Normalizer.Form.NFD;
import static java.text.Normalizer.normalize;
import static java.util.regex.Pattern.compile;

@Component
public class NormalizeHelper {
    private static final String WHITE_SPACE = "\\s+";
    private static final Pattern DIACRITIC_MARKS = compile("\\p{M}+");
    private static final Pattern NON_ALPHANUMERIC_UNICODE = compile("[^\\p{L}\\p{Nd}]+"); // unicode letters and decimal numbers only
    private static final Set<String> NOISE_WORDS = Set.of("the", "to", "an", "a", "mrs", "mr", "and");


    public static String toNormalized(String input) {
        if (input == null) return null;

        String base = DIACRITIC_MARKS.matcher(normalize(input.trim().toLowerCase(), NFD)).replaceAll("");
        String norm = NON_ALPHANUMERIC_UNICODE.matcher(base).replaceAll(" ").trim();

        return Arrays.stream(norm.split(WHITE_SPACE))
                .filter(sub -> !sub.isBlank())
                .filter(sub -> !NOISE_WORDS.contains(sub))
                .collect(Collectors.joining(" "));
    }
}
