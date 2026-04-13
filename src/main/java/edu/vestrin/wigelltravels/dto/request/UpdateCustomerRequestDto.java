package edu.vestrin.wigelltravels.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateCustomerRequestDto(
        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "^[a-zA-ZåäöÅÄÖéÉ\\s\\-']+$", message = "Ogiltiga tecken i firstName")
        String firstName,

        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "^[a-zA-ZåäöÅÄÖéÉ\\s\\-']+$", message = "Ogiltiga tecken i lastName")
        String lastName,

        @NotBlank
        @Pattern(regexp = "^[+\\d][\\d\\s\\-]{6,14}$", message = "Ogiltigt format för phoneNum.")
        String phoneNum
) {
}
