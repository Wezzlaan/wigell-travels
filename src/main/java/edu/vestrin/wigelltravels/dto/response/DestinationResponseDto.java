package edu.vestrin.wigelltravels.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
