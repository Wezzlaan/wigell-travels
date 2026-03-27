package edu.vestrin.wigelltravels.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerRequestDto(
        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "^[a-zA-ZåäöÅÄÖéÉ\\s\\-']+$", message = "Invalid characters in first name")
        String firstName,

        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "^[a-zA-ZåäöÅÄÖéÉ\\s\\-']+$", message = "Invalid characters in last name")
        String lastName,

        @NotBlank
        @Pattern(regexp = "^\\d{8}-\\d{4}$", message = "Invalid SSN format, expected: YYYYMMDD-XXXX")
        String socSecNum,

        @NotBlank
        @Pattern(regexp = "^[+\\d][\\d\\s\\-]{6,14}$", message = "Invalid phone number")
        String phoneNum,

        @NotBlank
        @Pattern(regexp = "^[a-zA-ZåäöÅÄÖéÉ\\s\\-']+$", message = "Invalid characters in country")
        @Size(max = 50)
        String country,

        @NotBlank
        @Pattern(regexp = "^[a-zA-ZåäöÅÄÖéÉ\\s\\-']+$", message = "Invalid characters in city")
        @Size(max = 50)
        String city,

        @NotBlank
        @Pattern(regexp = "^[a-zA-ZåäöÅÄÖéÉ0-9\\s\\-']+$", message = "Invalid characters in street address")
        @Size(max = 50)
        String street,

        @NotBlank
        @Pattern(regexp = "^\\d{5}$", message = "Postal code must be exactly 5 digits")
        String postalCode
) {
}
