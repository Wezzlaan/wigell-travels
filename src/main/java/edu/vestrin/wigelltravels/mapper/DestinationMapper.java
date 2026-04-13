package edu.vestrin.wigelltravels.mapper;

import edu.vestrin.wigelltravels.Util.StringNormalizer;
import edu.vestrin.wigelltravels.dto.request.DestinationRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateDestinationRequestDto;
import edu.vestrin.wigelltravels.dto.response.DestinationResponseDto;
import edu.vestrin.wigelltravels.entity.Destination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DestinationMapper {

    private static final Logger logger = LoggerFactory.getLogger(DestinationMapper.class);

    public Destination toEntity(DestinationRequestDto request) {
        logger.debug("toEntity() - Konverterar DTO till entitet.");
        var hotelName = StringNormalizer.name(request.hotelName());
        var city = StringNormalizer.name(request.city());
        var country = StringNormalizer.name(request.country());

        return new Destination(
                hotelName,
                city,
                country,
                request.pricePerWeek()
        );
    }

    public DestinationResponseDto toResponse(Destination destination) {
        logger.debug("toResponse() - Konverterar Destination med ID: {} till DTO.", destination.getId());
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
        logger.debug("applyUpdate() - Applicerar uppdatering av begärd Destination med ID: {}.", destination.getId());
        var hotelName = StringNormalizer.name(request.hotelName());
        var city = StringNormalizer.name(request.city());
        var country = StringNormalizer.name(request.country());

        destination.setHotelName(hotelName);
        destination.setCity(city);
        destination.setCountry(country);
        destination.setPricePerWeek(request.pricePerWeek());

        return destination;
    }
}
