package ana.tret.amlcheckservice.controller;

import ana.tret.amlcheckservice.dto.sanctionedsubject.SanctionedSubjectCreateRequest;
import ana.tret.amlcheckservice.dto.sanctionedsubject.SanctionedSubjectUpdateRequest;
import ana.tret.amlcheckservice.dto.sanctionedsubject.SanctionedSubjectResponse;
import ana.tret.amlcheckservice.service.SanctionedSubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sanctioned-subjects")
public class SanctionedSubjectController {

    private final SanctionedSubjectService service;

    @PostMapping
    @ResponseStatus(CREATED)
    public SanctionedSubjectResponse addSubject(@Valid @RequestBody SanctionedSubjectCreateRequest subjectRequest) {
        return service.add(subjectRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(OK)
    public SanctionedSubjectResponse updateSubject(@PathVariable("id") Long id,
                                                   @Valid @RequestBody SanctionedSubjectUpdateRequest subjectRequest) {
        return service.update(id, subjectRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteSubject(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
