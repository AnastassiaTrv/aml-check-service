package ana.tret.amlcheckservice.service;

import ana.tret.amlcheckservice.dto.amlcheck.AmlCheckRequest;
import ana.tret.amlcheckservice.dto.amlcheck.AmlCheckResponse;
import ana.tret.amlcheckservice.repository.sanctionedsubject.SanctionedSubject;
import ana.tret.amlcheckservice.repository.sanctionedsubject.SanctionedSubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ana.tret.amlcheckservice.service.NormalizeHelper.toNormalized;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AmlCheckService {

    private final SanctionedSubjectRepository repository;

    private static final int CANDIDATE_LIMIT = 5;
    private static final double INITIAL_SIMILARITY_RATE = 0.15;

    public AmlCheckResponse performAmlScreening(AmlCheckRequest request) {
        String normalizedInput = toNormalized(request.fullName());
        List<SanctionedSubject> candidates = repository.preselectCandidatesBySimilarityRate(normalizedInput, CANDIDATE_LIMIT, INITIAL_SIMILARITY_RATE);
        return new AmlCheckResponse("None", "LOW", false);
    }

}
