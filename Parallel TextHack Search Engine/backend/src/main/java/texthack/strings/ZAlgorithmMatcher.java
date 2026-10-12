package texthack.strings;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Z-Algorithm String Matching</h1>
 * <p>
 * Finds occurrences in linear time by calculating the Z-array for the concatenated string
 * <code>P + '\u0000' + T</code>. Maintains a [L, R] segment (Z-box) to avoid repeated comparisons.
 * </p>
 * <ul>
 *   <li><b>Time Complexity:</b> O(n + m) linear time</li>
 *   <li><b>Space Complexity:</b> O(n + m) for the Z-array</li>
 * </ul>
 */
@Component
public class ZAlgorithmMatcher implements StringMatcher {

    private static final char DELIMITER = '\u0000'; // Unique non-text delimiter

    @Override
    public MatchResult search(String text, String pattern) {
        long start = System.nanoTime();
        List<Integer> matches = new ArrayList<>();

        if (text == null || pattern == null) {
            return new MatchResult(getAlgorithmName(), pattern, matches, System.nanoTime() - start);
        }

        int n = text.length();
        int m = pattern.length();

        if (m == 0 || n < m) {
            return new MatchResult(getAlgorithmName(), pattern, matches, System.nanoTime() - start);
        }

        // Construct concatenated string: P + DELIMITER + T
        String concat = pattern + DELIMITER + text;
        int totalLen = concat.length();

        int[] z = computeZArray(concat);

        // Search positions: if Z[i] == m, occurrence found in text
        for (int i = m + 1; i < totalLen; i++) {
            if (z[i] == m) {
                matches.add(i - (m + 1));
            }
        }

        long duration = System.nanoTime() - start;
        return new MatchResult(getAlgorithmName(), pattern, matches, duration);
    }

    /**
     * Computes the Z-array for string S in O(length) time.
     * Z[i] is the length of the longest common prefix between S and S[i..length-1].
     */
    public int[] computeZArray(String s) {
        int len = s.length();
        int[] z = new int[len];

        int l = 0;
        int r = 0;

        for (int i = 1; i < len; i++) {
            if (i > r) {
                // i is outside current Z-box: compare naively
                l = r = i;
                while (r < len && s.charAt(r - l) == s.charAt(r)) {
                    r++;
                }
                z[i] = r - l;
                r--;
            } else {
                // i is inside current Z-box [l, r]
                int k = i - l; // corresponding prefix index

                // If value does not stretch beyond right bound of box
                if (z[k] < r - i + 1) {
                    z[i] = z[k];
                } else {
                    // Potential extension beyond r
                    l = i;
                    while (r < len && s.charAt(r - l) == s.charAt(r)) {
                        r++;
                    }
                    z[i] = r - l;
                    r--;
                }
            }
        }
        return z;
    }

    @Override
    public String getAlgorithmName() {
        return "Z_ALGORITHM";
    }
}
