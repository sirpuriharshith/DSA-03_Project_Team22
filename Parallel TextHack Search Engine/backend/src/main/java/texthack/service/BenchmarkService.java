package texthack.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import texthack.dto.*;
import texthack.model.BenchmarkResultEntity;
import texthack.parallel.ParallelSearchEngine;
import texthack.parallel.SequentialSearchEngine;
import texthack.repository.BenchmarkResultRepository;

import java.util.*;

@Service
public class BenchmarkService {

    private static final Logger log = LoggerFactory.getLogger(BenchmarkService.class);

    private final DocumentService documentService;
    private final SequentialSearchEngine sequentialEngine;
    private final ParallelSearchEngine parallelEngine;
    private final BenchmarkResultRepository benchmarkResultRepository;

    public BenchmarkService(DocumentService documentService,
                            SequentialSearchEngine sequentialEngine,
                            ParallelSearchEngine parallelEngine,
                            BenchmarkResultRepository benchmarkResultRepository) {
        this.documentService = documentService;
        this.sequentialEngine = sequentialEngine;
        this.parallelEngine = parallelEngine;
        this.benchmarkResultRepository = benchmarkResultRepository;
    }

    public BenchmarkReportDto runScalabilityBenchmark(String query, String searchType) {
        if (query == null || query.isBlank()) {
            query = "algorithm";
        }
        if (searchType == null || searchType.isBlank()) {
            searchType = "EXACT";
        }

        List<DocumentDto> corpus = documentService.getAllDocuments();
        int docCount = corpus.size();
        int maxCores = Runtime.getRuntime().availableProcessors();

        // Determine thread list to evaluate
        Set<Integer> threadSet = new TreeSet<>();
        threadSet.add(1);
        if (maxCores >= 2) threadSet.add(2);
        if (maxCores >= 4) threadSet.add(4);
        if (maxCores >= 8) threadSet.add(8);
        threadSet.add(maxCores);

        List<Integer> threadCounts = new ArrayList<>(threadSet);
        Collections.sort(threadCounts);

        SearchRequestDto baseRequest = new SearchRequestDto(query, searchType, "SEQUENTIAL", 1, 2, "KMP");

        // Warm up JVM JIT compiler
        for (int w = 0; w < 2; w++) {
            sequentialEngine.executeSearch(corpus, baseRequest);
        }

        // Measure single-threaded baseline T_1 (average of 3 runs for stability)
        double totalT1 = 0;
        int warmupRuns = 3;
        for (int r = 0; r < warmupRuns; r++) {
            SearchResponseDto res1 = sequentialEngine.executeSearch(corpus, baseRequest);
            totalT1 += res1.getExecutionTimeMs();
        }
        double baselineT1 = Math.max(0.1, totalT1 / warmupRuns);

        List<BenchmarkRowDto> rows = new ArrayList<>();

        for (int threads : threadCounts) {
            SearchRequestDto pRequest = new SearchRequestDto(query, searchType, "PARALLEL", threads, 2, "KMP");

            double totalTp = 0;
            int testRuns = 3;
            for (int r = 0; r < testRuns; r++) {
                SearchResponseDto pRes = parallelEngine.executeSearch(corpus, pRequest, baselineT1);
                totalTp += pRes.getExecutionTimeMs();
            }
            double avgTp = Math.max(0.05, totalTp / testRuns);

            double throughput = (docCount > 0 && avgTp > 0) ? Math.round((docCount / (avgTp / 1000.0)) * 10.0) / 10.0 : 0.0;
            double speedup = Math.round((baselineT1 / avgTp) * 100.0) / 100.0;
            double efficiency = Math.min(100.0, Math.round(((speedup / threads) * 100.0) * 10.0) / 10.0);

            if (threads == 1) {
                speedup = 1.00;
                efficiency = 100.0;
            }

            BenchmarkRowDto row = new BenchmarkRowDto(threads, Math.round(avgTp * 100.0) / 100.0, throughput, speedup, efficiency);
            rows.add(row);

            // Persist to database
            try {
                BenchmarkResultEntity entity = new BenchmarkResultEntity(
                        query, searchType, docCount, threads, row.getExecutionTimeMs(), row.getThroughput(), row.getSpeedup(), row.getEfficiency()
                );
                benchmarkResultRepository.save(entity);
            } catch (Exception e) {
                log.warn("Could not persist benchmark result to DB: {}", e.getMessage());
            }
        }

        return new BenchmarkReportDto(query, searchType, docCount, rows);
    }

    public List<BenchmarkResultEntity> getRecentBenchmarks() {
        return benchmarkResultRepository.findTop20ByOrderByCreatedAtDesc();
    }
}
