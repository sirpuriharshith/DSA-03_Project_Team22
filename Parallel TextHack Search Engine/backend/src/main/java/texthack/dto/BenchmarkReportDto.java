package texthack.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BenchmarkReportDto {

    private String query;
    private String searchType;
    private int documentCount;
    private List<BenchmarkRowDto> rows;
    private LocalDateTime timestamp = LocalDateTime.now();

    public BenchmarkReportDto() {
    }

    public BenchmarkReportDto(String query, String searchType, int documentCount, List<BenchmarkRowDto> rows) {
        this.query = query;
        this.searchType = searchType;
        this.documentCount = documentCount;
        this.rows = rows;
        this.timestamp = LocalDateTime.now();
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

    public int getDocumentCount() {
        return documentCount;
    }

    public void setDocumentCount(int documentCount) {
        this.documentCount = documentCount;
    }

    public List<BenchmarkRowDto> getRows() {
        return rows;
    }

    public void setRows(List<BenchmarkRowDto> rows) {
        this.rows = rows;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
