package texthack.parallel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Tree-Style Parallel Reduction Engine</h1>
 * <p>
 * Combines partial search results from P worker threads using an associative binary tree reduction:
 * </p>
 * <pre>
 * Level 0 (Workers):   [R1]       [R2]       [R3]       [R4]
 *                        \       /             \       /
 * Level 1 (Stage 1):      [R1 + R2]             [R3 + R4]
 *                             \                     /
 * Level 2 (Stage 2):               [R_final]
 * </pre>
 * <ul>
 *   <li><b>Tree Depth:</b> ceil(log2(P)) reduction stages</li>
 *   <li><b>Work:</b> O(P) total merges</li>
 *   <li><b>Span (Critical Path):</b> O(log P) merge steps</li>
 *   <li><b>Thread Safety:</b> Complete absence of race conditions due to immutable partial structures.</li>
 * </ul>
 */
@Component
public class TreeStyleParallelReduction {

    private static final Logger log = LoggerFactory.getLogger(TreeStyleParallelReduction.class);

    /**
     * Executes the tree-style parallel reduction over P worker partial results.
     *
     * @param partialResults list of partial results produced by workers
     * @return final consolidated PartialSearchResult
     */
    public PartialSearchResult reduce(List<PartialSearchResult> partialResults) {
        if (partialResults == null || partialResults.isEmpty()) {
            return new PartialSearchResult();
        }

        if (partialResults.size() == 1) {
            return partialResults.get(0);
        }

        List<PartialSearchResult> currentStage = new ArrayList<>(partialResults);
        int stage = 1;

        while (currentStage.size() > 1) {
            List<PartialSearchResult> nextStage = new ArrayList<>();
            int n = currentStage.size();

            for (int i = 0; i < n; i += 2) {
                if (i + 1 < n) {
                    // Pairwise reduction: R[i] + R[i+1]
                    PartialSearchResult merged = PartialSearchResult.merge(currentStage.get(i), currentStage.get(i + 1));
                    nextStage.add(merged);
                } else {
                    // Odd element carries over directly to next stage
                    nextStage.add(currentStage.get(i));
                }
            }

            log.debug("Reduction Stage {}: Reduced {} partitions to {}", stage++, n, nextStage.size());
            currentStage = nextStage;
        }

        return currentStage.get(0);
    }

    /**
     * Recursive Divide-and-Conquer tree reduction variant.
     */
    public PartialSearchResult reduceRecursive(List<PartialSearchResult> list, int left, int right) {
        if (left > right) return new PartialSearchResult();
        if (left == right) return list.get(left);

        int mid = (left + right) >>> 1;
        PartialSearchResult leftMerged = reduceRecursive(list, left, mid);
        PartialSearchResult rightMerged = reduceRecursive(list, mid + 1, right);

        return PartialSearchResult.merge(leftMerged, rightMerged);
    }
}
