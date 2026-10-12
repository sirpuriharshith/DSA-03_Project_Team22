package texthack.strings;

/**
 * Common contract for single-pattern handcrafted string matching algorithms.
 */
public interface StringMatcher {

    /**
     * Searches for all occurrences of pattern within text without using built-in search functions.
     *
     * @param text    document text (size n)
     * @param pattern search keyword / phrase (size m)
     * @return MatchResult containing occurrence count, positions, and execution time
     */
    MatchResult search(String text, String pattern);

    /**
     * Algorithm identifier name.
     */
    String getAlgorithmName();
}
