package texthack.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import texthack.dto.DocumentDto;
import texthack.model.DocumentEntity;
import texthack.repository.DocumentRepository;
import texthack.util.DocumentLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private static final Logger log = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository documentRepository;
    private final DocumentLoader documentLoader;
    private final Path documentStoragePath = Paths.get("data", "documents");

    public DocumentService(DocumentRepository documentRepository, DocumentLoader documentLoader) {
        this.documentRepository = documentRepository;
        this.documentLoader = documentLoader;
    }

    @PostConstruct
    public void init() {
        try {
            if (!Files.exists(documentStoragePath)) {
                Files.createDirectories(documentStoragePath);
            }
            syncDiskDocumentsToDatabase();
        } catch (Exception e) {
            log.warn("Failed to synchronize disk documents during startup: {}", e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<DocumentDto> getAllDocuments() {
        return documentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<DocumentDto> getDocumentById(Long id) {
        return documentRepository.findById(id).map(this::mapToDto);
    }

    @Transactional
    public DocumentDto saveUploadedDocument(String originalFileName, InputStream inputStream, long fileSize) throws IOException {
        // Sanitize file name to prevent directory traversal
        String sanitizedName = Paths.get(originalFileName).getFileName().toString();
        Path targetPath = documentStoragePath.resolve(sanitizedName);

        // Save physical file
        Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);

        // Extract and normalize
        try (InputStream readStream = Files.newInputStream(targetPath)) {
            DocumentDto dto = documentLoader.loadFromStream(readStream, sanitizedName, fileSize);
            dto.setFilePath(targetPath.toString().replace('\\', '/'));

            // Check if existing document by file name
            Optional<DocumentEntity> existing = documentRepository.findByFileName(sanitizedName);
            DocumentEntity entity = existing.orElseGet(DocumentEntity::new);

            entity.setFileName(dto.getFileName());
            entity.setFilePath(dto.getFilePath());
            entity.setFileType(dto.getFileType());
            entity.setContent(dto.getContent());
            entity.setFileSize(dto.getFileSize());
            entity.setWordCount(dto.getWordCount());
            entity.setCharacterCount(dto.getCharacterCount());
            entity.setIndexedAt(dto.getIndexedAt());

            DocumentEntity saved = documentRepository.save(entity);
            return mapToDto(saved);
        }
    }

    @Transactional
    public void deleteDocument(Long id) {
        documentRepository.findById(id).ifPresent(doc -> {
            try {
                if (doc.getFilePath() != null) {
                    Path p = Paths.get(doc.getFilePath());
                    Files.deleteIfExists(p);
                }
            } catch (IOException e) {
                log.warn("Could not delete physical file for document id {}: {}", id, e.getMessage());
            }
            documentRepository.delete(doc);
        });
    }

    @Transactional
    public void syncDiskDocumentsToDatabase() {
        if (!Files.exists(documentStoragePath)) {
            return;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(documentStoragePath)) {
            for (Path path : stream) {
                if (Files.isRegularFile(path)) {
                    String fileName = path.getFileName().toString();
                    if (documentRepository.findByFileName(fileName).isEmpty()) {
                        try {
                            DocumentDto dto = documentLoader.loadFromFile(path);
                            DocumentEntity entity = new DocumentEntity(
                                    dto.getFileName(),
                                    dto.getFilePath(),
                                    dto.getFileType(),
                                    dto.getContent(),
                                    dto.getFileSize(),
                                    dto.getWordCount(),
                                    dto.getCharacterCount()
                            );
                            documentRepository.save(entity);
                            log.info("Indexed disk document to database: {}", fileName);
                        } catch (Exception e) {
                            log.warn("Error indexing file {}: {}", fileName, e.getMessage());
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error("Failed to read document directory: {}", e.getMessage());
        }
    }

    public long getTotalDocuments() {
        return documentRepository.countTotalDocuments();
    }

    public long getTotalFileSizeBytes() {
        return documentRepository.sumTotalFileSizeBytes();
    }

    public long getTotalWordCount() {
        return documentRepository.sumTotalWordCount();
    }

    private DocumentDto mapToDto(DocumentEntity entity) {
        return new DocumentDto(
                entity.getId(),
                entity.getFileName(),
                entity.getFilePath(),
                entity.getFileType(),
                entity.getContent(),
                entity.getFileSize(),
                entity.getWordCount(),
                entity.getCharacterCount(),
                entity.getIndexedAt()
        );
    }
}
