package texthack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import texthack.strings.KmpMatcher;
import texthack.strings.MatchResult;

import static org.assertj.core.api.Assertions.assertThat;

public class KmpMatcherTest {

    private final KmpMatcher kmp = new KmpMatcher();

    @Test
    @DisplayName("KMP: Normal pattern match in text")
    void testNormalMatch() {
        String text = "The algorithm search engine executes an advanced algorithm.";
        MatchResult result = kmp.search(text, "algorithm");
        assertThat(result.getCount()).isEqualTo(2);
        assertThat(result.getMatchIndices()).containsExactly(4, 49);
    }

    @Test
    @DisplayName("KMP: Pattern not found in text")
    void testPatternNotFound() {
        String text = "Quick brown fox jumps over the lazy dog";
        MatchResult result = kmp.search(text, "elephant");
        assertThat(result.hasMatch()).isFalse();
        assertThat(result.getCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("KMP: Pattern equal to entire text")
    void testPatternEqualToText() {
        String text = "exactmatch";
        MatchResult result = kmp.search(text, "exactmatch");
        assertThat(result.getCount()).isEqualTo(1);
        assertThat(result.getMatchIndices()).containsExactly(0);
    }

    @Test
    @DisplayName("KMP: Empty or null inputs")
    void testEmptyAndNull() {
        assertThat(kmp.search("", "pattern").getCount()).isEqualTo(0);
        assertThat(kmp.search("text", "").getCount()).isEqualTo(0);
        assertThat(kmp.search(null, "pattern").getCount()).isEqualTo(0);
        assertThat(kmp.search("text", null).getCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("KMP: Unicode and multi-language support")
    void testUnicode() {
        String text = "Algorithmus und parallele Suche — 数据结构与算法 — جستجو";
        MatchResult result = kmp.search(text, "算法");
        assertThat(result.getCount()).isEqualTo(1);
        assertThat(result.hasMatch()).isTrue();
    }

    @Test
    @DisplayName("KMP: Repetitive overlapping pattern")
    void testOverlappingPattern() {
        String text = "AAAAAA";
        MatchResult result = kmp.search(text, "AAA");
        assertThat(result.getCount()).isEqualTo(4);
        assertThat(result.getMatchIndices()).containsExactly(0, 1, 2, 3);
    }
}
