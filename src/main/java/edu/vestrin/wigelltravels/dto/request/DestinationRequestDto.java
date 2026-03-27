package edu.vestrin.wigelltravels.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DestinationRequestDto(
        @NotBlank
        @Size(max = 60)
        String hotelName,

        @NotBlank
        @Size(max = 50)
        String city,

        @NotBlank
        @Size(max = 50)
        String country,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal pricePerWeek
) {
}
