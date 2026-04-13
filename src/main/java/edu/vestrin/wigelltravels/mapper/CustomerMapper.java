package edu.vestrin.wigelltravels.mapper;

import edu.vestrin.wigelltravels.Util.StringNormalizer;
import edu.vestrin.wigelltravels.dto.request.CustomerWithUserRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateCustomerRequestDto;
import edu.vestrin.wigelltravels.dto.response.AddressCustomerResponseDto;
import edu.vestrin.wigelltravels.dto.response.CustomerResponseDto;
import edu.vestrin.wigelltravels.entity.Address;
import edu.vestrin.wigelltravels.entity.Customer;
import edu.vestrin.wigelltravels.exceptions.InvalidPhoneNumException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    private final static Logger logger = LoggerFactory.getLogger(CustomerMapper.class);

    public Customer toEntity(CustomerWithUserRequestDto request, String keycloakId) {
        logger.debug("toEntity() - Konverterar DTO till entitet.");
        var country = StringNormalizer.name(request.country());
        var city = StringNormalizer.name(request.city());
        var street = StringNormalizer.name(request.street());

        var address = new Address(
                country,
                city,
                street,
                request.postalCode()
        );

        var firstName = StringNormalizer.name(request.firstName());
        var lastName = StringNormalizer.name(request.lastName());
        var num = StringNormalizer.phoneNumber(request.phoneNum());

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
        logger.debug("toResponse() - Konverterar Customer med ID: {} till DTO.", customer.getId());

        logger.trace("toResponse() - Hämtar lista med Address från Customer.");
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
        logger.debug("applyUpdate() - Applicerar uppdatering av begärd customer med ID: {}.", customer.getId());
        var firstName = StringNormalizer.name(request.firstName());
        var lastName = StringNormalizer.name(request.lastName());
        var num = StringNormalizer.phoneNumber(request.phoneNum());

        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhoneNum(num);

        return customer;
    }
}
