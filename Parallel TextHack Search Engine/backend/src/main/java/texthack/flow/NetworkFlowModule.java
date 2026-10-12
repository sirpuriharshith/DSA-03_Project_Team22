package texthack.flow;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <h1>Network Flow Educational Module</h1>
 * <p>
 * Demonstrates classical maximum flow and minimum cut algorithms:
 * </p>
 * <ul>
 *   <li><b>Edmonds-Karp:</b> Breadth-First Search shortest augmenting paths in O(V * E²).</li>
 *   <li><b>Dinic's Algorithm:</b> Level Graph BFS + Blocking Flow DFS in O(V² * E).</li>
 *   <li><b>Max-Flow Min-Cut:</b> Finding the bottleneck cut partitions (S, T).</li>
 *   <li><b>Maximum Bipartite Matching:</b> Reduction from bipartite graph to max flow.</li>
 * </ul>
 */
@Component
public class NetworkFlowModule {

    public static class FlowEdge {
        public final int u, v;
        public final int capacity;
        public int flow;
        public FlowEdge residual;

        public FlowEdge(int u, int v, int capacity) {
            this.u = u;
            this.v = v;
            this.capacity = capacity;
            this.flow = 0;
        }

        public int remainingCapacity() {
            return capacity - flow;
        }

        public void augment(int bottleneck) {
            this.flow += bottleneck;
            this.residual.flow -= bottleneck;
        }
    }

    public static class FlowNetwork {
        public final int n;
        public final List<List<FlowEdge>> adj;

        public FlowNetwork(int n) {
            this.n = n;
            this.adj = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                this.adj.add(new ArrayList<>());
            }
        }

        public void addEdge(int u, int v, int cap) {
            FlowEdge forward = new FlowEdge(u, v, cap);
            FlowEdge backward = new FlowEdge(v, u, 0);
            forward.residual = backward;
            backward.residual = forward;
            adj.get(u).add(forward);
            adj.get(v).add(backward);
        }
    }

    public static class FlowResult {
        public final int maxFlow;
        public final List<FlowEdge> minCutEdges;
        public final Set<Integer> sourceCutSet;
        public final Set<Integer> sinkCutSet;
        public final String algorithm;

        public FlowResult(int maxFlow, List<FlowEdge> minCutEdges, Set<Integer> sourceCutSet, Set<Integer> sinkCutSet, String algorithm) {
            this.maxFlow = maxFlow;
            this.minCutEdges = minCutEdges;
            this.sourceCutSet = sourceCutSet;
            this.sinkCutSet = sinkCutSet;
            this.algorithm = algorithm;
        }
    }

    /**
     * Edmonds-Karp Algorithm: O(V * E^2).
     */
    public FlowResult edmondsKarp(FlowNetwork network, int s, int t) {
        int maxFlow = 0;

        while (true) {
            FlowEdge[] parentEdge = new FlowEdge[network.n];
            Queue<Integer> q = new ArrayDeque<>();
            q.add(s);

            while (!q.isEmpty() && parentEdge[t] == null) {
                int curr = q.poll();
                for (FlowEdge edge : network.adj.get(curr)) {
                    if (edge.v != s && parentEdge[edge.v] == null && edge.remainingCapacity() > 0) {
                        parentEdge[edge.v] = edge;
                        q.add(edge.v);
                    }
                }
            }

            if (parentEdge[t] == null) {
                break; // No augmenting path exists
            }

            // Find bottleneck capacity along path
            int bottleneck = Integer.MAX_VALUE;
            for (FlowEdge edge = parentEdge[t]; edge != null; edge = parentEdge[edge.u]) {
                bottleneck = Math.min(bottleneck, edge.remainingCapacity());
            }

            // Augment flow
            for (FlowEdge edge = parentEdge[t]; edge != null; edge = parentEdge[edge.u]) {
                edge.augment(bottleneck);
            }

            maxFlow += bottleneck;
        }

        return computeMinCut(network, s, t, maxFlow, "EDMONDS_KARP");
    }

    /**
     * Dinic's Algorithm: O(V^2 * E).
     */
    public FlowResult dinic(FlowNetwork network, int s, int t) {
        int maxFlow = 0;
        int[] level = new int[network.n];
        int[] ptr = new int[network.n];

        while (bfsDinic(network, s, t, level)) {
            Arrays.fill(ptr, 0);
            while (true) {
                int pushed = dfsDinic(network, s, t, Integer.MAX_VALUE, level, ptr);
                if (pushed == 0) break;
                maxFlow += pushed;
            }
        }

        return computeMinCut(network, s, t, maxFlow, "DINIC");
    }

    private boolean bfsDinic(FlowNetwork network, int s, int t, int[] level) {
        Arrays.fill(level, -1);
        level[s] = 0;
        Queue<Integer> q = new ArrayDeque<>();
        q.add(s);

        while (!q.isEmpty()) {
            int u = q.poll();
            for (FlowEdge edge : network.adj.get(u)) {
                if (edge.remainingCapacity() > 0 && level[edge.v] == -1) {
                    level[edge.v] = level[u] + 1;
                    q.add(edge.v);
                }
            }
        }
        return level[t] != -1;
    }

    private int dfsDinic(FlowNetwork network, int u, int t, int pushed, int[] level, int[] ptr) {
        if (pushed == 0 || u == t) return pushed;

        for (int cid = ptr[u]; cid < network.adj.get(u).size(); cid = ++ptr[u]) {
            FlowEdge edge = network.adj.get(u).get(cid);
            if (level[edge.v] != level[u] + 1 || edge.remainingCapacity() == 0) {
                continue;
            }

            int tr = dfsDinic(network, edge.v, t, Math.min(pushed, edge.remainingCapacity()), level, ptr);
            if (tr == 0) continue;

            edge.augment(tr);
            return tr;
        }
        return 0;
    }

    private FlowResult computeMinCut(FlowNetwork network, int s, int t, int maxFlow, String algo) {
        // BFS to find all vertices reachable from s in residual graph
        boolean[] visited = new boolean[network.n];
        Queue<Integer> q = new ArrayDeque<>();
        q.add(s);
        visited[s] = true;

        Set<Integer> sourceSet = new HashSet<>();
        Set<Integer> sinkSet = new HashSet<>();

        while (!q.isEmpty()) {
            int u = q.poll();
            sourceSet.add(u);
            for (FlowEdge edge : network.adj.get(u)) {
                if (!visited[edge.v] && edge.remainingCapacity() > 0) {
                    visited[edge.v] = true;
                    q.add(edge.v);
                }
            }
        }

        for (int i = 0; i < network.n; i++) {
            if (!sourceSet.contains(i)) {
                sinkSet.add(i);
            }
        }

        List<FlowEdge> cutEdges = new ArrayList<>();
        for (int u : sourceSet) {
            for (FlowEdge edge : network.adj.get(u)) {
                if (sinkSet.contains(edge.v) && edge.capacity > 0) {
                    cutEdges.add(edge);
                }
            }
        }

        return new FlowResult(maxFlow, cutEdges, sourceSet, sinkSet, algo);
    }

    /**
     * Maximum Bipartite Matching:
     * Reduces bipartite matching between Left set (0..L-1) and Right set (0..R-1) to Dinic flow.
     */
    public int maximumBipartiteMatching(int numLeft, int numRight, List<int[]> edges) {
        int source = 0;
        int sink = numLeft + numRight + 1;
        FlowNetwork net = new FlowNetwork(sink + 1);

        for (int i = 1; i <= numLeft; i++) {
            net.addEdge(source, i, 1);
        }
        for (int j = 1; j <= numRight; j++) {
            net.addEdge(numLeft + j, sink, 1);
        }
        for (int[] edge : edges) {
            net.addEdge(edge[0] + 1, numLeft + edge[1] + 1, 1);
        }

        FlowResult res = dinic(net, source, sink);
        return res.maxFlow;
    }
}
