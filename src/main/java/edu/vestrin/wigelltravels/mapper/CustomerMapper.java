package edu.vestrin.wigelltravels.mapper;

import edu.vestrin.wigelltravels.dto.request.CustomerWithUserRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateCustomerRequestDto;
import edu.vestrin.wigelltravels.dto.response.AddressCustomerResponseDto;
import edu.vestrin.wigelltravels.dto.response.CustomerResponseDto;
import edu.vestrin.wigelltravels.entity.Address;
import edu.vestrin.wigelltravels.entity.Customer;
import edu.vestrin.wigelltravels.exceptions.InvalidPhoneNumException;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerWithUserRequestDto request, String keycloakId) {
        var country = normalizedName(request.country());
        var city = normalizedName(request.city());
        var street = normalizedName(request.street());

        var address = new Address(
                country,
                city,
                street,
                request.postalCode()
        );

        var firstName = normalizedName(request.firstName());
        var lastName = normalizedName(request.lastName());
        var num = normalizedPhoneNum(request.phoneNum());

        return new Customer(
                keycloakId,
                firstName,
                lastName,
                request.socSecNum(),
                num,
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

    // --- PRIVATE HELPER METHODS ---

    private String normalizedPhoneNum(String dtoPhoneNum) {
        if (dtoPhoneNum == null || dtoPhoneNum.isBlank()) return null;

        String cleaned = dtoPhoneNum.replaceAll("[^0-9+]", "");

        if (cleaned.startsWith("07")) {
            cleaned = "+46" + cleaned.substring(1);
        } else if (cleaned.startsWith("46")) {
            cleaned = "+" + cleaned;
        }

        if (!cleaned.matches("^\\+46\\d{9}$")) throw new InvalidPhoneNumException(cleaned);

        return String.format("%s %s %s %s %s",
                cleaned.substring(0, 3),  // +46
                cleaned.substring(3, 5),  // 70
                cleaned.substring(5, 8),  // 123
                cleaned.substring(8, 10), // 45
                cleaned.substring(10, 12) // 67
        );
    }

    private String normalizedName(String name) {
        String cleaned = name.trim();

        return cleaned.substring(0, 1).toUpperCase() + cleaned.substring(1).toLowerCase();
    }
}
