package texthack.np;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <h1>NP-Completeness & Approximation Algorithms Module</h1>
 * <p>
 * Demonstrates theoretical computer science concepts including P vs NP, polynomial-time reductions,
 * and polynomial-time approximation algorithms for intractable problems.
 * </p>
 * <h3>Polynomial Reduction Chain:</h3>
 * <pre>
 *   Circuit-SAT
 *       ↓ (Cook-Levin Theorem)
 *      SAT
 *       ↓
 *     3-SAT
 *       ↓
 *     CLIQUE  (Clause gadgets + literal compatibility edges)
 *       ↓
 * INDEPENDENT SET (Complement graph: G' = (V, E'))
 *       ↓
 *  VERTEX COVER   (A subset S is a vertex cover iff V \ S is an independent set)
 * </pre>
 */
@Component
public class NpCompletenessModule {

    public static class VertexCoverResult {
        public final Set<Integer> vertexCover;
        public final List<int[]> selectedMatchingEdges;
        public final double approximationRatio;
        public final String guarantee;

        public VertexCoverResult(Set<Integer> vertexCover, List<int[]> selectedMatchingEdges, double approximationRatio, String guarantee) {
            this.vertexCover = vertexCover;
            this.selectedMatchingEdges = selectedMatchingEdges;
            this.approximationRatio = approximationRatio;
            this.guarantee = guarantee;
        }
    }

    /**
     * Maximal-Matching based 2-Approximation for Minimum Vertex Cover.
     * Complexity: O(V + E)
     * Guarantee: |C| <= 2 * |OPT|
     */
    public VertexCoverResult approximateVertexCover(int numVertices, List<int[]> edges) {
        Set<Integer> cover = new HashSet<>();
        List<int[]> matching = new ArrayList<>();
        boolean[] inCover = new boolean[numVertices];

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];

            // If neither endpoint is currently covered
            if (!inCover[u] && !inCover[v]) {
                inCover[u] = true;
                inCover[v] = true;
                cover.add(u);
                cover.add(v);
                matching.add(edge);
            }
        }

        return new VertexCoverResult(
                cover,
                matching,
                2.0,
                "Size of generated cover |C| is at most 2 * |OPT| because any valid cover must pick at least 1 vertex from each disjoint matching edge."
        );
    }

    /**
     * Demonstrates polynomial-time reduction from Independent Set to Vertex Cover:
     * Graph G = (V, E) has an Independent Set of size k if and only if
     * G has a Vertex Cover of size |V| - k.
     */
    public Set<Integer> reduceIndependentSetToVertexCover(int numVertices, Set<Integer> independentSet) {
        Set<Integer> vertexCover = new HashSet<>();
        for (int i = 0; i < numVertices; i++) {
            if (!independentSet.contains(i)) {
                vertexCover.add(i);
            }
        }
        return vertexCover;
    }
}
