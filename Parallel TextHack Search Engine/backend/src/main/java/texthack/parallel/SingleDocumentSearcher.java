package texthack.parallel;

import org.springframework.stereotype.Component;
import texthack.dp.DamerauLevenshteinDistance;
import texthack.dp.LevenshteinDistance;
import texthack.dto.DocumentDto;
import texthack.dto.SearchRequestDto;
import texthack.dto.SearchResultDto;
import texthack.strings.*;

import java.util.*;

/**
 * Executes search over a single document using purely handcrafted algorithms.
 */
@Component
public class SingleDocumentSearcher {

    private final KmpMatcher kmpMatcher;
    private final ZAlgorithmMatcher zMatcher;
    private final RabinKarpMatcher rabinKarpMatcher;
    private final NaiveStringMatcher naiveMatcher;
    private final AhoCorasickMatcher ahoCorasickMatcher;
    private final LevenshteinDistance levenshteinDistance;
    private final DamerauLevenshteinDistance damerauLevenshteinDistance;
    private final RelevanceRankingEngine rankingEngine;

    public SingleDocumentSearcher(KmpMatcher kmpMatcher,
                                  ZAlgorithmMatcher zMatcher,
                                  RabinKarpMatcher rabinKarpMatcher,
                                  NaiveStringMatcher naiveMatcher,
                                  AhoCorasickMatcher ahoCorasickMatcher,
                                  LevenshteinDistance levenshteinDistance,
                                  DamerauLevenshteinDistance damerauLevenshteinDistance,
                                  RelevanceRankingEngine rankingEngine) {
        this.kmpMatcher = kmpMatcher;
        this.zMatcher = zMatcher;
        this.rabinKarpMatcher = rabinKarpMatcher;
        this.naiveMatcher = naiveMatcher;
        this.ahoCorasickMatcher = ahoCorasickMatcher;
        this.levenshteinDistance = levenshteinDistance;
        this.damerauLevenshteinDistance = damerauLevenshteinDistance;
        this.rankingEngine = rankingEngine;
    }

    public Optional<SearchResultDto> searchDocument(DocumentDto doc, SearchRequestDto request) {
        if (doc == null || doc.getContent() == null || doc.getContent().isEmpty()) {
            return Optional.empty();
        }

        String searchType = (request.getSearchType() != null) ? request.getSearchType().toUpperCase() : "EXACT";
        String query = request.getQuery();
        String content = doc.getContent();

        // Case-insensitive search: compare lowercase representations
        String lowerContent = content.toLowerCase();
        String lowerQuery = query.toLowerCase();

        switch (searchType) {
            case "EXACT":
            case "PHRASE":
                return searchExactOrPhrase(doc, lowerContent, lowerQuery, searchType, request.getAlgorithm());

            case "MULTI_KEYWORD":
                return searchMultiKeyword(doc, lowerContent, lowerQuery);

            case "FUZZY":
                int maxDist = (request.getMaxEditDistance() != null) ? request.getMaxEditDistance() : 2;
                return searchFuzzy(doc, lowerContent, lowerQuery, maxDist);

            default:
                return searchExactOrPhrase(doc, lowerContent, lowerQuery, "EXACT", request.getAlgorithm());
        }
    }

    private Optional<SearchResultDto> searchExactOrPhrase(DocumentDto doc, String content, String query, String searchType, String algoName) {
        StringMatcher matcher = selectMatcher(algoName);
        MatchResult result = matcher.search(content, query);

        if (!result.hasMatch()) {
            return Optional.empty();
        }

        double score = rankingEngine.calculateScore(result.getCount(), doc.getWordCount(), searchType, 0);
        String snippet = rankingEngine.extractSnippet(doc.getContent(), result.getMatchIndices(), query.length());

        return Optional.of(new SearchResultDto(
                doc.getId(),
                doc.getFileName(),
                doc.getFileType(),
                result.getCount(),
                score,
                0,
                snippet,
                result.getMatchIndices()
        ));
    }

    private Optional<SearchResultDto> searchMultiKeyword(DocumentDto doc, String content, String query) {
        // Tokenize query into distinct keywords without regex
        List<String> keywords = extractTokens(query);
        if (keywords.isEmpty()) {
            return Optional.empty();
        }

        Map<String, List<Integer>> matches = ahoCorasickMatcher.searchMultiple(content, keywords);
        int totalOccurrences = 0;
        List<Integer> allPositions = new ArrayList<>();

        for (List<Integer> positions : matches.values()) {
            totalOccurrences += positions.size();
            allPositions.addAll(positions);
        }

        if (totalOccurrences == 0) {
            return Optional.empty();
        }

        Collections.sort(allPositions);
        double score = rankingEngine.calculateScore(totalOccurrences, doc.getWordCount(), "MULTI_KEYWORD", 0);
        String snippet = rankingEngine.extractSnippet(doc.getContent(), allPositions, keywords.get(0).length());

        return Optional.of(new SearchResultDto(
                doc.getId(),
                doc.getFileName(),
                doc.getFileType(),
                totalOccurrences,
                score,
                0,
                snippet,
                allPositions
        ));
    }

    private Optional<SearchResultDto> searchFuzzy(DocumentDto doc, String content, String query, int maxEditDistance) {
        List<String> tokens = extractTokens(content);
        int occurrences = 0;
        int minDistance = Integer.MAX_VALUE;
        List<Integer> matchIndices = new ArrayList<>();

        // Check each token using Damerau-Levenshtein
        for (String token : tokens) {
            // Optimization: skip tokens whose length diff is greater than maxEditDistance
            if (Math.abs(token.length() - query.length()) > maxEditDistance) {
                continue;
            }

            int dist = damerauLevenshteinDistance.computeDistance(token, query);
            if (dist <= maxEditDistance) {
                occurrences++;
                if (dist < minDistance) {
                    minDistance = dist;
                }
            }
        }

        if (occurrences == 0) {
            return Optional.empty();
        }

        double score = rankingEngine.calculateScore(occurrences, doc.getWordCount(), "FUZZY", minDistance);
        String snippet = rankingEngine.extractSnippet(doc.getContent(), matchIndices, query.length());

        return Optional.of(new SearchResultDto(
                doc.getId(),
                doc.getFileName(),
                doc.getFileType(),
                occurrences,
                score,
                minDistance,
                snippet,
                matchIndices
        ));
    }

    private StringMatcher selectMatcher(String algoName) {
        if ("Z_ALGORITHM".equalsIgnoreCase(algoName)) {
            return zMatcher;
        } else if ("RABIN_KARP".equalsIgnoreCase(algoName)) {
            return rabinKarpMatcher;
        } else if ("NAIVE".equalsIgnoreCase(algoName)) {
            return naiveMatcher;
        }
        return kmpMatcher; // Default
    }

    private List<String> extractTokens(String text) {
        List<String> tokens = new ArrayList<>();
        int len = text.length();
        int start = -1;

        for (int i = 0; i < len; i++) {
            char c = text.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                if (start == -1) start = i;
            } else {
                if (start != -1) {
                    tokens.add(text.substring(start, i));
                    start = -1;
                }
            }
        }
        if (start != -1) {
            tokens.add(text.substring(start, len));
        }
        return tokens;
    }
}
