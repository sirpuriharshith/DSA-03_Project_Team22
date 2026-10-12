package texthack.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA Entity representing an indexed document in texthack_db.
 */
@Entity
@Table(name = "documents")
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false, length = 1024)
    private String filePath;

    @Column(name = "file_type", nullable = false, length = 32)
    private String fileType; // TXT, PDF, DOCX, CSV

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "word_count", nullable = false)
    private Integer wordCount = 0;

    @Column(name = "character_count", nullable = false)
    private Integer characterCount = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "indexed_at")
    private LocalDateTime indexedAt = LocalDateTime.now();

    public DocumentEntity() {
    }

    public DocumentEntity(String fileName, String filePath, String fileType, String content, Long fileSize, Integer wordCount, Integer characterCount) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.content = content;
        this.fileSize = fileSize;
        this.wordCount = wordCount;
        this.characterCount = characterCount;
        this.createdAt = LocalDateTime.now();
        this.indexedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public Integer getCharacterCount() {
        return characterCount;
    }

    public void setCharacterCount(Integer characterCount) {
        this.characterCount = characterCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getIndexedAt() {
        return indexedAt;
    }

    public void setIndexedAt(LocalDateTime indexedAt) {
        this.indexedAt = indexedAt;
    }
}
