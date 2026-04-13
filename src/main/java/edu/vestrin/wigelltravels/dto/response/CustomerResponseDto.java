package edu.vestrin.wigelltravels.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record CustomerResponseDto(Long id,
                                  String keycloakId,
                                  String firstName,
                                  String lastName,
                                  String socSecNum,
                                  String phoneNum,
                                  List<AddressCustomerResponseDto> addresses) {
}
