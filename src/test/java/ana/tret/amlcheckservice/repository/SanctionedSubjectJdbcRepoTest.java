package ana.tret.amlcheckservice.repository;

import ana.tret.amlcheckservice.domain.SubjectMatch;
import ana.tret.amlcheckservice.exception.DuplicateRecordException;
import ana.tret.amlcheckservice.exception.RecordNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SanctionedSubjectJdbcRepoTest extends IntegrationBaseTest {

    @Autowired
    private SanctionedSubjectJdbcRepo repository;

    private static final String FULL_NAME_NEW = "Michael Jackson";
    private static final String NORMALIZED_NEW = "jackson michael";

    @Test
    void insert_shouldInsertRecordAndReturnInserted() {
        var inserted = repository.insert(FULL_NAME_NEW, NORMALIZED_NEW);
        assertThat(inserted).isNotNull();
        assertThat(inserted.fullName()).isEqualTo(FULL_NAME_NEW);
        assertThat(inserted.normalizedName()).isEqualTo(NORMALIZED_NEW);
    }

    @Test
    void insert_shouldThrowDuplicateRecordException_ifSameNameExists() {
        assertThrows(DuplicateRecordException.class, () -> repository.insert("Osama Bin Laden", "bin laden osama"));
    }

    @Test
    void preselectOrderedBySimilarity_shouldReturnListOrderedBySimilarity() {
        List<SubjectMatch> preselected = repository.preselectOrderedBySimilarity("bin laden osama", 3);
        assertThat(preselected).hasSize(3);
        assertThat(preselected.get(0).fullName()).isEqualTo("Osama Bin Laden");
        assertThat(preselected.get(1).fullName()).isEqualTo("Osomana Bing");
        assertThat(preselected.get(2).fullName()).isEqualTo("Ladon Benn");
    }

    @Test
    void update_shouldUpdateRecordAndIncreaseVersion() {
        var updated = repository.update(1L, FULL_NAME_NEW, NORMALIZED_NEW, 0L);
        assertThat(updated.fullName()).isEqualTo(FULL_NAME_NEW);
        assertThat(updated.normalizedName()).isEqualTo(NORMALIZED_NEW);
        assertThat(updated.version()).isEqualTo(1L);
    }

    @Test
    void update_shouldThrowRecordNotFoundException_ifNotExistsById() {
        assertThrows(RecordNotFoundException.class,
                () -> repository.update(100L, FULL_NAME_NEW, NORMALIZED_NEW, 0L));
    }

    @Test
    void update_shouldThrowOptimisticLockingFailureException_ifVersionIsWrong() {
        assertThrows(OptimisticLockingFailureException.class,
                () -> repository.update(1L, FULL_NAME_NEW, NORMALIZED_NEW, 1L));
    }

    @Test
    void update_shouldThrowDuplicateRecordException_ifSameNameAlreadyExists() {
        assertThrows(DuplicateRecordException.class,
                () -> repository.update(1L, "Anastassia Tret", "anastassia tret", 0L));
    }

    @Test
    void existsById_shouldReturnTrue_ifIdExists() {
        assertThat(repository.existsById(1L)).isTrue();
    }

    @Test
    void existsById_shouldReturnFalse_ifIdNotExist() {
        assertThat(repository.existsById(100L)).isFalse();
    }

    @Test
    void deleteById_shouldDeleteExistingRecord() {
        assertThat(repository.existsById(1L)).isTrue(); // precondition
        repository.deleteById(1L);
        assertThat(repository.existsById(1L)).isFalse();
    }
}
