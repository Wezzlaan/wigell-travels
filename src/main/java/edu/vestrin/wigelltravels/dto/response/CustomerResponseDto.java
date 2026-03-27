package edu.vestrin.wigelltravels.dto.response;

public record CustomerResponseDto(Long id,
                                  String keycloakId,
                                  String firstName,
                                  String lastName,
                                  String socSecNum,
                                  String phoneNum,
                                  AddressCustomerResponseDto address) {
}
