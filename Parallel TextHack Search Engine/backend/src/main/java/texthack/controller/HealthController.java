package texthack.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import texthack.service.DocumentService;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    private final DocumentService documentService;

    public HealthController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("status", "UP");
        resp.put("system", "Parallel TextHack Search Engine");
        resp.put("javaVersion", System.getProperty("java.version"));
        resp.put("operatingSystem", System.getProperty("os.name"));
        resp.put("cpuCores", Runtime.getRuntime().availableProcessors());
        resp.put("maxHeapMb", ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax() / (1024 * 1024));
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> systemMetrics() {
        Map<String, Object> metrics = new LinkedHashMap<>();
        metrics.put("totalDocuments", documentService.getTotalDocuments());
        metrics.put("totalWords", documentService.getTotalWordCount());
        metrics.put("totalFileSizeBytes", documentService.getTotalFileSizeBytes());
        metrics.put("availableCores", Runtime.getRuntime().availableProcessors());
        metrics.put("allocatedMemoryMb", Runtime.getRuntime().totalMemory() / (1024 * 1024));
        metrics.put("freeMemoryMb", Runtime.getRuntime().freeMemory() / (1024 * 1024));
        metrics.put("supportedAlgorithms", Arrays.asList(
                "KMP", "Z_ALGORITHM", "RABIN_KARP", "NAIVE", "AHO_CORASICK",
                "SUFFIX_ARRAY", "KASAI_LCP", "LEVENSHTEIN", "DAMERAU_LEVENSHTEIN",
                "NEEDLEMAN_WUNSCH", "SMITH_WATERMAN", "DINIC_MAX_FLOW", "VERTEX_COVER_2_APPROX",
                "RANDOMIZED_QUICKSORT", "MILLER_RABIN"
        ));
        return ResponseEntity.ok(metrics);
    }
}
