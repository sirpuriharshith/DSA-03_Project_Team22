package texthack.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * Common document extraction abstraction.
 */
public interface DocumentReader {

    /**
     * Extracts text content from the given input stream.
     *
     * @param inputStream document byte stream
     * @return normalized UTF-8 string content
     * @throws IOException on parsing or I/O error
     */
    String extractText(InputStream inputStream) throws IOException;

    /**
     * Checks if this reader supports the given file extension.
     *
     * @param extension lowercase extension (e.g., "txt", "pdf", "docx", "csv")
     * @return true if supported
     */
    boolean supports(String extension);
}
