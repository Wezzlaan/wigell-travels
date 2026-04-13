package edu.vestrin.wigelltravels.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.vestrin.wigelltravels.entity.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
        LocalDateTime bookedAt,
        LocalDateTime modifiedAt
) {
}
