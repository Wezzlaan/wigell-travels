package edu.vestrin.wigelltravels.mapper;

import edu.vestrin.wigelltravels.dto.request.AddressRequestDto;
import edu.vestrin.wigelltravels.dto.response.AddressResponseDto;
import edu.vestrin.wigelltravels.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address toEntity(AddressRequestDto request) {
        return new Address(
                request.country(),
                request.city(),
                request.street(),
                request.postalCode()
        );
    }

    public AddressResponseDto toResponse(Address address) {
        return new AddressResponseDto(
                address.getId(),
                address.getCountry(),
                address.getCity(),
                address.getStreet(),
                address.getPostalCode(),
                address.getCreatedAt(),
                address.getUpdatedAt()
        );
    }
}
