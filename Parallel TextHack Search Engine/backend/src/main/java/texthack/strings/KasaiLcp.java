package texthack.strings;

import org.springframework.stereotype.Component;

/**
 * <h1>Kasai's Algorithm for Longest Common Prefix (LCP) Array</h1>
 * <p>
 * Computes the LCP array from a string and its Suffix Array in strictly <b>O(n) linear time</b>.
 * </p>
 * <h3>Key Insight:</h3>
 * <p>
 * If suffix <code>i</code> has LCP <code>h</code> with its previous suffix in the Suffix Array,
 * then suffix <code>i + 1</code> has an LCP of at least <code>h - 1</code> with its previous suffix.
 * Thus, <code>h</code> can be decremented at most <code>n</code> times, bounding the total
 * character comparisons to <code>2n = O(n)</code>.
 * </p>
 */
@Component
public class KasaiLcp {

    /**
     * Constructs the LCP array in O(n) time.
     *
     * @param s  input string
     * @param sa suffix array of string s
     * @return lcp array where lcp[i] is LCP of suffix sa[i] and suffix sa[i-1] (lcp[0] = 0)
     */
    public int[] buildLcpArray(String s, int[] sa) {
        if (s == null || sa == null || s.isEmpty() || sa.length == 0) {
            return new int[0];
        }

        int n = s.length();
        int[] lcp = new int[n];
        int[] rank = new int[n]; // Inverse suffix array: rank[sa[i]] = i

        for (int i = 0; i < n; i++) {
            rank[sa[i]] = i;
        }

        int h = 0; // Current LCP length

        for (int i = 0; i < n; i++) {
            if (rank[i] > 0) {
                int prevSuffixIndex = sa[rank[i] - 1];

                // Extend match while characters are identical
                while (i + h < n && prevSuffixIndex + h < n && s.charAt(i + h) == s.charAt(prevSuffixIndex + h)) {
                    h++;
                }

                lcp[rank[i]] = h;

                // Decrease h by at most 1 for next suffix i+1
                if (h > 0) {
                    h--;
                }
            } else {
                lcp[0] = 0;
            }
        }

        return lcp;
    }
}
