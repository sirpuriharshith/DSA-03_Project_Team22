package texthack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import texthack.model.SearchResultEntity;

import java.util.List;

@Repository
public interface SearchResultRepository extends JpaRepository<SearchResultEntity, Long> {
    List<SearchResultEntity> findByQueryIdOrderByScoreDesc(Long queryId);
}
