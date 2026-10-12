package texthack.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "benchmark_results")
public class BenchmarkResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "query_text", nullable = false)
    private String queryText;

    @Column(name = "search_type", nullable = false, length = 64)
    private String searchType;

    @Column(name = "document_count", nullable = false)
    private Integer documentCount;

    @Column(name = "thread_count", nullable = false)
    private Integer threadCount;

    @Column(name = "execution_time_ms", nullable = false)
    private Double executionTimeMs;

    @Column(name = "throughput", nullable = false)
    private Double throughput;

    @Column(name = "speedup", nullable = false)
    private Double speedup;

    @Column(name = "efficiency", nullable = false)
    private Double efficiency;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public BenchmarkResultEntity() {
    }

    public BenchmarkResultEntity(String queryText, String searchType, Integer documentCount, Integer threadCount,
                                 Double executionTimeMs, Double throughput, Double speedup, Double efficiency) {
        this.queryText = queryText;
        this.searchType = searchType;
        this.documentCount = documentCount;
        this.threadCount = threadCount;
        this.executionTimeMs = executionTimeMs;
        this.throughput = throughput;
        this.speedup = speedup;
        this.efficiency = efficiency;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public Integer getDocumentCount() {
        return documentCount;
    }

    public void setDocumentCount(Integer documentCount) {
        this.documentCount = documentCount;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }

    public Double getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(Double executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public Double getThroughput() {
        return throughput;
    }

    public void setThroughput(Double throughput) {
        this.throughput = throughput;
    }

    public Double getSpeedup() {
        return speedup;
    }

    public void setSpeedup(Double speedup) {
        this.speedup = speedup;
    }

    public Double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(Double efficiency) {
        this.efficiency = efficiency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
