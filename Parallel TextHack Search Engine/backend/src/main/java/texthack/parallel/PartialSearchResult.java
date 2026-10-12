package texthack.parallel;

import texthack.dto.SearchResultDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Thread-safe container holding intermediate search findings for a document partition slice.
 */
public class PartialSearchResult {

    private final List<SearchResultDto> results;
    private final int totalOccurrences;
    private final int documentsSearched;

    public PartialSearchResult() {
        this.results = Collections.emptyList();
        this.totalOccurrences = 0;
        this.documentsSearched = 0;
    }

    public PartialSearchResult(List<SearchResultDto> results, int totalOccurrences, int documentsSearched) {
        this.results = results != null ? Collections.unmodifiableList(new ArrayList<>(results)) : Collections.emptyList();
        this.totalOccurrences = totalOccurrences;
        this.documentsSearched = documentsSearched;
    }

    public List<SearchResultDto> getResults() {
        return results;
    }

    public int getTotalOccurrences() {
        return totalOccurrences;
    }

    public int getDocumentsSearched() {
        return documentsSearched;
    }

    /**
     * Associative merge operator for parallel reduction: R_combined = R_a + R_b.
     */
    public static PartialSearchResult merge(PartialSearchResult a, PartialSearchResult b) {
        if (a == null && b == null) return new PartialSearchResult();
        if (a == null) return b;
        if (b == null) return a;

        List<SearchResultDto> mergedList = new ArrayList<>(a.getResults().size() + b.getResults().size());
        mergedList.addAll(a.getResults());
        mergedList.addAll(b.getResults());

        return new PartialSearchResult(
                mergedList,
                a.getTotalOccurrences() + b.getTotalOccurrences(),
                a.getDocumentsSearched() + b.getDocumentsSearched()
        );
    }
}
