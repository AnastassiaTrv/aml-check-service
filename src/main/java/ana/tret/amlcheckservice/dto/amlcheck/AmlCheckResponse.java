package ana.tret.amlcheckservice.dto.amlcheck;

import ana.tret.amlcheckservice.domain.SubjectMatch;

import static ana.tret.amlcheckservice.dto.amlcheck.MatchType.ZERO;

public record AmlCheckResponse(String bestMatch, MatchType type, double score, boolean match) {

    public static AmlCheckResponse fromSubjectMatch(SubjectMatch dto,
                                                    MatchType type,
                                                    boolean isMatch) {
        return new AmlCheckResponse(dto.fullName(), type, dto.rate(), isMatch);
    }

    public static AmlCheckResponse fromZeroMatch() {
        return new AmlCheckResponse(null, ZERO, 0.0, false);
    }
}
