package ana.tret.amlcheckservice.controller;

import ana.tret.amlcheckservice.dto.amlcheck.AmlCheckRequest;
import ana.tret.amlcheckservice.dto.amlcheck.AmlCheckResponse;
import ana.tret.amlcheckservice.service.AmlCheckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/aml-checks", produces = MediaType.APPLICATION_JSON_VALUE)
public class AmlCheckController {

    private final AmlCheckService amlCheckService;

    @PostMapping
    @ResponseStatus(OK)
    public AmlCheckResponse check(@Valid @RequestBody AmlCheckRequest request) {
        return amlCheckService.performAmlScreening(request);
    }

}
