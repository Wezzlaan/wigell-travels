package edu.vestrin.wigelltravels.mapper;

import edu.vestrin.wigelltravels.dto.request.DestinationRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateDestinationRequestDto;
import edu.vestrin.wigelltravels.dto.response.DestinationResponseDto;
import edu.vestrin.wigelltravels.entity.Destination;
import org.springframework.stereotype.Component;

@Component
public class DestinationMapper {

    public Destination toEntity(DestinationRequestDto request) {
        return new Destination(
                request.hotelName(),
                request.city(),
                request.country(),
                request.pricePerWeek()
        );
    }

    public DestinationResponseDto toResponse(Destination destination) {
        return new DestinationResponseDto(
                destination.getId(),
                destination.getHotelName(),
                destination.getCity(),
                destination.getCountry(),
                destination.getPricePerWeek(),
                destination.getCreatedAt(),
                destination.getUpdatedAt()
        );
    }


    public Destination applyUpdate(Destination destination, UpdateDestinationRequestDto request) {
        destination.setHotelName(request.hotelName());
        destination.setCity(request.city());
        destination.setCountry(request.country());
        destination.setPricePerWeek(request.pricePerWeek());

        return destination;
    }


}
