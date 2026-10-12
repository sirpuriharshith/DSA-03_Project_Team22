package texthack.util;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class DocxReader implements DocumentReader {

    @Override
    public String extractText(InputStream inputStream) throws IOException {
        try (XWPFDocument document = new XWPFDocument(inputStream);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return extractor.getText().trim();
        }
    }

    @Override
    public boolean supports(String extension) {
        return "docx".equalsIgnoreCase(extension);
    }
}
