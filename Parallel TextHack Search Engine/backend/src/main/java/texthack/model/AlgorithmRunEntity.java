package texthack.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "algorithm_runs")
public class AlgorithmRunEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "algorithm_name", nullable = false, length = 128)
    private String algorithmName;

    @Column(name = "input_size", nullable = false)
    private Integer inputSize;

    @Column(name = "execution_time_ns", nullable = false)
    private Long executionTimeNs;

    @Column(name = "memory_used_bytes", nullable = false)
    private Long memoryUsedBytes;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public AlgorithmRunEntity() {
    }

    public AlgorithmRunEntity(String algorithmName, Integer inputSize, Long executionTimeNs, Long memoryUsedBytes) {
        this.algorithmName = algorithmName;
        this.inputSize = inputSize;
        this.executionTimeNs = executionTimeNs;
        this.memoryUsedBytes = memoryUsedBytes;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public Integer getInputSize() {
        return inputSize;
    }

    public void setInputSize(Integer inputSize) {
        this.inputSize = inputSize;
    }

    public Long getExecutionTimeNs() {
        return executionTimeNs;
    }

    public void setExecutionTimeNs(Long executionTimeNs) {
        this.executionTimeNs = executionTimeNs;
    }

    public Long getMemoryUsedBytes() {
        return memoryUsedBytes;
    }

    public void setMemoryUsedBytes(Long memoryUsedBytes) {
        this.memoryUsedBytes = memoryUsedBytes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
