package edu.vestrin.wigelltravels.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DestinationResponseDto(
        Long id,
        String hotelName,
        String city,
        String country,
        BigDecimal pricePerWeek,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
