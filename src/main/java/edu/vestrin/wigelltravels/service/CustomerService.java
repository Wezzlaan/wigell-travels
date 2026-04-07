package edu.vestrin.wigelltravels.service;

import edu.vestrin.wigelltravels.dto.request.AddressRequestDto;
import edu.vestrin.wigelltravels.dto.request.CustomerWithUserRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateCustomerRequestDto;
import edu.vestrin.wigelltravels.dto.response.CustomerResponseDto;

import java.util.List;

public interface CustomerService {

    List<CustomerResponseDto> findAll();

    CustomerResponseDto createCustomer(CustomerWithUserRequestDto request);

    CustomerResponseDto updateCustomer(Long id, UpdateCustomerRequestDto request);

    CustomerResponseDto createAddress(Long customerId, AddressRequestDto request);

    void deleteCustomer(Long id);

    void deleteAddress(Long customerId, Long addressId);
}
