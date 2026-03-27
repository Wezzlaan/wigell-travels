package edu.vestrin.wigelltravels.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

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
