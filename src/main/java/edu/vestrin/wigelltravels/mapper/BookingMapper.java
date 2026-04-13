package edu.vestrin.wigelltravels.mapper;

import edu.vestrin.wigelltravels.Util.StringNormalizer;
import edu.vestrin.wigelltravels.dto.request.BookingRequestDto;
import edu.vestrin.wigelltravels.dto.response.BookingResponseDto;
import edu.vestrin.wigelltravels.entity.Booking;
import edu.vestrin.wigelltravels.entity.Customer;
import edu.vestrin.wigelltravels.entity.Destination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BookingMapper {
    private final static Logger logger = LoggerFactory.getLogger(BookingMapper.class);
    public Booking toEntity(BookingRequestDto request, Customer customer,
                            Destination destination, BigDecimal totalSEK, BigDecimal totalPLN) {
        logger.debug("toEntity() - Konverterar DTO till entitet.");

        var hotelName = StringNormalizer.name(destination.getHotelName());
        var city = StringNormalizer.name(destination.getCity());
        var country = StringNormalizer.name(destination.getCountry());

        return new Booking(
                customer,
                destination,
                request.departureDate(),
                request.numOfWeeks(),
                hotelName,
                city,
                country,
                totalSEK,
                totalPLN
        );
    }

    public BookingResponseDto toResponse(Booking booking) {
        logger.debug("toResponse() - Konverterar entitet till DTO.");

        return new BookingResponseDto(
                booking.getId(),
                booking.getCustomer().getId(),
                booking.getDestination().getId(),
                booking.getDepartureDate(),
                booking.getNumOfWeeks(),
                booking.getHotelName(),
                booking.getCity(),
                booking.getCountry(),
                booking.getTotalPriceSEK(),
                booking.getTotalPricePLN(),
                booking.getStatus(),
                booking.getBookedAt(),
                booking.getModifiedAt()
        );
    }
}
