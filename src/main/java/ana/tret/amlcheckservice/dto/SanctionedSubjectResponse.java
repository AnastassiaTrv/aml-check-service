package ana.tret.amlcheckservice.dto;

import ana.tret.amlcheckservice.repository.sanctionedsubject.SanctionedSubject;

import java.time.Instant;

public record SanctionedSubjectResponse(
        Long id,
        String fullName,
        String normalizedName,
        Instant createdAt,
        Instant updatedAt) {

    public static SanctionedSubjectResponse fromEntity(SanctionedSubject e) {
        return new SanctionedSubjectResponse(
                e.getId(),
                e.getFullName(),
                e.getNormalizedName(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }
}
