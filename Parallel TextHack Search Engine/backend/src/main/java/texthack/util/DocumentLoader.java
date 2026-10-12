package texthack.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import texthack.dto.DocumentDto;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service that orchestrates document format detection, extraction, and Unicode text normalization.
 */
@Service
public class DocumentLoader {

    private static final Logger log = LoggerFactory.getLogger(DocumentLoader.class);

    private final List<DocumentReader> readers;

    public DocumentLoader(List<DocumentReader> readers) {
        this.readers = readers;
    }

    /**
     * Extracts and normalizes text from an InputStream given the filename.
     */
    public DocumentDto loadFromStream(InputStream inputStream, String originalFilename, long fileSize) throws IOException {
        String extension = getFileExtension(originalFilename);
        DocumentReader selectedReader = readers.stream()
                .filter(r -> r.supports(extension))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported document extension: ." + extension));

        String rawText = selectedReader.extractText(inputStream);
        String normalizedText = normalizeText(rawText);

        int wordCount = countWords(normalizedText);
        int charCount = normalizedText.length();

        DocumentDto doc = new DocumentDto();
        doc.setFileName(originalFilename);
        doc.setFileType(extension.toUpperCase());
        doc.setContent(normalizedText);
        doc.setFileSize(fileSize);
        doc.setWordCount(wordCount);
        doc.setCharacterCount(charCount);
        doc.setIndexedAt(LocalDateTime.now());

        return doc;
    }

    /**
     * Extracts and normalizes text from a local disk file.
     */
    public DocumentDto loadFromFile(Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found at: " + filePath.toAbsolutePath());
        }

        String fileName = filePath.getFileName().toString();
        long size = Files.size(filePath);

        try (InputStream is = Files.newInputStream(filePath)) {
            DocumentDto dto = loadFromStream(is, fileName, size);
            dto.setFilePath(filePath.toString().replace('\\', '/'));
            return dto;
        }
    }

    /**
     * Unicode NFC normalization and clean whitespace handling.
     */
    public String normalizeText(String raw) {
        if (raw == null || raw.isEmpty()) {
            return "";
        }
        // Unicode NFC Canonical Decomposition, followed by Canonical Composition
        String nfc = Normalizer.normalize(raw, Normalizer.Form.NFC);

        // Normalize carriage returns and line endings
        return nfc.replace("\r\n", "\n").replace('\r', '\n');
    }

    /**
     * Accurate word count supporting international characters.
     */
    public int countWords(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        int count = 0;
        boolean inWord = false;
        int len = text.length();

        for (int i = 0; i < len; i++) {
            char c = text.charAt(i);
            if (Character.isWhitespace(c) || Character.isSpaceChar(c)) {
                inWord = false;
            } else {
                if (!inWord) {
                    count++;
                    inWord = true;
                }
            }
        }
        return count;
    }

    public static String getFileExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1 || lastDot == filename.length() - 1) {
            return "";
        }
        return filename.substring(lastDot + 1).toLowerCase();
    }
}
