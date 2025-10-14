package ana.tret.amlcheckservice.service;

import ana.tret.amlcheckservice.dto.SanctionedSubjectRequest;
import ana.tret.amlcheckservice.dto.SanctionedSubjectResponse;
import ana.tret.amlcheckservice.repository.sanctionedsubject.SanctionedSubject;
import ana.tret.amlcheckservice.repository.sanctionedsubject.SanctionedSubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ana.tret.amlcheckservice.dto.SanctionedSubjectResponse.fromEntity;
import static ana.tret.amlcheckservice.exception.RecordNotFoundException.notFoundById;
import static ana.tret.amlcheckservice.service.NormalizeHelper.toNormalized;

@Service
@RequiredArgsConstructor
@Slf4j
public class SanctionedSubjectService {

    private final SanctionedSubjectRepository repository;

    @Transactional
    public SanctionedSubjectResponse add(SanctionedSubjectRequest request) {
        String fullName = request.fullName();
        String normalizedName = toNormalized(fullName);
        SanctionedSubject subject = repository.findSanctionedSubjectByNormalizedName(normalizedName)
                .orElseGet(() -> createSanctionedSubject(fullName, normalizedName));

        return fromEntity(subject);
    }

    @Transactional
    public SanctionedSubjectResponse update(Long id, SanctionedSubjectRequest request) {
        SanctionedSubject subject = repository.findById(id)
                .orElseThrow(() -> notFoundById(id));

        subject.setFullName(request.fullName())
                .setNormalizedName(request.fullName().toLowerCase());

        return fromEntity(repository.save(subject));
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private SanctionedSubject createSanctionedSubject(String fullName, String normalizedName) {
        return repository.save(new SanctionedSubject()
                .setFullName(fullName)
                .setNormalizedName(normalizedName));
    }

}
