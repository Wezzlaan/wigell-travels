package edu.vestrin.wigelltravels.service;

import com.groupc.shared.exception.ResourceNotFoundException;
import edu.vestrin.wigelltravels.dto.request.BookingRequestDto;
import edu.vestrin.wigelltravels.dto.request.PatchBookingRequestDto;
import edu.vestrin.wigelltravels.dto.response.BookingResponseDto;
import edu.vestrin.wigelltravels.mapper.BookingMapper;
import edu.vestrin.wigelltravels.repository.BookingRepository;
import edu.vestrin.wigelltravels.repository.CustomerRepository;
import edu.vestrin.wigelltravels.repository.DestinationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepo;
    private final CustomerRepository customerRepo;
    private final DestinationRepository destinationRepo;
    private final BookingMapper mapper;

    public BookingServiceImpl(BookingRepository bookingRepo, CustomerRepository customerRepo,
                              DestinationRepository destinationRepo, BookingMapper mapper) {
        this.bookingRepo = bookingRepo;
        this.customerRepo = customerRepo;
        this.destinationRepo = destinationRepo;
        this.mapper = mapper;
    }

    @Override
    public List<BookingResponseDto> listById(Long customerId) {
        return bookingRepo.findByCustomerId(customerId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public BookingResponseDto create(BookingRequestDto request, String keycloakId) {
        var customer = customerRepo.findCustomerByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        var destination = destinationRepo.findById(request.destinationId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found"));

        BigDecimal totalSEK = destination.getPricePerWeek()
                .multiply(BigDecimal.valueOf(request.numOfWeeks()));

        BigDecimal totalPLN = totalSEK.multiply(BigDecimal.valueOf(0.40)); // TODO: Replace with call to Currency Converter microservice.

        var booking = mapper.toEntity(request, customer, destination, totalSEK, totalPLN);

        return mapper.toResponse(bookingRepo.save(booking));
    }

    @Override
    public BookingResponseDto patch(Long bookingId, PatchBookingRequestDto request) {
        var booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (request.numOfWeeks() != null) booking.setNumOfWeeks(request.numOfWeeks());
        if (request.hotelName() != null) booking.setHotelName(request.hotelName());
        if (request.destinationId() != null) {
            var destination = destinationRepo.findById(request.destinationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Destination not found"));
            booking.setDestination(destination);
            booking.setCity(destination.getCity());
            booking.setCountry(destination.getCountry());

            BigDecimal newSEK = destination.getPricePerWeek()
                    .multiply(BigDecimal.valueOf(booking.getNumOfWeeks()));
            booking.setTotalPriceSEK(newSEK);
            booking.setTotalPricePLN(newSEK.multiply(BigDecimal.valueOf(0.40))); // TODO: Replace with call to Currency Converter microservice.
        }

        return mapper.toResponse(bookingRepo.save(booking));
    }
}
