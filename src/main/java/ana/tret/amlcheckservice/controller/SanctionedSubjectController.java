package ana.tret.amlcheckservice.controller;

import ana.tret.amlcheckservice.dto.sanctionedsubject.SanctionedSubjectRequest;
import ana.tret.amlcheckservice.dto.sanctionedsubject.SanctionedSubjectResponse;
import ana.tret.amlcheckservice.service.SanctionedSubjectService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sanctioned-subject")
public class SanctionedSubjectController {

    private final SanctionedSubjectService service;

    @PostMapping
    @ResponseStatus(CREATED)
    public SanctionedSubjectResponse addSubject(SanctionedSubjectRequest subjectRequest) {
        return service.add(subjectRequest);
    }

    @PutMapping
    @ResponseStatus(OK)
    public SanctionedSubjectResponse updateSubject(@PathParam("id") Long id, SanctionedSubjectRequest subjectRequest) {
        return service.update(id, subjectRequest);
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    public void deleteSubject(@PathParam("id") Long id) {
        service.delete(id);
    }
}
