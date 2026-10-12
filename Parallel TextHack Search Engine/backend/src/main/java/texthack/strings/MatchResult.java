package texthack.strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Result of executing a string matching algorithm over a document.
 */
public class MatchResult {

    private final String algorithmName;
    private final String pattern;
    private final int count;
    private final List<Integer> matchIndices;
    private final long executionTimeNs;

    public MatchResult(String algorithmName, String pattern, List<Integer> matchIndices, long executionTimeNs) {
        this.algorithmName = algorithmName;
        this.pattern = pattern;
        this.matchIndices = matchIndices != null ? Collections.unmodifiableList(new ArrayList<>(matchIndices)) : Collections.emptyList();
        this.count = this.matchIndices.size();
        this.executionTimeNs = executionTimeNs;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public String getPattern() {
        return pattern;
    }

    public int getCount() {
        return count;
    }

    public List<Integer> getMatchIndices() {
        return matchIndices;
    }

    public long getExecutionTimeNs() {
        return executionTimeNs;
    }

    public boolean hasMatch() {
        return count > 0;
    }
}
