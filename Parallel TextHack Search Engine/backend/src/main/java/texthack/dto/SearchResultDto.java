package texthack.dto;

import java.util.List;

public class SearchResultDto implements Comparable<SearchResultDto> {

    private Long documentId;
    private String documentName;
    private String fileType;
    private int occurrenceCount;
    private double score;
    private Integer editDistance; // for fuzzy matches
    private String snippet;
    private List<Integer> matchPositions;

    public SearchResultDto() {
    }

    public SearchResultDto(Long documentId, String documentName, String fileType, int occurrenceCount, double score, Integer editDistance, String snippet, List<Integer> matchPositions) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.fileType = fileType;
        this.occurrenceCount = occurrenceCount;
        this.score = score;
        this.editDistance = editDistance;
        this.snippet = snippet;
        this.matchPositions = matchPositions;
    }

    @Override
    public int compareTo(SearchResultDto o) {
        // Descending order of relevance score
        return Double.compare(o.score, this.score);
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getOccurrenceCount() {
        return occurrenceCount;
    }

    public void setOccurrenceCount(int occurrenceCount) {
        this.occurrenceCount = occurrenceCount;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Integer getEditDistance() {
        return editDistance;
    }

    public void setEditDistance(Integer editDistance) {
        this.editDistance = editDistance;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public List<Integer> getMatchPositions() {
        return matchPositions;
    }

    public void setMatchPositions(List<Integer> matchPositions) {
        this.matchPositions = matchPositions;
    }
}
