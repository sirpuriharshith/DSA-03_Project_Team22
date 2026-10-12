package texthack.parallel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import texthack.dto.DocumentDto;
import texthack.dto.SearchRequestDto;
import texthack.dto.SearchResponseDto;
import texthack.dto.SearchResultDto;
import texthack.metrics.PerformanceMetricsEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * <h1>Parallel Document Search Engine</h1>
 * <p>
 * Partitions the document corpus into P balanced disjoint slices, executes searches concurrently
 * across worker threads, and consolidates the partial findings using a tree-style parallel reduction.
 * </p>
 */
@Service
public class ParallelSearchEngine {

    private static final Logger log = LoggerFactory.getLogger(ParallelSearchEngine.class);

    private final SingleDocumentSearcher documentSearcher;
    private final TreeStyleParallelReduction reductionEngine;
    private final RelevanceRankingEngine rankingEngine;
    private final PerformanceMetricsEngine metricsEngine;

    public ParallelSearchEngine(SingleDocumentSearcher documentSearcher,
                                TreeStyleParallelReduction reductionEngine,
                                RelevanceRankingEngine rankingEngine,
                                PerformanceMetricsEngine metricsEngine) {
        this.documentSearcher = documentSearcher;
        this.reductionEngine = reductionEngine;
        this.rankingEngine = rankingEngine;
        this.metricsEngine = metricsEngine;
    }

    public SearchResponseDto executeSearch(List<DocumentDto> corpus, SearchRequestDto request, double sequentialTimeMs) {
        int availableCores = Runtime.getRuntime().availableProcessors();
        int requestedThreads = (request.getThreads() != null && request.getThreads() > 0) ? request.getThreads() : 4;
        int threadCount = Math.min(requestedThreads, availableCores);

        if (corpus == null || corpus.isEmpty()) {
            SearchResponseDto empty = new SearchResponseDto();
            empty.setQuery(request.getQuery());
            empty.setMode("PARALLEL");
            empty.setThreads(threadCount);
            empty.setResults(Collections.emptyList());
            return empty;
        }

        // If corpus size is smaller than thread count, clamp thread count
        threadCount = Math.min(threadCount, corpus.size());

        // 1. Partition document corpus across threadCount workers
        List<List<DocumentDto>> partitions = partitionCorpus(corpus, threadCount);

        long startTime = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        List<Future<PartialSearchResult>> futures = new ArrayList<>();
        for (List<DocumentDto> slice : partitions) {
            futures.add(executor.submit(() -> searchPartition(slice, request)));
        }

        // 2. Gather worker partial results
        List<PartialSearchResult> partialResults = new ArrayList<>();
        try {
            for (Future<PartialSearchResult> future : futures) {
                partialResults.add(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Parallel search worker task interrupted: {}", e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }

        // 3. Tree-Style Parallel Reduction Phase
        PartialSearchResult consolidated = reductionEngine.reduce(partialResults);

        // 4. Rank final consolidated search results
        List<SearchResultDto> rankedResults = new ArrayList<>(consolidated.getResults());
        rankingEngine.rankResults(rankedResults);

        long endTime = System.nanoTime();
        double parallelTimeMs = (endTime - startTime) / 1_000_000.0;

        // 5. Compute Performance Metrics
        double throughput = metricsEngine.calculateThroughput(corpus.size(), parallelTimeMs);
        double speedup = metricsEngine.calculateSpeedup(sequentialTimeMs, parallelTimeMs);
        double efficiency = metricsEngine.calculateEfficiency(speedup, threadCount);

        SearchResponseDto response = new SearchResponseDto();
        response.setQuery(request.getQuery());
        response.setSearchType(request.getSearchType());
        response.setMode("PARALLEL");
        response.setThreads(threadCount);
        response.setAlgorithmUsed(request.getAlgorithm() != null ? request.getAlgorithm() : "KMP");
        response.setTotalDocumentsSearched(corpus.size());
        response.setMatchingDocumentsCount(rankedResults.size());
        response.setTotalOccurrences(consolidated.getTotalOccurrences());
        response.setExecutionTimeMs(Math.round(parallelTimeMs * 100.0) / 100.0);
        response.setThroughput(throughput);
        response.setSpeedup(speedup);
        response.setEfficiency(efficiency);
        response.setResults(rankedResults);

        return response;
    }

    /**
     * Searches a single document partition slice.
     */
    private PartialSearchResult searchPartition(List<DocumentDto> slice, SearchRequestDto request) {
        List<SearchResultDto> matches = new ArrayList<>();
        int totalOccurrences = 0;

        for (DocumentDto doc : slice) {
            Optional<SearchResultDto> result = documentSearcher.searchDocument(doc, request);
            if (result.isPresent()) {
                SearchResultDto match = result.get();
                matches.add(match);
                totalOccurrences += match.getOccurrenceCount();
            }
        }

        return new PartialSearchResult(matches, totalOccurrences, slice.size());
    }

    /**
     * Partitions corpus uniformly into P balanced disjoint subsets.
     */
    public List<List<DocumentDto>> partitionCorpus(List<DocumentDto> corpus, int partitionsCount) {
        List<List<DocumentDto>> partitions = new ArrayList<>(partitionsCount);
        int total = corpus.size();
        int baseChunkSize = total / partitionsCount;
        int remainder = total % partitionsCount;

        int currentStart = 0;
        for (int i = 0; i < partitionsCount; i++) {
            int currentChunkSize = baseChunkSize + (i < remainder ? 1 : 0);
            int currentEnd = currentStart + currentChunkSize;
            partitions.add(new ArrayList<>(corpus.subList(currentStart, currentEnd)));
            currentStart = currentEnd;
        }

        return partitions;
    }
}
