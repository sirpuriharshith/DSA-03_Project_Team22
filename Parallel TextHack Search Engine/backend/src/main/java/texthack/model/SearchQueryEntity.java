package texthack.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "search_queries")
public class SearchQueryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "query_text", nullable = false)
    private String queryText;

    @Column(name = "search_type", nullable = false, length = 64)
    private String searchType; // EXACT, PHRASE, MULTI_KEYWORD, FUZZY

    @Column(name = "execution_mode", nullable = false, length = 32)
    private String executionMode; // SEQUENTIAL, PARALLEL

    @Column(name = "thread_count", nullable = false)
    private Integer threadCount = 1;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public SearchQueryEntity() {
    }

    public SearchQueryEntity(String queryText, String searchType, String executionMode, Integer threadCount) {
        this.queryText = queryText;
        this.searchType = searchType;
        this.executionMode = executionMode;
        this.threadCount = threadCount;
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

    public String getExecutionMode() {
        return executionMode;
    }

    public void setExecutionMode(String executionMode) {
        this.executionMode = executionMode;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
