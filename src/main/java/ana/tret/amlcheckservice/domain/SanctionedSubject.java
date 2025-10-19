package ana.tret.amlcheckservice.domain;

import java.time.OffsetDateTime;

public record SanctionedSubject(Long id,
                                String fullName,
                                String normalizedName,
                                OffsetDateTime createdAt,
                                OffsetDateTime updatedAt,
                                Long version) {}
