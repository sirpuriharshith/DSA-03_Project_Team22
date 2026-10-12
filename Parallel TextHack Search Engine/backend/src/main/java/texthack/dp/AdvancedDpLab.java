package texthack.dp;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Advanced Dynamic Programming Module</h1>
 * <p>
 * Implements <b>Tree DP</b>, <b>Rerooting DP</b>, and <b>Sum Over Subsets (SOS DP)</b>.
 * </p>
 */
@Component
public class AdvancedDpLab {

    /**
     * Tree DP: Maximum Independent Set on Tree.
     * dp[u][0] = max weight in subtree of u if u is NOT included
     * dp[u][1] = max weight in subtree of u if u IS included
     */
    public int maxWeightIndependentSet(int n, List<List<Integer>> adj, int[] weights) {
        int[][] dp = new int[n][2];
        dfsTreeDp(0, -1, adj, weights, dp);
        return Math.max(dp[0][0], dp[0][1]);
    }

    private void dfsTreeDp(int u, int p, List<List<Integer>> adj, int[] weights, int[][] dp) {
        dp[u][0] = 0;
        dp[u][1] = weights[u];

        for (int v : adj.get(u)) {
            if (v != p) {
                dfsTreeDp(v, u, adj, weights, dp);
                dp[u][0] += Math.max(dp[v][0], dp[v][1]);
                dp[u][1] += dp[v][0];
            }
        }
    }

    /**
     * Rerooting DP: Computes sum of distances from every vertex to all other vertices in a tree.
     * Complexity: O(n) using two DFS passes.
     */
    public long[] sumOfDistancesInTree(int n, List<List<Integer>> adj) {
        long[] ans = new long[n];
        int[] count = new int[n];

        // Pass 1: Post-order DFS from root 0
        dfsPostOrder(0, -1, adj, count, ans);

        // Pass 2: Pre-order DFS to reroot
        dfsPreOrder(0, -1, adj, count, ans, n);

        return ans;
    }

    private void dfsPostOrder(int u, int p, List<List<Integer>> adj, int[] count, long[] ans) {
        count[u] = 1;
        for (int v : adj.get(u)) {
            if (v != p) {
                dfsPostOrder(v, u, adj, count, ans);
                count[u] += count[v];
                ans[u] += ans[v] + count[v];
            }
        }
    }

    private void dfsPreOrder(int u, int p, List<List<Integer>> adj, int[] count, long[] ans, int n) {
        for (int v : adj.get(u)) {
            if (v != p) {
                // When moving root from u to v:
                // ans[v] = ans[u] - count[v] + (n - count[v])
                ans[v] = ans[u] - count[v] + (n - count[v]);
                dfsPreOrder(v, u, adj, count, ans, n);
            }
        }
    }

    /**
     * SOS DP (Sum Over Subsets):
     * Given an array A of size 2^N, computes for every mask F[mask] = sum_{submask subseteq mask} A[submask].
     * Standard brute force: O(3^N). SOS DP: O(N * 2^N).
     */
    public int[] sumOverSubsets(int[] a, int numBits) {
        int total = 1 << numBits;
        int[] dp = new int[total];
        System.arraycopy(a, 0, dp, 0, Math.min(a.length, total));

        for (int i = 0; i < numBits; i++) {
            for (int mask = 0; mask < total; mask++) {
                if ((mask & (1 << i)) != 0) {
                    dp[mask] += dp[mask ^ (1 << i)];
                }
            }
        }
        return dp;
    }
}
