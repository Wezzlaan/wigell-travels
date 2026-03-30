package edu.vestrin.wigelltravels.service;

import edu.vestrin.wigelltravels.dto.request.CustomerRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateCustomerRequestDto;
import edu.vestrin.wigelltravels.dto.response.CustomerResponseDto;

import java.util.List;

public interface CustomerService {

    List<CustomerResponseDto> list();

    CustomerResponseDto create(CustomerRequestDto request, String keycloakId);

    CustomerResponseDto update(Long id, UpdateCustomerRequestDto request);

    void delete(Long id);
}
