package texthack.dp;

import org.springframework.stereotype.Component;

/**
 * <h1>Damerau-Levenshtein Edit Distance (Optimal String Alignment)</h1>
 * <p>
 * Counts insertions, deletions, substitutions, and <b>adjacent character transpositions</b>.
 * </p>
 * <h3>Dynamic Programming Recurrence:</h3>
 * <pre>
 * D(i, j) = min(
 *     D(i - 1, j) + 1,       // Deletion
 *     D(i, j - 1) + 1,       // Insertion
 *     D(i - 1, j - 1) + cost // Substitution
 * )
 * If i > 1 and j > 1 and s1[i - 1] == s2[j - 2] and s1[i - 2] == s2[j - 1]:
 *     D(i, j) = min(D(i, j), D(i - 2, j - 2) + 1) // Adjacent Transposition
 * </pre>
 * <ul>
 *   <li><b>Time Complexity:</b> O(n * m)</li>
 *   <li><b>Space Complexity:</b> O(n * m) matrix</li>
 * </ul>
 */
@Component
public class DamerauLevenshteinDistance {

    public int computeDistance(String s1, String s2) {
        if (s1 == null) s1 = "";
        if (s2 == null) s2 = "";

        int n = s1.length();
        int m = s2.length();

        if (n == 0) return m;
        if (m == 0) return n;

        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;

                int deletion = dp[i - 1][j] + 1;
                int insertion = dp[i][j - 1] + 1;
                int substitution = dp[i - 1][j - 1] + cost;

                dp[i][j] = Math.min(deletion, Math.min(insertion, substitution));

                // Adjacent character transposition
                if (i > 1 && j > 1 &&
                        s1.charAt(i - 1) == s2.charAt(j - 2) &&
                        s1.charAt(i - 2) == s2.charAt(j - 1)) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 2][j - 2] + 1);
                }
            }
        }

        return dp[n][m];
    }
}
