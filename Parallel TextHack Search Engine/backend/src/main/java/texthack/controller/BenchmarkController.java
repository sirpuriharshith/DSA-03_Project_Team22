package texthack.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import texthack.dto.BenchmarkReportDto;
import texthack.model.BenchmarkResultEntity;
import texthack.service.BenchmarkService;

import java.util.List;

@RestController
@RequestMapping("/api/benchmarks")
public class BenchmarkController {

    private final BenchmarkService benchmarkService;

    public BenchmarkController(BenchmarkService benchmarkService) {
        this.benchmarkService = benchmarkService;
    }

    @GetMapping
    public ResponseEntity<List<BenchmarkResultEntity>> getRecentBenchmarks() {
        return ResponseEntity.ok(benchmarkService.getRecentBenchmarks());
    }

    @PostMapping("/run")
    public ResponseEntity<BenchmarkReportDto> runBenchmark(
            @RequestParam(value = "query", defaultValue = "algorithm") String query,
            @RequestParam(value = "searchType", defaultValue = "EXACT") String searchType) {
        BenchmarkReportDto report = benchmarkService.runScalabilityBenchmark(query, searchType);
        return ResponseEntity.ok(report);
    }
}
