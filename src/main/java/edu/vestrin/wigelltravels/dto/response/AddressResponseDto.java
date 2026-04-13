package edu.vestrin.wigelltravels.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AddressResponseDto(
        Long id,
        String country,
        String city,
        String street,
        String postalCode,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
