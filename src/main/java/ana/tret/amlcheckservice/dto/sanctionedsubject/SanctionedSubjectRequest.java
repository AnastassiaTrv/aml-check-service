package ana.tret.amlcheckservice.dto.sanctionedsubject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SanctionedSubjectRequest(@NotBlank @Size(max = 255) String fullName) {}
