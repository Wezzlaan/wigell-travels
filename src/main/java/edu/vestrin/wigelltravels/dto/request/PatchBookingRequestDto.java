package edu.vestrin.wigelltravels.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record PatchBookingRequestDto(

        @Min(1)
        @Max(52)
        Integer numOfWeeks,

        Long destinationId,

        @Size(max = 60)
        String hotelName
) {
}
