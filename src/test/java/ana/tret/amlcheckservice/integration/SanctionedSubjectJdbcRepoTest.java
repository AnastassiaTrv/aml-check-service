package ana.tret.amlcheckservice.integration;

import ana.tret.amlcheckservice.dto.sanctionedsubject.SubjectMatchDto;
import ana.tret.amlcheckservice.exception.DuplicateRecordException;
import ana.tret.amlcheckservice.exception.RecordNotFoundException;
import ana.tret.amlcheckservice.repository.SanctionedSubjectJdbcRepo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ana.tret.amlcheckservice.TestcontainersConfiguration;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
public class SanctionedSubjectJdbcRepoTest {

    @Autowired
    private SanctionedSubjectJdbcRepo repository;

    @Test
    @Transactional
    void insert_shouldInsertRecordAndReturnInserted() {
        var before = OffsetDateTime.now(UTC);
        var inserted = repository.insert("Bob Marley", "marley bob");

        assertThat(inserted).isNotNull();
        assertThat(inserted.createdAt()).isAfterOrEqualTo(before);
        assertThat(inserted.updatedAt()).isAfterOrEqualTo(before);
    }

    @Test
    @Transactional
    void insert_shouldThrowDuplicateRecordException_ifSameNameExists() {
        assertThrows(DuplicateRecordException.class, () -> repository.insert("Osama Bin Laden", "bin laden osama"));
    }

    @Test
    @Transactional
    void preselectOrderedBySimilarity_shouldReturnListOrderedBySimilarity() {
        List<SubjectMatchDto> preselected = repository.preselectOrderedBySimilarity("bin laden osama", 3);

        assertThat(preselected).hasSize(3);
        assertThat(preselected.get(0).fullName()).isEqualTo("Osama Bin Laden");
        assertThat(preselected.get(1).fullName()).isEqualTo("Osomana Bing");
        assertThat(preselected.get(2).fullName()).isEqualTo("Ladon Benn");
    }

    @Test
    @Transactional
    void update_shouldUpdateRecordAndReturnUpdated() {
        var fullName = "Michael Jackson";
        var normalized = "jackson michael";
        var before = OffsetDateTime.now(UTC);
        var updated = repository.update(1L, fullName, normalized, 0L);

        assertThat(updated.fullName()).isEqualTo(fullName);
        assertThat(updated.normalizedName()).isEqualTo(normalized);
        assertThat(updated.updatedAt()).isAfter(before);
        assertThat(updated.createdAt()).isBefore(before);
    }

    @Test
    @Transactional
    void update_shouldThrowRecordNotFoundException_ifNotExistsById() {
        var fullName = "Michael Jackson";
        var normalized = "jackson michael";
        assertThrows(RecordNotFoundException.class, () -> repository.update(100L, fullName, normalized, 0L));
    }

    @Test
    @Transactional
    void update_shouldThrowOptimisticLockingFailureException_ifVersionIsWrong() {
        var fullName = "Michael Jackson";
        var normalized = "jackson michael";
        assertThrows(OptimisticLockingFailureException.class, () -> repository.update(1L, fullName, normalized, 1L));
    }

    @Test
    @Transactional
    void update_shouldThrowDuplicateRecordException_ifSameNameAlreadyExists() {
        var fullName = "Anastassia Tret";
        var normalized = "anastassia tret";
        assertThrows(DuplicateRecordException.class, () -> repository.update(1L, fullName, normalized, 0L));
    }

    @Test
    @Transactional
    void existsById_shouldReturnTrue_ifIdExists() {
        assertThat(repository.existsById(1L)).isTrue();
    }

    @Test
    @Transactional
    void existsById_shouldReturnFalse_ifIdNotExist() {
        assertThat(repository.existsById(100L)).isFalse();
    }

    @Test
    @Transactional
    void deleteById_shouldDeleteExistingRecord() {
        assertThat(repository.existsById(1L)).isTrue(); // precondition
        repository.deleteById(1L);
        assertThat(repository.existsById(1L)).isFalse();
    }
}
