package edu.vestrin.wigelltravels.mapper;

import edu.vestrin.wigelltravels.dto.request.BookingRequestDto;
import edu.vestrin.wigelltravels.dto.response.BookingResponseDto;
import edu.vestrin.wigelltravels.entity.Booking;
import edu.vestrin.wigelltravels.entity.Customer;
import edu.vestrin.wigelltravels.entity.Destination;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BookingMapper {

    public Booking toEntity(BookingRequestDto request, Customer customer,
                            Destination destination, BigDecimal totalSEK, BigDecimal totalPLN) {

        return new Booking(
                customer,
                destination,
                request.departureDate(),
                request.numOfWeeks(),
                destination.getHotelName(),
                destination.getCity(),
                destination.getCountry(),
                totalSEK,
                totalPLN
        );
    }

    public BookingResponseDto toResponse(Booking booking) {
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
                booking.getBookedAt()
        );
    }
}
