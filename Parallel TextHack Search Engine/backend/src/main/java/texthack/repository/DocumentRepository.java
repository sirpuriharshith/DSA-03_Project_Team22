package texthack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import texthack.model.DocumentEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {

    Optional<DocumentEntity> findByFileName(String fileName);

    List<DocumentEntity> findByFileType(String fileType);

    @Query("SELECT COUNT(d) FROM DocumentEntity d")
    long countTotalDocuments();

    @Query("SELECT COALESCE(SUM(d.fileSize), 0) FROM DocumentEntity d")
    long sumTotalFileSizeBytes();

    @Query("SELECT COALESCE(SUM(d.wordCount), 0) FROM DocumentEntity d")
    long sumTotalWordCount();
}
