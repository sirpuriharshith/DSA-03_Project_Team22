package texthack.strings;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <h1>Suffix Array Construction & Search</h1>
 * <p>
 * Implements prefix-doubling suffix sorting in <code>O(n log² n)</code> time.
 * Supports binary search pattern matching in <code>O(m log n)</code> time.
 * </p>
 * <h3>SA-IS (Suffix Array Induced Sorting) Theoretical Overview:</h3>
 * <p>
 * While prefix-doubling runs in O(n log² n), the SA-IS algorithm builds the suffix array in
 * optimal <b>O(n) linear time</b>. It classifies suffixes as S-type (strictly smaller than suffix i+1)
 * or L-type (strictly greater). Suffixes that are S-type while preceding an L-type are LMS
 * (Leftmost S-type). SA-IS sorts LMS suffixes recursively using a reduced alphabet, then
 * "induces" the sorted order of all L-type and S-type suffixes using bucket pointers.
 * </p>
 */
@Component
public class SuffixArray {

    /**
     * Suffix item representation with rank pairs for prefix-doubling sort.
     */
    private static class SuffixRank implements Comparable<SuffixRank> {
        int index;
        int rank0;
        int rank1;

        @Override
        public int compareTo(SuffixRank o) {
            if (this.rank0 != o.rank0) {
                return Integer.compare(this.rank0, o.rank0);
            }
            return Integer.compare(this.rank1, o.rank1);
        }
    }

    /**
     * Constructs the suffix array for string S using O(n log² n) prefix doubling.
     *
     * @param s input string
     * @return array of starting indices of sorted suffixes
     */
    public int[] buildSuffixArray(String s) {
        if (s == null || s.isEmpty()) {
            return new int[0];
        }

        int n = s.length();
        SuffixRank[] suffixes = new SuffixRank[n];

        // Initial ranks from single characters
        for (int i = 0; i < n; i++) {
            suffixes[i] = new SuffixRank();
            suffixes[i].index = i;
            suffixes[i].rank0 = (int) s.charAt(i);
            suffixes[i].rank1 = (i + 1 < n) ? (int) s.charAt(i + 1) : -1;
        }

        Arrays.sort(suffixes);

        int[] ind = new int[n]; // Map original suffix index to position in suffixes[]

        // Prefix doubling: compare prefixes of length 2*k
        for (int k = 4; k < 2 * n; k *= 2) {
            int rank = 0;
            int prevRank0 = suffixes[0].rank0;
            suffixes[0].rank0 = rank;
            ind[suffixes[0].index] = 0;

            for (int i = 1; i < n; i++) {
                if (suffixes[i].rank0 == prevRank0 && suffixes[i].rank1 == suffixes[i - 1].rank1) {
                    prevRank0 = suffixes[i].rank0;
                    suffixes[i].rank0 = rank;
                } else {
                    prevRank0 = suffixes[i].rank0;
                    suffixes[i].rank0 = ++rank;
                }
                ind[suffixes[i].index] = i;
            }

            for (int i = 0; i < n; i++) {
                int nextIndex = suffixes[i].index + k / 2;
                suffixes[i].rank1 = (nextIndex < n) ? suffixes[ind[nextIndex]].rank0 : -1;
            }

            Arrays.sort(suffixes);
        }

        int[] sa = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = suffixes[i].index;
        }
        return sa;
    }

    /**
     * Searches for all occurrences of pattern in text using binary search over the suffix array.
     * Complexity: O(m log n)
     */
    public MatchResult search(String text, String pattern, int[] sa) {
        long start = System.nanoTime();
        List<Integer> matches = new ArrayList<>();

        if (text == null || pattern == null || sa == null || sa.length == 0 || pattern.isEmpty()) {
            return new MatchResult("SUFFIX_ARRAY", pattern, matches, System.nanoTime() - start);
        }

        int n = text.length();
        int m = pattern.length();

        // Binary search lower bound
        int low = 0, high = n - 1;
        int lowerBound = -1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = comparePrefix(text, sa[mid], pattern);
            if (cmp >= 0) {
                if (cmp == 0) lowerBound = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        if (lowerBound == -1) {
            return new MatchResult("SUFFIX_ARRAY", pattern, matches, System.nanoTime() - start);
        }

        // Binary search upper bound
        low = lowerBound;
        high = n - 1;
        int upperBound = lowerBound;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = comparePrefix(text, sa[mid], pattern);
            if (cmp == 0) {
                upperBound = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        for (int i = lowerBound; i <= upperBound; i++) {
            matches.add(sa[i]);
        }

        return new MatchResult("SUFFIX_ARRAY", pattern, matches, System.nanoTime() - start);
    }

    /**
     * Lexicographical comparison between text starting at offset and pattern up to pattern length.
     */
    private int comparePrefix(String text, int textOffset, String pattern) {
        int n = text.length();
        int m = pattern.length();
        int len = Math.min(n - textOffset, m);

        for (int i = 0; i < len; i++) {
            char tc = text.charAt(textOffset + i);
            char pc = pattern.charAt(i);
            if (tc != pc) {
                return Character.compare(tc, pc);
            }
        }

        if (n - textOffset < m) {
            return -1; // Suffix ran out of characters before matching full pattern
        }
        return 0; // Prefix of suffix matches pattern exactly
    }
}
