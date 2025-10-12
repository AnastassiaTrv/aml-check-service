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

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SanctionedSubjectService {

    private final SanctionedSubjectRepository repository;

    public SanctionedSubjectResponse add(SanctionedSubjectRequest request) {
        //toDo idempotent!
        SanctionedSubject subject = new SanctionedSubject()
                .setFullName(request.fullName())
                .setNormalizedName(request.fullName().toLowerCase());

        return fromEntity(repository.save(subject));
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

}
