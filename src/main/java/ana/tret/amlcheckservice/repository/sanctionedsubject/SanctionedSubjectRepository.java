package ana.tret.amlcheckservice.repository.sanctionedsubject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SanctionedSubjectRepository extends JpaRepository<SanctionedSubject, Long> {

    Optional<SanctionedSubject> findSanctionedSubjectByNormalizedName(String normalizedName);

}