package ana.tret.amlcheckservice.dto.amlcheck;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AmlCheckRequest(@NotBlank @Size(max = 255) String fullName) {
}
