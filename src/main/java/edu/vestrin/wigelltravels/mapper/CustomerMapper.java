package edu.vestrin.wigelltravels.mapper;

import edu.vestrin.wigelltravels.dto.request.CustomerRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateCustomerRequestDto;
import edu.vestrin.wigelltravels.dto.response.AddressCustomerResponseDto;
import edu.vestrin.wigelltravels.dto.response.CustomerResponseDto;
import edu.vestrin.wigelltravels.entity.Address;
import edu.vestrin.wigelltravels.entity.Customer;
import edu.vestrin.wigelltravels.repository.CustomerRepository;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequestDto request, String keycloakId) {
        var address = new Address(
                request.country(),
                request.city(),
                request.street(),
                request.postalCode()
        );

        return new Customer(
                keycloakId,
                request.firstName(),
                request.lastName(),
                request.socSecNum(),
                request.phoneNum(),
                address
        );
    }

    public CustomerResponseDto toResponse(Customer customer) {
        var addressResponse = new AddressCustomerResponseDto(
                customer.getAddress().getId(),
                customer.getAddress().getCountry(),
                customer.getAddress().getCity(),
                customer.getAddress().getStreet(),
                customer.getAddress().getPostalCode()
        );

        return new CustomerResponseDto(
                customer.getId(),
                customer.getKeycloakId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getSocSecNum(),
                customer.getPhoneNum(),
                addressResponse
        );
    }

    public Customer applyUpdate(Customer customer, UpdateCustomerRequestDto request) {
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setPhoneNum(request.phoneNum());

        customer.getAddress().setCountry(request.country());
        customer.getAddress().setCity(request.city());
        customer.getAddress().setStreet(request.street());
        customer.getAddress().setPostalCode(request.postalCode());

        return customer;
    }
}
