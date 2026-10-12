package texthack.strings;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Rabin-Karp String Matching Algorithm</h1>
 * <p>
 * Uses double polynomial rolling hash with modular arithmetic to locate patterns.
 * When both hash signatures match, an exact character verification is executed to prevent
 * spurious false-positive collision matches.
 * </p>
 * <ul>
 *   <li><b>Average Time Complexity:</b> O(n + m)</li>
 *   <li><b>Worst Case Time:</b> O(n * m) (drastically minimized by double hashing)</li>
 *   <li><b>Space Complexity:</b> O(1) auxiliary</li>
 *   <li><b>Double Hashing Primes:</b> M1 = 1,000,000,007, M2 = 1,000,000,009, Base = 257</li>
 * </ul>
 */
@Component
public class RabinKarpMatcher implements StringMatcher {

    private static final long MOD1 = 1_000_000_007L;
    private static final long MOD2 = 1_000_000_009L;
    private static final long BASE = 257L;

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

        // Precompute Base^(m - 1) % MOD
        long power1 = 1;
        long power2 = 1;
        for (int i = 0; i < m - 1; i++) {
            power1 = (power1 * BASE) % MOD1;
            power2 = (power2 * BASE) % MOD2;
        }

        // Compute initial hash values for pattern and first window of text
        long patHash1 = 0, patHash2 = 0;
        long textHash1 = 0, textHash2 = 0;

        for (int i = 0; i < m; i++) {
            char pChar = pattern.charAt(i);
            char tChar = text.charAt(i);

            patHash1 = (patHash1 * BASE + pChar) % MOD1;
            patHash2 = (patHash2 * BASE + pChar) % MOD2;

            textHash1 = (textHash1 * BASE + tChar) % MOD1;
            textHash2 = (textHash2 * BASE + tChar) % MOD2;
        }

        // Slide window across text
        for (int i = 0; i <= n - m; i++) {
            // Check hash match on both moduli
            if (patHash1 == textHash1 && patHash2 == textHash2) {
                // Collision verification: verify actual characters
                if (verifyMatch(text, pattern, i, m)) {
                    matches.add(i);
                }
            }

            // Compute hash for next window: remove leading char, add trailing char
            if (i < n - m) {
                long leadingChar = text.charAt(i);
                long nextChar = text.charAt(i + m);

                // Modulo 1 update
                textHash1 = (textHash1 - (leadingChar * power1) % MOD1 + MOD1) % MOD1;
                textHash1 = (textHash1 * BASE + nextChar) % MOD1;

                // Modulo 2 update
                textHash2 = (textHash2 - (leadingChar * power2) % MOD2 + MOD2) % MOD2;
                textHash2 = (textHash2 * BASE + nextChar) % MOD2;
            }
        }

        long duration = System.nanoTime() - start;
        return new MatchResult(getAlgorithmName(), pattern, matches, duration);
    }

    /**
     * Exact character comparison when hash signatures match.
     */
    private boolean verifyMatch(String text, String pattern, int offset, int m) {
        for (int j = 0; j < m; j++) {
            if (text.charAt(offset + j) != pattern.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getAlgorithmName() {
        return "RABIN_KARP";
    }
}
