package texthack.dp;

import org.springframework.stereotype.Component;

/**
 * <h1>Wagner-Fischer Levenshtein Edit Distance</h1>
 * <p>
 * Computes the minimum number of single-character insertions, deletions, or substitutions
 * required to transform string s1 into string s2.
 * </p>
 * <h3>Dynamic Programming Recurrence:</h3>
 * <pre>
 * Base Cases:
 *   D(i, 0) = i  (cost of i deletions)
 *   D(0, j) = j  (cost of j insertions)
 *
 * For i > 0, j > 0:
 *   cost = (s1[i - 1] == s2[j - 1]) ? 0 : 1
 *   D(i, j) = min(
 *       D(i - 1, j) + 1,       // Deletion from s1
 *       D(i, j - 1) + 1,       // Insertion into s1
 *       D(i - 1, j - 1) + cost // Substitution
 *   )
 * </pre>
 * <ul>
 *   <li><b>Time Complexity:</b> O(n * m)</li>
 *   <li><b>Space Complexity:</b> O(min(n, m)) using two rolling row buffers</li>
 * </ul>
 */
@Component
public class LevenshteinDistance {

    /**
     * Computes the Levenshtein distance using O(min(n, m)) memory.
     */
    public int computeDistance(String s1, String s2) {
        if (s1 == null) s1 = "";
        if (s2 == null) s2 = "";

        int n = s1.length();
        int m = s2.length();

        if (n == 0) return m;
        if (m == 0) return n;

        // Optimize memory to O(min(n, m))
        if (n < m) {
            String temp = s1;
            s1 = s2;
            s2 = temp;
            n = s1.length();
            m = s2.length();
        }

        int[] prevRow = new int[m + 1];
        int[] currRow = new int[m + 1];

        // Base case: transforming empty prefix into s2 prefix of length j
        for (int j = 0; j <= m; j++) {
            prevRow[j] = j;
        }

        for (int i = 1; i <= n; i++) {
            currRow[0] = i; // Deleting i characters from s1

            for (int j = 1; j <= m; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                int deletion = prevRow[j] + 1;
                int insertion = currRow[j - 1] + 1;
                int substitution = prevRow[j - 1] + cost;

                currRow[j] = Math.min(deletion, Math.min(insertion, substitution));
            }

            // Swap buffers for next iteration
            System.arraycopy(currRow, 0, prevRow, 0, m + 1);
        }

        return prevRow[m];
    }
}
