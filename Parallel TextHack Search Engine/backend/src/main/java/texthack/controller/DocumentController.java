package texthack.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import texthack.dto.DocumentDto;
import texthack.service.DocumentService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public ResponseEntity<List<DocumentDto>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDocumentById(@PathVariable Long id) {
        return documentService.getDocumentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Uploaded file is empty");
            return ResponseEntity.badRequest().body(err);
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !isValidExtension(originalName)) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Unsupported file format. Supported: .txt, .pdf, .docx, .csv");
            return ResponseEntity.badRequest().body(err);
        }

        try {
            DocumentDto saved = documentService.saveUploadedDocument(originalName, file.getInputStream(), file.getSize());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IOException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Error parsing document: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "Document deleted successfully");
        return ResponseEntity.ok(resp);
    }

    private boolean isValidExtension(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".txt") || lower.endsWith(".pdf") || lower.endsWith(".docx") || lower.endsWith(".csv");
    }
}
