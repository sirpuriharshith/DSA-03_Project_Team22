package texthack.dp;

import org.springframework.stereotype.Component;

/**
 * <h1>Interval Dynamic Programming</h1>
 * <p>
 * Demonstrates <b>Matrix Chain Multiplication</b> and <b>Optimal Binary Search Tree (OBST)</b>.
 * </p>
 */
@Component
public class IntervalDp {

    /**
     * Matrix Chain Multiplication.
     * Recurrence: m[i, j] = min_{i <= k < j} (m[i, k] + m[k+1, j] + p[i-1]*p[k]*p[j])
     * Complexity: O(n^3) time, O(n^2) space.
     *
     * @param p dimensions array where matrix i has dimension p[i-1] x p[i]
     */
    public int matrixChainOrder(int[] p) {
        int n = p.length - 1;
        int[][] m = new int[n + 1][n + 1];

        for (int len = 2; len <= n; len++) {
            for (int i = 1; i <= n - len + 1; i++) {
                int j = i + len - 1;
                m[i][j] = Integer.MAX_VALUE;
                for (int k = i; k < j; k++) {
                    int q = m[i][k] + m[k + 1][j] + p[i - 1] * p[k] * p[j];
                    if (q < m[i][j]) {
                        m[i][j] = q;
                    }
                }
            }
        }
        return m[1][n];
    }

    /**
     * Optimal Binary Search Tree (OBST).
     * Recurrence: e[i, j] = min_{i <= r <= j} (e[i, r-1] + e[r+1, j] + w(i, j))
     * Complexity: O(n^3) time, O(n^2) space.
     */
    public double optimalBst(double[] p, double[] q, int n) {
        double[][] e = new double[n + 2][n + 1];
        double[][] w = new double[n + 2][n + 1];

        for (int i = 1; i <= n + 1; i++) {
            e[i][i - 1] = q[i - 1];
            w[i][i - 1] = q[i - 1];
        }

        for (int l = 1; l <= n; l++) {
            for (int i = 1; i <= n - l + 1; i++) {
                int j = i + l - 1;
                e[i][j] = Double.MAX_VALUE;
                w[i][j] = w[i][j - 1] + p[j] + q[j];
                for (int r = i; r <= j; r++) {
                    double t = e[i][r - 1] + e[r + 1][j] + w[i][j];
                    if (t < e[i][j]) {
                        e[i][j] = t;
                    }
                }
            }
        }
        return e[1][n];
    }
}
