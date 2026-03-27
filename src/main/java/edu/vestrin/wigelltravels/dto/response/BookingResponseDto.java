package edu.vestrin.wigelltravels.dto.response;

import edu.vestrin.wigelltravels.entity.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookingResponseDto(
        Long id,
        Long customerId,
        Long destinationId,
        LocalDate departureDate,
        int numOfWeeks,
        String hotelName,
        String city,
        String country,
        BigDecimal totalPriceSEK,
        BigDecimal totalPricePLN,
        BookingStatus status,
        LocalDateTime bookedAt
) {
}
