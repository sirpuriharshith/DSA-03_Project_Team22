package texthack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import texthack.model.BenchmarkResultEntity;

import java.util.List;

@Repository
public interface BenchmarkResultRepository extends JpaRepository<BenchmarkResultEntity, Long> {
    List<BenchmarkResultEntity> findTop20ByOrderByCreatedAtDesc();
}
