package edu.vestrin.wigelltravels.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressRequestDto(
        @NotBlank
        @Pattern(regexp = "^[a-zA-ZåäöÅÄÖéÉ\\s\\-']+$", message = "Ogiltiga tecken i country")
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
        @Pattern(regexp = "^\\d{5}$", message = "postalCode måste vara endast siffror och ha exakt 5 tecken")
        String postalCode
) {
}
