package ana.tret.amlcheckservice.repository.sanctionedsubject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SanctionedSubjectRepository extends JpaRepository<SanctionedSubject, Long> {

    Optional<SanctionedSubject> findSanctionedSubjectByNormalizedName(String normalizedName);

    @Query(value = """
        SELECT *
        FROM sanctioned_subject
        WHERE normalized_name % :normalized AND similarity(normalized_name, :normalized) >= :rate
        ORDER BY similarity(normalized_name, :normalized) DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<SanctionedSubject> preselectCandidatesBySimilarityRate(String normalized, int limit, double rate);
}