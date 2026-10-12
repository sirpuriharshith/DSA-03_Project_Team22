package texthack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import texthack.dto.DocumentDto;
import texthack.dto.SearchRequestDto;
import texthack.dto.SearchResponseDto;
import texthack.dto.SearchResultDto;
import texthack.parallel.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ParallelSearchReductionTest {

    private final TreeStyleParallelReduction reduction = new TreeStyleParallelReduction();

    @Test
    @DisplayName("Parallel Reduction: Tree-style reduction merges partial results without loss")
    void testTreeReduction() {
        SearchResultDto r1 = new SearchResultDto(1L, "doc1.txt", "TXT", 3, 5.0, 0, "snippet1", List.of(1, 5, 9));
        SearchResultDto r2 = new SearchResultDto(2L, "doc2.txt", "TXT", 2, 4.0, 0, "snippet2", List.of(2, 6));
        SearchResultDto r3 = new SearchResultDto(3L, "doc3.txt", "TXT", 1, 3.0, 0, "snippet3", List.of(3));
        SearchResultDto r4 = new SearchResultDto(4L, "doc4.txt", "TXT", 4, 6.0, 0, "snippet4", List.of(4, 8, 12, 16));

        PartialSearchResult p1 = new PartialSearchResult(List.of(r1), 3, 25);
        PartialSearchResult p2 = new PartialSearchResult(List.of(r2), 2, 25);
        PartialSearchResult p3 = new PartialSearchResult(List.of(r3), 1, 25);
        PartialSearchResult p4 = new PartialSearchResult(List.of(r4), 4, 25);

        PartialSearchResult consolidated = reduction.reduce(Arrays.asList(p1, p2, p3, p4));

        assertThat(consolidated.getDocumentsSearched()).isEqualTo(100);
        assertThat(consolidated.getTotalOccurrences()).isEqualTo(10);
        assertThat(consolidated.getResults()).hasSize(4);
    }
}
