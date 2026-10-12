package texthack.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a normalized document across loaders and engine layers.
 */
public class DocumentDto {

    private Long id;
    private String fileName;
    private String filePath;
    private String fileType; // TXT, PDF, DOCX, CSV
    private String content;
    private Long fileSize;
    private Integer wordCount;
    private Integer characterCount;
    private LocalDateTime indexedAt;

    public DocumentDto() {
    }

    public DocumentDto(Long id, String fileName, String filePath, String fileType, String content, Long fileSize, Integer wordCount, Integer characterCount, LocalDateTime indexedAt) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.content = content;
        this.fileSize = fileSize;
        this.wordCount = wordCount;
        this.characterCount = characterCount;
        this.indexedAt = indexedAt;
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

    public LocalDateTime getIndexedAt() {
        return indexedAt;
    }

    public void setIndexedAt(LocalDateTime indexedAt) {
        this.indexedAt = indexedAt;
    }
}
