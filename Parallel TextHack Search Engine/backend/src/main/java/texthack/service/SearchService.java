package texthack.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import texthack.dto.DocumentDto;
import texthack.dto.SearchRequestDto;
import texthack.dto.SearchResponseDto;
import texthack.dto.SearchResultDto;
import texthack.model.DocumentEntity;
import texthack.model.SearchQueryEntity;
import texthack.model.SearchResultEntity;
import texthack.parallel.ParallelSearchEngine;
import texthack.parallel.SequentialSearchEngine;
import texthack.repository.DocumentRepository;
import texthack.repository.SearchQueryRepository;
import texthack.repository.SearchResultRepository;

import java.util.List;

@Service
public class SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchService.class);

    private final DocumentService documentService;
    private final SequentialSearchEngine sequentialEngine;
    private final ParallelSearchEngine parallelEngine;
    private final SearchQueryRepository searchQueryRepository;
    private final SearchResultRepository searchResultRepository;
    private final DocumentRepository documentRepository;

    public SearchService(DocumentService documentService,
                         SequentialSearchEngine sequentialEngine,
                         ParallelSearchEngine parallelEngine,
                         SearchQueryRepository searchQueryRepository,
                         SearchResultRepository searchResultRepository,
                         DocumentRepository documentRepository) {
        this.documentService = documentService;
        this.sequentialEngine = sequentialEngine;
        this.parallelEngine = parallelEngine;
        this.searchQueryRepository = searchQueryRepository;
        this.searchResultRepository = searchResultRepository;
        this.documentRepository = documentRepository;
    }

    public SearchResponseDto search(SearchRequestDto request) {
        List<DocumentDto> corpus = documentService.getAllDocuments();
        String mode = (request.getMode() != null) ? request.getMode().toUpperCase() : "PARALLEL";

        SearchResponseDto response;

        if ("SEQUENTIAL".equalsIgnoreCase(mode)) {
            response = sequentialEngine.executeSearch(corpus, request);
        } else {
            // First run sequential to determine empirical baseline T_1 for accurate speedup measurement
            SearchResponseDto seqBaseline = sequentialEngine.executeSearch(corpus, request);
            response = parallelEngine.executeSearch(corpus, request, seqBaseline.getExecutionTimeMs());
        }

        // Asynchronously or safely persist query audit trail
        try {
            saveQueryAudit(request, response);
        } catch (Exception e) {
            log.warn("Could not save query audit to database: {}", e.getMessage());
        }

        return response;
    }

    @Transactional
    public void saveQueryAudit(SearchRequestDto request, SearchResponseDto response) {
        SearchQueryEntity queryEntity = new SearchQueryEntity(
                request.getQuery(),
                request.getSearchType(),
                response.getMode(),
                response.getThreads()
        );
        SearchQueryEntity savedQuery = searchQueryRepository.save(queryEntity);

        if (response.getResults() != null) {
            int savedCount = 0;
            for (SearchResultDto item : response.getResults()) {
                if (savedCount++ >= 15) break; // Keep top 15 in database audit
                if (item.getDocumentId() != null) {
                    documentRepository.findById(item.getDocumentId()).ifPresent(doc -> {
                        SearchResultEntity resEntity = new SearchResultEntity(
                                savedQuery,
                                doc,
                                item.getOccurrenceCount(),
                                item.getScore(),
                                item.getEditDistance()
                        );
                        searchResultRepository.save(resEntity);
                    });
                }
            }
        }
    }
}
