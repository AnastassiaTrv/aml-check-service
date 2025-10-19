package ana.tret.amlcheckservice.dto.sanctionedsubject;

import ana.tret.amlcheckservice.domain.SanctionedSubject;

import java.time.OffsetDateTime;

public record SanctionedSubjectResponse(
        Long id,
        String fullName,
        String normalizedName,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Long version) {

    public static SanctionedSubjectResponse fromDto(SanctionedSubject e) {
        return new SanctionedSubjectResponse(
                e.id(),
                e.fullName(),
                e.normalizedName(),
                e.createdAt(),
                e.updatedAt(),
                e.version()
        );
    }
}
