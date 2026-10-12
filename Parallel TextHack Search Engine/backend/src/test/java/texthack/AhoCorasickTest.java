package texthack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import texthack.strings.AhoCorasickMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AhoCorasickTest {

    private final AhoCorasickMatcher ahoCorasick = new AhoCorasickMatcher();

    @Test
    @DisplayName("Aho-Corasick: Multi-keyword simultaneous match")
    void testMultiPatternSearch() {
        String text = "ushers";
        List<String> patterns = Arrays.asList("he", "she", "his", "hers");

        Map<String, List<Integer>> matches = ahoCorasick.searchMultiple(text, patterns);

        assertThat(matches.get("he")).contains(2); // "usHErs"
        assertThat(matches.get("she")).contains(1); // "uSHErs"
        assertThat(matches.get("hers")).contains(2); // "usHERS"
        assertThat(matches.get("his")).isEmpty();
    }
}
