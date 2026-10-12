package texthack.parallel;

import org.springframework.stereotype.Service;
import texthack.dto.DocumentDto;
import texthack.dto.SearchRequestDto;
import texthack.dto.SearchResponseDto;
import texthack.dto.SearchResultDto;
import texthack.metrics.PerformanceMetricsEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <h1>Sequential Document Search Engine</h1>
 * <p>
 * Evaluates search queries across documents linearly one-by-one on a single thread.
 * Serves as the single-threaded baseline T_1 for calculating parallel speedup and efficiency.
 * </p>
 */
@Service
public class SequentialSearchEngine {

    private final SingleDocumentSearcher documentSearcher;
    private final RelevanceRankingEngine rankingEngine;
    private final PerformanceMetricsEngine metricsEngine;

    public SequentialSearchEngine(SingleDocumentSearcher documentSearcher,
                                  RelevanceRankingEngine rankingEngine,
                                  PerformanceMetricsEngine metricsEngine) {
        this.documentSearcher = documentSearcher;
        this.rankingEngine = rankingEngine;
        this.metricsEngine = metricsEngine;
    }

    public SearchResponseDto executeSearch(List<DocumentDto> corpus, SearchRequestDto request) {
        long startTime = System.nanoTime();

        List<SearchResultDto> matches = new ArrayList<>();
        int totalOccurrences = 0;

        for (DocumentDto doc : corpus) {
            Optional<SearchResultDto> result = documentSearcher.searchDocument(doc, request);
            if (result.isPresent()) {
                SearchResultDto match = result.get();
                matches.add(match);
                totalOccurrences += match.getOccurrenceCount();
            }
        }

        // Rank search results
        rankingEngine.rankResults(matches);

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;
        double throughput = metricsEngine.calculateThroughput(corpus.size(), executionTimeMs);

        SearchResponseDto response = new SearchResponseDto();
        response.setQuery(request.getQuery());
        response.setSearchType(request.getSearchType());
        response.setMode("SEQUENTIAL");
        response.setThreads(1);
        response.setAlgorithmUsed(request.getAlgorithm() != null ? request.getAlgorithm() : "KMP");
        response.setTotalDocumentsSearched(corpus.size());
        response.setMatchingDocumentsCount(matches.size());
        response.setTotalOccurrences(totalOccurrences);
        response.setExecutionTimeMs(Math.round(executionTimeMs * 100.0) / 100.0);
        response.setThroughput(throughput);
        response.setSpeedup(1.00); // Baseline speedup = 1.0
        response.setEfficiency(100.0); // Baseline efficiency = 100%
        response.setResults(matches);

        return response;
    }
}
