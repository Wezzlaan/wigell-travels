package edu.vestrin.wigelltravels.service;

import edu.vestrin.wigelltravels.dto.request.AddressRequestDto;
import edu.vestrin.wigelltravels.dto.request.CustomerRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateCustomerRequestDto;
import edu.vestrin.wigelltravels.dto.response.CustomerResponseDto;

import java.util.List;

public interface CustomerService {

    List<CustomerResponseDto> findAll();

    CustomerResponseDto create(CustomerRequestDto request, String keycloakId);

    CustomerResponseDto update(Long id, UpdateCustomerRequestDto request);

    CustomerResponseDto createAddress(Long customerId, AddressRequestDto request);

    void delete(Long id);

    void deleteAddress(Long customerId, Long addressId);
}
