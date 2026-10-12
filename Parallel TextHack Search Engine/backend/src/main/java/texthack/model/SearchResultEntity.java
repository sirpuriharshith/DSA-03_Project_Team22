package texthack.model;

import jakarta.persistence.*;

@Entity
@Table(name = "search_results")
public class SearchResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "query_id", nullable = false)
    private SearchQueryEntity query;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private DocumentEntity document;

    @Column(name = "occurrence_count", nullable = false)
    private Integer occurrenceCount = 0;

    @Column(name = "score", nullable = false)
    private Double score = 0.0;

    @Column(name = "edit_distance")
    private Integer editDistance;

    public SearchResultEntity() {
    }

    public SearchResultEntity(SearchQueryEntity query, DocumentEntity document, Integer occurrenceCount, Double score, Integer editDistance) {
        this.query = query;
        this.document = document;
        this.occurrenceCount = occurrenceCount;
        this.score = score;
        this.editDistance = editDistance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SearchQueryEntity getQuery() {
        return query;
    }

    public void setQuery(SearchQueryEntity query) {
        this.query = query;
    }

    public DocumentEntity getDocument() {
        return document;
    }

    public void setDocument(DocumentEntity document) {
        this.document = document;
    }

    public Integer getOccurrenceCount() {
        return occurrenceCount;
    }

    public void setOccurrenceCount(Integer occurrenceCount) {
        this.occurrenceCount = occurrenceCount;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getEditDistance() {
        return editDistance;
    }

    public void setEditDistance(Integer editDistance) {
        this.editDistance = editDistance;
    }
}
