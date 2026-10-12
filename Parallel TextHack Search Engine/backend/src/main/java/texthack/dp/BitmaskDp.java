package texthack.dp;

import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * <h1>Bitmask Dynamic Programming</h1>
 * <p>
 * Solves the <b>Traveling Salesperson Problem (TSP)</b> and <b>Hamiltonian Path</b>
 * using Held-Karp dynamic programming with bitmasks.
 * </p>
 * <ul>
 *   <li><b>Time Complexity:</b> O(n² * 2ⁿ) vs Brute-force O(n!)</li>
 *   <li><b>Space Complexity:</b> O(n * 2ⁿ)</li>
 * </ul>
 */
@Component
public class BitmaskDp {

    private static final int INF = 1_000_000_000;

    /**
     * Solves TSP returning minimum tour cost visiting every node and returning to start (node 0).
     */
    public int tsp(int[][] dist) {
        int n = dist.length;
        int numStates = 1 << n;
        int[][] dp = new int[numStates][n];

        for (int[] row : dp) {
            Arrays.fill(row, INF);
        }

        // Base case: start at city 0 with mask containing only city 0
        dp[1][0] = 0;

        for (int mask = 1; mask < numStates; mask++) {
            for (int u = 0; u < n; u++) {
                if ((mask & (1 << u)) == 0 || dp[mask][u] >= INF) {
                    continue;
                }

                for (int v = 0; v < n; v++) {
                    if ((mask & (1 << v)) == 0 && dist[u][v] < INF) {
                        int nextMask = mask | (1 << v);
                        dp[nextMask][v] = Math.min(dp[nextMask][v], dp[mask][u] + dist[u][v]);
                    }
                }
            }
        }

        // Return to starting node 0
        int minCost = INF;
        int fullMask = (1 << n) - 1;
        for (int i = 1; i < n; i++) {
            if (dist[i][0] < INF) {
                minCost = Math.min(minCost, dp[fullMask][i] + dist[i][0]);
            }
        }

        return minCost;
    }

    /**
     * Checks if a directed/undirected graph contains a Hamiltonian path.
     */
    public boolean hasHamiltonianPath(boolean[][] adj) {
        int n = adj.length;
        int numStates = 1 << n;
        boolean[][] dp = new boolean[numStates][n];

        for (int i = 0; i < n; i++) {
            dp[1 << i][i] = true;
        }

        for (int mask = 1; mask < numStates; mask++) {
            for (int u = 0; u < n; u++) {
                if (!dp[mask][u]) continue;

                for (int v = 0; v < n; v++) {
                    if ((mask & (1 << v)) == 0 && adj[u][v]) {
                        dp[mask | (1 << v)][v] = true;
                    }
                }
            }
        }

        int fullMask = (1 << n) - 1;
        for (int i = 0; i < n; i++) {
            if (dp[fullMask][i]) return true;
        }
        return false;
    }
}
