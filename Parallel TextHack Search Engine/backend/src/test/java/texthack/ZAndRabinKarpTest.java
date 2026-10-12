package texthack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import texthack.strings.MatchResult;
import texthack.strings.RabinKarpMatcher;
import texthack.strings.ZAlgorithmMatcher;

import static org.assertj.core.api.Assertions.assertThat;

public class ZAndRabinKarpTest {

    private final ZAlgorithmMatcher zAlgo = new ZAlgorithmMatcher();
    private final RabinKarpMatcher rabinKarp = new RabinKarpMatcher();

    @Test
    @DisplayName("Z-Algorithm: Pattern matching correctness")
    void testZAlgorithm() {
        String text = "abracadabra";
        MatchResult result = zAlgo.search(text, "abra");
        assertThat(result.getCount()).isEqualTo(2);
        assertThat(result.getMatchIndices()).containsExactly(0, 7);
    }

    @Test
    @DisplayName("Rabin-Karp: Rolling double-hash pattern match")
    void testRabinKarp() {
        String text = "Knuth-Morris-Pratt and Rabin-Karp are linear-time string algorithms.";
        MatchResult res = rabinKarp.search(text, "Rabin-Karp");
        assertThat(res.getCount()).isEqualTo(1);
        assertThat(res.getMatchIndices()).containsExactly(23);
    }

    @Test
    @DisplayName("Rabin-Karp: Overlapping repetitive sequences")
    void testRabinKarpOverlapping() {
        String text = "abababab";
        MatchResult res = rabinKarp.search(text, "aba");
        assertThat(res.getCount()).isEqualTo(3);
        assertThat(res.getMatchIndices()).containsExactly(0, 2, 4);
    }
}
