package texthack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import texthack.dp.DamerauLevenshteinDistance;
import texthack.dp.LevenshteinDistance;
import texthack.dp.SequenceAlignment;

import static org.assertj.core.api.Assertions.assertThat;

public class DynamicProgrammingTest {

    private final LevenshteinDistance levenshtein = new LevenshteinDistance();
    private final DamerauLevenshteinDistance damerau = new DamerauLevenshteinDistance();
    private final SequenceAlignment seqAlign = new SequenceAlignment();

    @Test
    @DisplayName("Levenshtein: Wagner-Fischer edit distance")
    void testLevenshtein() {
        assertThat(levenshtein.computeDistance("kitten", "sitting")).isEqualTo(3);
        assertThat(levenshtein.computeDistance("algorithm", "algoritm")).isEqualTo(1); // 1 deletion
        assertThat(levenshtein.computeDistance("", "search")).isEqualTo(6);
        assertThat(levenshtein.computeDistance("parallel", "parallel")).isEqualTo(0);
    }

    @Test
    @DisplayName("Damerau-Levenshtein: Transposition detection")
    void testDamerauLevenshtein() {
        // "ca" -> "ac" is 1 transposition in Damerau, but 2 edits in Levenshtein
        assertThat(damerau.computeDistance("ca", "ac")).isEqualTo(1);
        assertThat(levenshtein.computeDistance("ca", "ac")).isEqualTo(2);

        // Common typing swap
        assertThat(damerau.computeDistance("recieved", "received")).isEqualTo(1);
    }

    @Test
    @DisplayName("Needleman-Wunsch: Global sequence alignment")
    void testNeedlemanWunsch() {
        SequenceAlignment.AlignmentResult res = seqAlign.needlemanWunsch("GCATGCU", "GATTACA", 2, 1, 1);
        assertThat(res.score).isGreaterThan(0);
        assertThat(res.alignmentType).isEqualTo("NEEDLEMAN_WUNSCH");
    }
}
