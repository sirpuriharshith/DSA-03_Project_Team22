package texthack.strings;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Naive String Matching Algorithm</h1>
 * <p>
 * Slides the pattern across the text character-by-character and tests for a match at each position.
 * </p>
 * <ul>
 *   <li><b>Time Complexity (Worst Case):</b> O(n * m) — e.g. text="AAAAAA", pattern="AAAB"</li>
 *   <li><b>Time Complexity (Best Case):</b> O(n)</li>
 *   <li><b>Space Complexity:</b> O(1) auxiliary</li>
 * </ul>
 */
@Component
public class NaiveStringMatcher implements StringMatcher {

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

        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            while (j < m && text.charAt(i + j) == pattern.charAt(j)) {
                j++;
            }
            if (j == m) {
                matches.add(i);
            }
        }

        long duration = System.nanoTime() - start;
        return new MatchResult(getAlgorithmName(), pattern, matches, duration);
    }

    @Override
    public String getAlgorithmName() {
        return "NAIVE";
    }
}
