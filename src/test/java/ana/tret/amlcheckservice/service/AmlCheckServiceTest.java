package ana.tret.amlcheckservice.service;

import ana.tret.amlcheckservice.dto.amlcheck.AmlCheckRequest;
import ana.tret.amlcheckservice.dto.amlcheck.AmlCheckResponse;
import ana.tret.amlcheckservice.domain.SubjectMatch;
import ana.tret.amlcheckservice.repository.SanctionedSubjectJdbcRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static ana.tret.amlcheckservice.dto.amlcheck.MatchType.POSITIVE;
import static ana.tret.amlcheckservice.dto.amlcheck.MatchType.SUSPICIOUS;
import static ana.tret.amlcheckservice.dto.amlcheck.MatchType.ZERO;
import static ana.tret.amlcheckservice.service.AmlCheckService.CANDIDATE_LIMIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AmlCheckServiceTest {

    @Mock
    private SanctionedSubjectJdbcRepo repo;

    @InjectMocks
    private AmlCheckService service;

    private static final String FULL_NAME = "Osama Bin Laden";
    private static final String NORMALIZED = "bin laden osama";

    @Test
    void performAmlScreening_shouldReturnPositiveMatch() {
        SubjectMatch dto = new SubjectMatch(1L, FULL_NAME, NORMALIZED, 1.0);
        when(repo.preselectOrderedBySimilarity(NORMALIZED, CANDIDATE_LIMIT)).thenReturn(List.of(dto));

        AmlCheckResponse result = service.performAmlScreening(new AmlCheckRequest(FULL_NAME));
        assertThat(result.score()).isEqualTo(1.0);
        assertThat(result.match()).isTrue();
        assertThat(result.bestMatch()).isEqualTo(FULL_NAME);
        assertThat(result.type()).isEqualTo(POSITIVE);
    }

    @Test
    void performAmlScreening_shouldReturnSuspiciousMatch() {
        String inputName = "Ladn the Asoma";
        String inputNormalized = "asoma ladn";

        SubjectMatch dto = new SubjectMatch(1L, FULL_NAME, NORMALIZED, 0.73);
        when(repo.preselectOrderedBySimilarity(inputNormalized, CANDIDATE_LIMIT)).thenReturn(List.of(dto));

        AmlCheckResponse result = service.performAmlScreening(new AmlCheckRequest(inputName));
        assertThat(result.score()).isEqualTo(0.73);
        assertThat(result.match()).isFalse();
        assertThat(result.bestMatch()).isEqualTo(FULL_NAME);
        assertThat(result.type()).isEqualTo(SUSPICIOUS);
    }

    @Test
    void performAmlScreening_shouldReturnZeroMatch() {
        String inputName = "Ana De Jamona";
        String inputNormalized = "ana de jamona";

        SubjectMatch dto = new SubjectMatch(1L, FULL_NAME, NORMALIZED, 0.0);
        when(repo.preselectOrderedBySimilarity(inputNormalized, CANDIDATE_LIMIT)).thenReturn(List.of(dto));

        AmlCheckResponse result = service.performAmlScreening(new AmlCheckRequest(inputName));
        assertThat(result.score()).isEqualTo(0.0);
        assertThat(result.match()).isFalse();
        assertThat(result.bestMatch()).isNull();
        assertThat(result.type()).isEqualTo(ZERO);
    }

    @Test
    void performAmlScreening_shouldReturnZeroMatch_ifPreselectIsEmpty() {
        String inputName = "Ana De Jamona";
        String inputNormalized = "ana de jamona";

        when(repo.preselectOrderedBySimilarity(inputNormalized, CANDIDATE_LIMIT)).thenReturn(List.of());

        AmlCheckResponse result = service.performAmlScreening(new AmlCheckRequest(inputName));
        assertThat(result.score()).isEqualTo(0.0);
        assertThat(result.match()).isFalse();
        assertThat(result.bestMatch()).isNull();
        assertThat(result.type()).isEqualTo(ZERO);
    }

}
