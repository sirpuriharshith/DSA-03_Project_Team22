package texthack.dp;

import org.springframework.stereotype.Component;

/**
 * <h1>Biological & Sequence Alignment Algorithms</h1>
 * <p>
 * Implements <b>Needleman-Wunsch (Global Alignment)</b> and <b>Smith-Waterman (Local Alignment)</b>.
 * </p>
 */
@Component
public class SequenceAlignment {

    public static class AlignmentResult {
        public final int score;
        public final String alignedS1;
        public final String alignedS2;
        public final String alignmentType;

        public AlignmentResult(int score, String alignedS1, String alignedS2, String alignmentType) {
            this.score = score;
            this.alignedS1 = alignedS1;
            this.alignedS2 = alignedS2;
            this.alignmentType = alignmentType;
        }
    }

    /**
     * Needleman-Wunsch Global Sequence Alignment.
     * Recurrence: M(i, j) = max( M(i-1, j-1) + S(i, j), M(i-1, j) - gap, M(i, j-1) - gap )
     */
    public AlignmentResult needlemanWunsch(String s1, String s2, int matchScore, int mismatchPenalty, int gapPenalty) {
        int n = s1.length();
        int m = s2.length();
        int[][] score = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) score[i][0] = -i * gapPenalty;
        for (int j = 0; j <= m; j++) score[0][j] = -j * gapPenalty;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int match = score[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? matchScore : -mismatchPenalty);
                int delete = score[i - 1][j] - gapPenalty;
                int insert = score[i][j - 1] - gapPenalty;
                score[i][j] = Math.max(match, Math.max(delete, insert));
            }
        }

        // Traceback
        StringBuilder a1 = new StringBuilder();
        StringBuilder a2 = new StringBuilder();
        int i = n, j = m;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && score[i][j] == score[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? matchScore : -mismatchPenalty)) {
                a1.append(s1.charAt(i - 1));
                a2.append(s2.charAt(j - 1));
                i--;
                j--;
            } else if (i > 0 && score[i][j] == score[i - 1][j] - gapPenalty) {
                a1.append(s1.charAt(i - 1));
                a2.append('-');
                i--;
            } else {
                a1.append('-');
                a2.append(s2.charAt(j - 1));
                j--;
            }
        }

        return new AlignmentResult(score[n][m], a1.reverse().toString(), a2.reverse().toString(), "NEEDLEMAN_WUNSCH");
    }

    /**
     * Smith-Waterman Local Sequence Alignment.
     * Recurrence: H(i, j) = max(0, H(i-1, j-1) + S(i, j), H(i-1, j) - gap, H(i, j-1) - gap)
     */
    public AlignmentResult smithWaterman(String s1, String s2, int matchScore, int mismatchPenalty, int gapPenalty) {
        int n = s1.length();
        int m = s2.length();
        int[][] h = new int[n + 1][m + 1];

        int maxScore = 0;
        int maxI = 0, maxJ = 0;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int match = h[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? matchScore : -mismatchPenalty);
                int delete = h[i - 1][j] - gapPenalty;
                int insert = h[i][j - 1] - gapPenalty;
                h[i][j] = Math.max(0, Math.max(match, Math.max(delete, insert)));

                if (h[i][j] > maxScore) {
                    maxScore = h[i][j];
                    maxI = i;
                    maxJ = j;
                }
            }
        }

        // Traceback from maximum cell until score reaches 0
        StringBuilder a1 = new StringBuilder();
        StringBuilder a2 = new StringBuilder();
        int i = maxI, j = maxJ;

        while (i > 0 && j > 0 && h[i][j] > 0) {
            if (h[i][j] == h[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? matchScore : -mismatchPenalty)) {
                a1.append(s1.charAt(i - 1));
                a2.append(s2.charAt(j - 1));
                i--;
                j--;
            } else if (h[i][j] == h[i - 1][j] - gapPenalty) {
                a1.append(s1.charAt(i - 1));
                a2.append('-');
                i--;
            } else {
                a1.append('-');
                a2.append(s2.charAt(j - 1));
                j--;
            }
        }

        return new AlignmentResult(maxScore, a1.reverse().toString(), a2.reverse().toString(), "SMITH_WATERMAN");
    }
}
