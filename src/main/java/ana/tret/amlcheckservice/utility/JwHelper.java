package ana.tret.amlcheckservice.utility;

import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;

import java.util.List;

import static ana.tret.amlcheckservice.utility.NormalizeHelper.splitByWhitespace;

@UtilityClass
public class JwHelper {

    private static final JaroWinklerSimilarity jwSimilarity = new JaroWinklerSimilarity();

    public static double calculateJaroWinklerScore(String first, String second) {
        List<String> left = splitByWhitespace(first);
        List<String> right = splitByWhitespace(second);
        return calculateJaroWinklerScore(left, right);
    }

    private static double calculateJaroWinklerScore(List<String> left, List<String> right) {
        if (CollectionUtils.isEmpty(left) || CollectionUtils.isEmpty(right)) return 0.0;

        boolean[] used = new boolean[right.size()]; // initial is [false, false, false, ... ]
        double sum = 0.0;
        int matches = 0;

        // for each element from left take the best unused element from right
        for (String leftElement : left) {
            double best = 0.0;
            int bestIdx = -1;
            for (int i = 0; i < right.size(); i++) {
                if (used[i]) continue;
                String rightElem = right.get(i);
                double s = calculateJwSimilarityScore(leftElement, rightElem);
                if (s > best) {
                    best = s;
                    bestIdx = i;
                }
            }
            if (bestIdx >= 0) {
                used[bestIdx] = true;
                sum += best;
                matches++;
            }
        }

        // if matches exist then calculate average score based on the longest array
        double base = matches == 0 ? 0.0 : sum / Math.max(left.size(), right.size());

        // calculate the bonus if at least one strong pair exists
        double strong = 0.0;
        for (String l : left) {
            for (String r : right) {
                strong = Math.max(strong, calculateJwSimilarityScore(l, r));
            }
        }

        double score = base * 0.85 + strong * 0.15;
        return Math.max(0.0, Math.min(1.0, score));
    }

    private static double calculateJwSimilarityScore(String left, String right) {
        Double sim = jwSimilarity.apply(left, right);
        return sim == null ? 0.0 : sim;
    }

}
