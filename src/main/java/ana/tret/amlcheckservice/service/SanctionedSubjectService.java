package ana.tret.amlcheckservice.service;

import ana.tret.amlcheckservice.dto.sanctionedsubject.SanctionedSubjectCreateRequest;
import ana.tret.amlcheckservice.dto.sanctionedsubject.SanctionedSubjectUpdateRequest;
import ana.tret.amlcheckservice.dto.sanctionedsubject.SanctionedSubjectResponse;
import ana.tret.amlcheckservice.repository.SanctionedSubjectJdbcRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ana.tret.amlcheckservice.utility.NormalizeHelper.toNormalized;

@Service
@RequiredArgsConstructor
@Slf4j
public class SanctionedSubjectService {

    private final SanctionedSubjectJdbcRepo repo;

    @Transactional
    public SanctionedSubjectResponse add(SanctionedSubjectCreateRequest request) {
        var fullName = request.fullName();
        var normalizedName = toNormalized(fullName);
        var dto = repo.insert(fullName, normalizedName);
        return SanctionedSubjectResponse.fromDto(dto);
    }

    @Transactional
    public SanctionedSubjectResponse update(Long id, SanctionedSubjectUpdateRequest request) {
        var fullName = request.fullName();
        var updated = repo.update(id, fullName, toNormalized(fullName), request.version());
        return SanctionedSubjectResponse.fromDto(updated);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id); // tolerate non-existing records
    }

}
