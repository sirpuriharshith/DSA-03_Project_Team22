package texthack.parallel;

import org.springframework.stereotype.Component;
import texthack.dto.DocumentDto;
import texthack.dto.SearchResultDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <h1>Explainable Multi-Factor Relevance Ranking Engine</h1>
 * <p>
 * Computes deterministic relevance scores for matching documents using a transparent,
 * academically documented scoring formula without proprietary "black box" heuristics.
 * </p>
 * <h3>Scoring Formula:</h3>
 * <pre>
 * Normalized Frequency = Occurrence Count / (Document Word Count + 1.0)
 * Base Frequency Score = Normalized Frequency * 10.0
 *
 * Match Type Weight:
 *   - EXACT Match:        +1.00
 *   - PHRASE Match:       +1.25
 *   - MULTI-KEYWORD:      +1.15
 *   - FUZZY Match:        +0.80
 *
 * Distance Penalty:
 *   - Penalty = (Edit Distance != null) ? (Edit Distance * 0.20) : 0.0
 *
 * Final Relevance Score = (Base Frequency Score * 2.0) + Match Type Weight - Distance Penalty
 * </pre>
 */
@Component
public class RelevanceRankingEngine {

    public double calculateScore(int occurrences, int wordCount, String searchType, Integer editDistance) {
        if (occurrences <= 0) {
            return 0.0;
        }

        double normalizedFrequency = (double) occurrences / (Math.max(1, wordCount) + 1.0);
        double frequencyScore = normalizedFrequency * 10.0;

        double matchTypeWeight;
        if ("PHRASE".equalsIgnoreCase(searchType)) {
            matchTypeWeight = 1.25;
        } else if ("MULTI_KEYWORD".equalsIgnoreCase(searchType)) {
            matchTypeWeight = 1.15;
        } else if ("FUZZY".equalsIgnoreCase(searchType)) {
            matchTypeWeight = 0.80;
        } else {
            matchTypeWeight = 1.00; // EXACT default
        }

        double distancePenalty = 0.0;
        if (editDistance != null && editDistance > 0) {
            distancePenalty = editDistance * 0.20;
        }

        double finalScore = (frequencyScore * 2.0) + matchTypeWeight - distancePenalty;
        return Math.max(0.01, Math.round(finalScore * 1000.0) / 1000.0);
    }

    /**
     * Extracts a clean contextual snippet containing the matched keyword or nearby content.
     */
    public String extractSnippet(String content, List<Integer> matchPositions, int patternLen) {
        if (content == null || content.isEmpty()) {
            return "";
        }
        if (matchPositions == null || matchPositions.isEmpty()) {
            return content.substring(0, Math.min(160, content.length())) + "...";
        }

        int firstPos = matchPositions.get(0);
        int start = Math.max(0, firstPos - 40);
        int end = Math.min(content.length(), firstPos + patternLen + 60);

        String prefix = (start > 0) ? "..." : "";
        String suffix = (end < content.length()) ? "..." : "";

        return prefix + content.substring(start, end).replace('\n', ' ') + suffix;
    }

    /**
     * Sorts matching results by score in descending order.
     */
    public void rankResults(List<SearchResultDto> results) {
        if (results != null && results.size() > 1) {
            Collections.sort(results);
        }
    }
}
