package texthack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import texthack.model.AlgorithmRunEntity;

import java.util.List;

@Repository
public interface AlgorithmRunRepository extends JpaRepository<AlgorithmRunEntity, Long> {
    List<AlgorithmRunEntity> findByAlgorithmNameOrderByCreatedAtDesc(String algorithmName);
}
