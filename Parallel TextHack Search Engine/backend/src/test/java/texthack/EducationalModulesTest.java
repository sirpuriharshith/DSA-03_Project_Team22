package texthack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import texthack.flow.NetworkFlowModule;
import texthack.np.NpCompletenessModule;
import texthack.randomized.RandomizedAlgorithmsModule;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EducationalModulesTest {

    private final NetworkFlowModule flowModule = new NetworkFlowModule();
    private final NpCompletenessModule npModule = new NpCompletenessModule();
    private final RandomizedAlgorithmsModule randModule = new RandomizedAlgorithmsModule();

    @Test
    @DisplayName("Network Flow: Dinic vs Edmonds-Karp Max-Flow")
    void testMaxFlow() {
        NetworkFlowModule.FlowNetwork net1 = new NetworkFlowModule.FlowNetwork(4);
        net1.addEdge(0, 1, 10);
        net1.addEdge(0, 2, 5);
        net1.addEdge(1, 2, 15);
        net1.addEdge(1, 3, 10);
        net1.addEdge(2, 3, 10);

        NetworkFlowModule.FlowResult dinicRes = flowModule.dinic(net1, 0, 3);
        assertThat(dinicRes.maxFlow).isEqualTo(15);
        assertThat(dinicRes.sourceCutSet).contains(0);
        assertThat(dinicRes.sinkCutSet).contains(3);
    }

    @Test
    @DisplayName("NP-Completeness: Vertex Cover 2-Approximation")
    void testVertexCoverApproximation() {
        List<int[]> edges = Arrays.asList(
                new int[]{0, 1}, new int[]{1, 2}, new int[]{2, 3}
        );
        NpCompletenessModule.VertexCoverResult vc = npModule.approximateVertexCover(4, edges);

        // Every edge must have at least one endpoint in the cover
        for (int[] edge : edges) {
            boolean covered = vc.vertexCover.contains(edge[0]) || vc.vertexCover.contains(edge[1]);
            assertThat(covered).isTrue();
        }
        assertThat(vc.approximationRatio).isEqualTo(2.0);
    }

    @Test
    @DisplayName("Randomized: Miller-Rabin Primality Testing")
    void testMillerRabin() {
        assertThat(randModule.isPrime(2)).isTrue();
        assertThat(randModule.isPrime(17)).isTrue();
        assertThat(randModule.isPrime(1_000_000_007L)).isTrue();
        assertThat(randModule.isPrime(1_000_000_009L)).isTrue();
        assertThat(randModule.isPrime(4)).isFalse();
        assertThat(randModule.isPrime(1_000_000_000L)).isFalse();
        assertThat(randModule.isPrime(1_000_000_011L)).isFalse(); // 1000000011 = 3 * 333333337
    }

    @Test
    @DisplayName("Randomized: QuickSort Correctness")
    void testRandomizedQuickSort() {
        int[] arr = {9, 1, 8, 2, 7, 3, 6, 4, 5, 0};
        randModule.randomizedQuickSort(arr);
        assertThat(arr).isSorted();
    }
}
