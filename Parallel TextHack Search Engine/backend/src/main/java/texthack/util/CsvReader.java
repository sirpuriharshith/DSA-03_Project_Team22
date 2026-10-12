package texthack.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class CsvReader implements DocumentReader {

    @Override
    public String extractText(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.parse(reader)) {
            for (CSVRecord record : parser) {
                for (int i = 0; i < record.size(); i++) {
                    sb.append(record.get(i)).append(" ");
                }
                sb.append("\n");
            }
        }
        return sb.toString().trim();
    }

    @Override
    public boolean supports(String extension) {
        return "csv".equalsIgnoreCase(extension);
    }
}
