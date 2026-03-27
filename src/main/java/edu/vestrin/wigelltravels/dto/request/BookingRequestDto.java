package edu.vestrin.wigelltravels.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookingRequestDto(
        @NotNull
        Long destinationId,

        @NotNull
        @Future
        LocalDate departureDate,

        @Min(1)
        @Max(52)
        int numOfWeeks
) {
}
