package ana.tret.amlcheckservice.dto.sanctionedsubject;

import java.time.OffsetDateTime;

public record SanctionedSubjectDto(Long id,
                                   String fullName,
                                   String normalizedName,
                                   OffsetDateTime createdAt,
                                   OffsetDateTime updatedAt,
                                   Long version) {}
