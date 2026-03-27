package edu.vestrin.wigelltravels.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Optional;

public record PatchCustomerRequestDto(

        @Size(max = 50)
        @Pattern(regexp = "^[a-zA-ZåäöÅÄÖéÉ\\s\\-']+$", message = "Invalid characters in first name")
        String firstName,

        @Size(max = 50)
        @Pattern(regexp = "^[a-zA-ZåäöÅÄÖéÉ\\s\\-']+$", message = "Invalid characters in last name")
        String lastName,

        @Pattern(regexp = "^\\d{8}-\\d{4}$", message = "Invalid SSN format, expected: YYYYMMDD-XXXX")
        String socSecNum,

        @Pattern(regexp = "^[+\\d][\\d\\s\\-]{6,14}$", message = "Invalid phone number")
        String phoneNum,

        @Pattern(regexp = "^[a-zA-ZåäöÅÄÖéÉ\\s\\-']+$", message = "Invalid characters in country")
        @Size(max = 50)
        String country,

        @Pattern(regexp = "^[a-zA-ZåäöÅÄÖéÉ\\s\\-']+$", message = "Invalid characters in city")
        @Size(max = 50)
        String city,

        @Pattern(regexp = "^[a-zA-ZåäöÅÄÖéÉ0-9\\s\\-']+$", message = "Invalid characters in street address")
        @Size(max = 50)
        String street,

        @Pattern(regexp = "^\\d{5}$", message = "Postal code must be exactly 5 digits")
        String postalCode
) {
}
