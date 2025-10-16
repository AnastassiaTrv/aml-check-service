package ana.tret.amlcheckservice.service;

import ana.tret.amlcheckservice.dto.sanctionedsubject.SubjectMatchDto;
import ana.tret.amlcheckservice.dto.amlcheck.AmlCheckRequest;
import ana.tret.amlcheckservice.dto.amlcheck.AmlCheckResponse;
import ana.tret.amlcheckservice.repository.sanctionedsubject.SanctionedSubjectJdbcRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ana.tret.amlcheckservice.utility.JwHelper.calculateJaroWinklerScore;
import static ana.tret.amlcheckservice.utility.NormalizeHelper.toNormalized;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AmlCheckService {

    private final SanctionedSubjectJdbcRepo repo;

    private static final int CANDIDATE_LIMIT = 5;
    private static final double FULL_MATCH_RATE = 1.0;

    public AmlCheckResponse performAmlScreening(AmlCheckRequest request) {
        String normalizedInput = toNormalized(request.fullName());
        List<SubjectMatchDto> candidates = repo.preselectOrderedBySimilarity(normalizedInput, CANDIDATE_LIMIT);

        if (isNotEmpty(candidates)) {
            SubjectMatchDto top = candidates.getFirst();
            if (top.rate() == FULL_MATCH_RATE) {
                return AmlCheckResponse.fromSubjectMatch(top, true); // early return if similarity is MAX, no further processing is needed
            }
            double score = calculateJaroWinklerScore(normalizedInput, top.normalizedName());
            return new AmlCheckResponse(top.fullName(), score, score >= getThreshold(normalizedInput));
        } else {
            return AmlCheckResponse.fromZeroMatch();
        }
    }

    private static double getThreshold(String input) {
        int length = input.length();
        if (length <= 4) return 0.95;
        if (length <= 7) return 0.9;
        if (length <= 10) return 0.86;
        if (length <= 14) return 0.82;
        return 0.8;
    }

}
