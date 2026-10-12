package texthack.strings;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Knuth-Morris-Pratt (KMP) Algorithm</h1>
 * <p>
 * Efficient string matching algorithm that eliminates backtracking in the input text by
 * preprocessing the pattern into a Longest Prefix Suffix (LPS) array.
 * </p>
 * <ul>
 *   <li><b>Preprocessing Time:</b> O(m) to build LPS array</li>
 *   <li><b>Matching Time:</b> O(n) as text pointer i strictly advances</li>
 *   <li><b>Overall Time Complexity:</b> O(n + m)</li>
 *   <li><b>Space Complexity:</b> O(m) for the LPS integer array</li>
 * </ul>
 */
@Component
public class KmpMatcher implements StringMatcher {

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

        // 1. Preprocess LPS array
        int[] lps = computeLpsArray(pattern);

        // 2. Pattern Matching traversal
        int i = 0; // index for text
        int j = 0; // index for pattern

        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            }

            if (j == m) {
                // Match found at start index (i - j)
                matches.add(i - j);
                j = lps[j - 1];
            } else if (i < n && text.charAt(i) != pattern.charAt(j)) {
                if (j != 0) {
                    j = lps[j - 1]; // Shift pattern using LPS
                } else {
                    i++;
                }
            }
        }

        long duration = System.nanoTime() - start;
        return new MatchResult(getAlgorithmName(), pattern, matches, duration);
    }

    /**
     * Constructs the LPS (Longest Proper Prefix which is also Suffix) array.
     * lps[i] stores the length of the maximum proper prefix of pattern[0..i]
     * that is also a suffix of pattern[0..i].
     */
    public int[] computeLpsArray(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0; // length of previous longest prefix suffix
        int i = 1;

        lps[0] = 0; // Base case: single character has no proper prefix

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    @Override
    public String getAlgorithmName() {
        return "KMP";
    }
}
