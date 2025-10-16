package ana.tret.amlcheckservice.dto.amlcheck;

import ana.tret.amlcheckservice.dto.sanctionedsubject.SubjectMatchDto;

public record AmlCheckResponse(String bestMatch, double score, boolean match) {

    public static AmlCheckResponse fromSubjectMatch(SubjectMatchDto dto, boolean isMatch) {
        return new AmlCheckResponse(dto.fullName(), dto.rate(), isMatch);
    }

    public static AmlCheckResponse fromZeroMatch() {
        return new AmlCheckResponse(null, 0.0, false);
    }
}
