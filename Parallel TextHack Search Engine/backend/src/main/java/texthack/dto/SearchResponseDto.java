package texthack.dto;

import java.util.List;

public class SearchResponseDto {

    private String query;
    private String searchType;
    private String mode;
    private int threads;
    private String algorithmUsed;
    private int totalDocumentsSearched;
    private int matchingDocumentsCount;
    private int totalOccurrences;
    private double executionTimeMs;
    private double throughput; // documents / sec
    private double speedup;
    private double efficiency;
    private List<SearchResultDto> results;

    public SearchResponseDto() {
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

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public String getAlgorithmUsed() {
        return algorithmUsed;
    }

    public void setAlgorithmUsed(String algorithmUsed) {
        this.algorithmUsed = algorithmUsed;
    }

    public int getTotalDocumentsSearched() {
        return totalDocumentsSearched;
    }

    public void setTotalDocumentsSearched(int totalDocumentsSearched) {
        this.totalDocumentsSearched = totalDocumentsSearched;
    }

    public int getMatchingDocumentsCount() {
        return matchingDocumentsCount;
    }

    public void setMatchingDocumentsCount(int matchingDocumentsCount) {
        this.matchingDocumentsCount = matchingDocumentsCount;
    }

    public int getTotalOccurrences() {
        return totalOccurrences;
    }

    public void setTotalOccurrences(int totalOccurrences) {
        this.totalOccurrences = totalOccurrences;
    }

    public double getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(double executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public double getThroughput() {
        return throughput;
    }

    public void setThroughput(double throughput) {
        this.throughput = throughput;
    }

    public double getSpeedup() {
        return speedup;
    }

    public void setSpeedup(double speedup) {
        this.speedup = speedup;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    public List<SearchResultDto> getResults() {
        return results;
    }

    public void setResults(List<SearchResultDto> results) {
        this.results = results;
    }
}
