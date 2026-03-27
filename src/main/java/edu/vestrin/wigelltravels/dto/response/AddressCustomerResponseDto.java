package edu.vestrin.wigelltravels.dto.response;

public record AddressCustomerResponseDto(Long id,
                                         String country,
                                         String city,
                                         String street,
                                         String postalCode) {
}
