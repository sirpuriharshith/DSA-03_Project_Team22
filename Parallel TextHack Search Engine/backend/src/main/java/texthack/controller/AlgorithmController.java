package texthack.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import texthack.dp.*;
import texthack.flow.NetworkFlowModule;
import texthack.np.NpCompletenessModule;
import texthack.randomized.RandomizedAlgorithmsModule;
import texthack.strings.*;

import java.util.*;

@RestController
@RequestMapping("/api/algorithms")
public class AlgorithmController {

    private final KmpMatcher kmpMatcher;
    private final ZAlgorithmMatcher zMatcher;
    private final RabinKarpMatcher rabinKarpMatcher;
    private final AhoCorasickMatcher ahoCorasickMatcher;
    private final SuffixArray suffixArray;
    private final KasaiLcp kasaiLcp;
    private final LevenshteinDistance levenshteinDistance;
    private final DamerauLevenshteinDistance damerauLevenshteinDistance;
    private final SequenceAlignment sequenceAlignment;
    private final IntervalDp intervalDp;
    private final BitmaskDp bitmaskDp;
    private final AdvancedDpLab advancedDpLab;
    private final NetworkFlowModule networkFlowModule;
    private final NpCompletenessModule npCompletenessModule;
    private final RandomizedAlgorithmsModule randomizedModule;

    public AlgorithmController(KmpMatcher kmpMatcher,
                               ZAlgorithmMatcher zMatcher,
                               RabinKarpMatcher rabinKarpMatcher,
                               AhoCorasickMatcher ahoCorasickMatcher,
                               SuffixArray suffixArray,
                               KasaiLcp kasaiLcp,
                               LevenshteinDistance levenshteinDistance,
                               DamerauLevenshteinDistance damerauLevenshteinDistance,
                               SequenceAlignment sequenceAlignment,
                               IntervalDp intervalDp,
                               BitmaskDp bitmaskDp,
                               AdvancedDpLab advancedDpLab,
                               NetworkFlowModule networkFlowModule,
                               NpCompletenessModule npCompletenessModule,
                               RandomizedAlgorithmsModule randomizedModule) {
        this.kmpMatcher = kmpMatcher;
        this.zMatcher = zMatcher;
        this.rabinKarpMatcher = rabinKarpMatcher;
        this.ahoCorasickMatcher = ahoCorasickMatcher;
        this.suffixArray = suffixArray;
        this.kasaiLcp = kasaiLcp;
        this.levenshteinDistance = levenshteinDistance;
        this.damerauLevenshteinDistance = damerauLevenshteinDistance;
        this.sequenceAlignment = sequenceAlignment;
        this.intervalDp = intervalDp;
        this.bitmaskDp = bitmaskDp;
        this.advancedDpLab = advancedDpLab;
        this.networkFlowModule = networkFlowModule;
        this.npCompletenessModule = npCompletenessModule;
        this.randomizedModule = randomizedModule;
    }

    @GetMapping("/modules")
    public ResponseEntity<Map<String, Object>> getAlgorithmModulesSummary() {
        Map<String, Object> response = new LinkedHashMap<>();

        // 1. String Algorithms
        String sampleText = "The parallel algorithm search engine uses advanced string matching.";
        String samplePattern = "algorithm";
        Map<String, Object> stringData = new LinkedHashMap<>();
        stringData.put("sampleText", sampleText);
        stringData.put("samplePattern", samplePattern);
        stringData.put("kmp", kmpMatcher.search(sampleText, samplePattern));
        stringData.put("zAlgorithm", zMatcher.search(sampleText, samplePattern));
        stringData.put("rabinKarp", rabinKarpMatcher.search(sampleText, samplePattern));
        stringData.put("ahoCorasick", ahoCorasickMatcher.searchMultiple(sampleText, Arrays.asList("algorithm", "parallel", "search")));

        int[] sa = suffixArray.buildSuffixArray(samplePattern);
        stringData.put("suffixArray", sa);
        stringData.put("lcpArray", kasaiLcp.buildLcpArray(samplePattern, sa));
        response.put("stringAlgorithms", stringData);

        // 2. Dynamic Programming
        Map<String, Object> dpData = new LinkedHashMap<>();
        dpData.put("levenshtein_algorithm_algoritm", levenshteinDistance.computeDistance("algorithm", "algoritm"));
        dpData.put("damerau_levenshtein_ca_ac", damerauLevenshteinDistance.computeDistance("ca", "ac"));
        dpData.put("needlemanWunsch", sequenceAlignment.needlemanWunsch("GCATGCU", "GATTACA", 1, 1, 1));
        dpData.put("matrixChainOperations", intervalDp.matrixChainOrder(new int[]{10, 20, 30, 40, 30}));
        dpData.put("tspMinTourCost", bitmaskDp.tsp(new int[][]{
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        }));
        response.put("dynamicProgramming", dpData);

        // 3. Network Flow
        NetworkFlowModule.FlowNetwork net = new NetworkFlowModule.FlowNetwork(6);
        net.addEdge(0, 1, 10);
        net.addEdge(0, 2, 10);
        net.addEdge(1, 2, 2);
        net.addEdge(1, 3, 4);
        net.addEdge(1, 4, 8);
        net.addEdge(2, 4, 9);
        net.addEdge(3, 5, 10);
        net.addEdge(4, 5, 10);
        NetworkFlowModule.FlowResult flowRes = networkFlowModule.dinic(net, 0, 5);

        Map<String, Object> flowData = new LinkedHashMap<>();
        flowData.put("maxFlow", flowRes.maxFlow);
        flowData.put("sourcePartition", flowRes.sourceCutSet);
        flowData.put("sinkPartition", flowRes.sinkCutSet);
        flowData.put("algorithm", "Dinic O(V^2 E)");
        response.put("networkFlow", flowData);

        // 4. NP-Completeness
        List<int[]> edges = Arrays.asList(
                new int[]{0, 1}, new int[]{1, 2}, new int[]{2, 3}, new int[]{3, 0}, new int[]{0, 2}
        );
        NpCompletenessModule.VertexCoverResult vc = npCompletenessModule.approximateVertexCover(4, edges);
        Map<String, Object> npData = new LinkedHashMap<>();
        npData.put("problem", "Minimum Vertex Cover (NP-Complete)");
        npData.put("approximateCover", vc.vertexCover);
        npData.put("approximationRatio", vc.approximationRatio);
        npData.put("guarantee", vc.guarantee);
        response.put("npCompleteness", npData);

        // 5. Randomized Algorithms
        int[] qArr = {64, 34, 25, 12, 22, 11, 90, 5};
        randomizedModule.randomizedQuickSort(qArr);

        Map<String, Object> randomData = new LinkedHashMap<>();
        randomData.put("randomizedQuickSortResult", qArr);
        randomData.put("millerRabin_1000000007_isPrime", randomizedModule.isPrime(1_000_000_007L));
        randomData.put("millerRabin_1000000009_isPrime", randomizedModule.isPrime(1_000_000_009L));
        randomData.put("millerRabin_1000000011_isPrime", randomizedModule.isPrime(1_000_000_011L));
        randomData.put("reservoirSample", randomizedModule.reservoirSample(Arrays.asList("doc1", "doc2", "doc3", "doc4", "doc5", "doc6").iterator(), 3));
        response.put("randomizedAlgorithms", randomData);

        return ResponseEntity.ok(response);
    }
}
