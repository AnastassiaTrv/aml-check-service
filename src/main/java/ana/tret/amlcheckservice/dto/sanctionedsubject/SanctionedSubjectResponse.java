package ana.tret.amlcheckservice.dto.sanctionedsubject;

import java.time.OffsetDateTime;

public record SanctionedSubjectResponse(
        Long id,
        String fullName,
        String normalizedName,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Long version) {

    public static SanctionedSubjectResponse fromDto(SanctionedSubjectDto e) {
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
