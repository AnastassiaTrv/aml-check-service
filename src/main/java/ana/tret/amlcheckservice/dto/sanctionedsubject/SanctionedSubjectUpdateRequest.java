package ana.tret.amlcheckservice.dto.sanctionedsubject;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SanctionedSubjectUpdateRequest(@NotBlank @Size(max = 255) String fullName,
                                             @NotNull @Min(0) Long version) {}
