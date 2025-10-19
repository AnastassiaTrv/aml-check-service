package ana.tret.amlcheckservice.service;

import ana.tret.amlcheckservice.domain.SubjectMatch;
import ana.tret.amlcheckservice.dto.amlcheck.AmlCheckRequest;
import ana.tret.amlcheckservice.dto.amlcheck.AmlCheckResponse;
import ana.tret.amlcheckservice.repository.SanctionedSubjectJdbcRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ana.tret.amlcheckservice.dto.amlcheck.MatchType.POSITIVE;
import static ana.tret.amlcheckservice.dto.amlcheck.MatchType.SUSPICIOUS;
import static ana.tret.amlcheckservice.utility.JwHelper.calculateJaroWinklerScore;
import static ana.tret.amlcheckservice.utility.NormalizeHelper.toNormalized;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AmlCheckService {

    private final SanctionedSubjectJdbcRepo repo;

    static final int CANDIDATE_LIMIT = 5;
    static final double CANDIDATE_MAX_RATE = 1.0;
    private static final double POSITIVE_MATCH_THRESHOLD = 0.8;
    private static final double SUSPICIOUS_MATCH_THRESHOLD = 0.7;

    public AmlCheckResponse performAmlScreening(AmlCheckRequest request) {
        final String normalizedInput = toNormalized(request.fullName());
        final List<SubjectMatch> candidates = repo.preselectOrderedBySimilarity(normalizedInput, CANDIDATE_LIMIT);

        if (candidates.isEmpty()) return AmlCheckResponse.fromZeroMatch();

        var best = candidates.getFirst();
        if (best.rate() == CANDIDATE_MAX_RATE) {
            log.info("AML screening result: preselect returned max score for candidate: {}, skipping JW score calculations", best.fullName());
            return AmlCheckResponse.fromSubjectMatch(best, POSITIVE,true);
        }

        double bestScore = calculateJaroWinklerScore(normalizedInput, best.normalizedName());
        for (int i = 1; i < candidates.size(); i++) {
            var candidate = candidates.get(i);
            double score = calculateJaroWinklerScore(normalizedInput, candidate.normalizedName());
            if (score > bestScore) {
                bestScore = score;
                best = candidate;
            }
        }

        if (bestScore < SUSPICIOUS_MATCH_THRESHOLD) return AmlCheckResponse.fromZeroMatch();

        boolean isMatch = bestScore >= POSITIVE_MATCH_THRESHOLD;
        var matchType = isMatch ? POSITIVE : SUSPICIOUS;

        log.info("AML screening result: {} match for input='{}', best='{}', score={}",
                matchType, request.fullName(), best.fullName(), bestScore);

        return new AmlCheckResponse(best.fullName(), matchType, bestScore, isMatch);
    }

}
