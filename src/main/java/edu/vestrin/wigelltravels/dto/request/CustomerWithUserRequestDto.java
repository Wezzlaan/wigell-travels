package edu.vestrin.wigelltravels.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerWithUserRequestDto(

        @NotBlank
        @Email(message = "Incorrect email format")
        String email,

        @NotBlank
        @Size(max = 20)
        String username,

        @Size(min = 8, max = 60)
        String password,

        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "^[a-zA-ZåäöÅÄÖéÉ\\s\\-']+$", message = "Ogiltiga tecken i firstName")
        String firstName,

        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "^[a-zA-ZåäöÅÄÖéÉ\\s\\-']+$", message = "Ogiltiga tecken i lastName")
        String lastName,

        @NotBlank
        @Pattern(regexp = "^\\d{8}-\\d{4}$", message = "Ogiltigt format för socSecNum. Förväntat: YYYYMMDD-XXXX")
        String socSecNum,

        @NotBlank
        @Pattern(regexp = "^[+\\d][\\d\\s\\-]{6,14}$", message = "Ogiltigt format för phoneNum.")
        String phoneNum,

        @NotBlank
        @Pattern(regexp = "^[a-zA-ZåäöÅÄÖéÉ\\s\\-']+$", message = "Ogiltiga tecken i country.")
        @Size(max = 50)
        String country,

        @NotBlank
        @Pattern(regexp = "^[a-zA-ZåäöÅÄÖéÉ\\s\\-']+$", message = "Ogiltiga tecken i city")
        @Size(max = 50)
        String city,

        @NotBlank
        @Pattern(regexp = "^[a-zA-ZåäöÅÄÖéÉ0-9\\s\\-']+$", message = "Ogiltiga tecken i street")
        @Size(max = 50)
        String street,

        @NotBlank
        @Pattern(regexp = "^\\d{5}$", message = "postalCode måste vara endast siffror och ha exakt 5 tecken.")
        String postalCode
) {
}
