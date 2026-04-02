package edu.vestrin.wigelltravels.mapper;

import edu.vestrin.wigelltravels.dto.request.CustomerWithUserRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateCustomerRequestDto;
import edu.vestrin.wigelltravels.dto.response.AddressCustomerResponseDto;
import edu.vestrin.wigelltravels.dto.response.CustomerResponseDto;
import edu.vestrin.wigelltravels.entity.Address;
import edu.vestrin.wigelltravels.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerWithUserRequestDto request, String keycloakId) {
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
        var addresses = customer.getAddresses().stream()
                .map(address -> new AddressCustomerResponseDto(
                        address.getId(),
                        address.getCountry(),
                        address.getCity(),
                        address.getStreet(),
                        address.getPostalCode()
                )).toList();

        return new CustomerResponseDto(
                customer.getId(),
                customer.getKeycloakId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getSocSecNum(),
                customer.getPhoneNum(),
                addresses
        );
    }

    public Customer applyUpdate(Customer customer, UpdateCustomerRequestDto request) {
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setPhoneNum(request.phoneNum());

        return customer;
    }
}
