package texthack.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SearchRequestDto {

    @NotBlank(message = "Search query cannot be empty")
    @Size(max = 100, message = "Query length must not exceed 100 characters")
    private String query;

    private String searchType = "EXACT"; // EXACT, PHRASE, MULTI_KEYWORD, FUZZY
    private String mode = "PARALLEL"; // SEQUENTIAL, PARALLEL

    @Min(value = 1, message = "Thread count must be at least 1")
    @Max(value = 64, message = "Thread count cannot exceed 64")
    private Integer threads = 4;

    @Min(value = 0, message = "Max edit distance cannot be negative")
    @Max(value = 5, message = "Max edit distance cannot exceed 5")
    private Integer maxEditDistance = 2; // For FUZZY search

    private String algorithm = "KMP"; // KMP, Z_ALGORITHM, RABIN_KARP, NAIVE, AHO_CORASICK

    public SearchRequestDto() {
    }

    public SearchRequestDto(String query, String searchType, String mode, Integer threads, Integer maxEditDistance, String algorithm) {
        this.query = query;
        this.searchType = searchType;
        this.mode = mode;
        this.threads = threads;
        this.maxEditDistance = maxEditDistance;
        this.algorithm = algorithm;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public Integer getMaxEditDistance() {
        return maxEditDistance;
    }

    public void setMaxEditDistance(Integer maxEditDistance) {
        this.maxEditDistance = maxEditDistance;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
