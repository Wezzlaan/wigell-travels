package edu.vestrin.wigelltravels.service;

import com.groupc.shared.exception.AccessDeniedException;
import com.groupc.shared.exception.ResourceNotFoundException;
import edu.vestrin.wigelltravels.dto.request.BookingRequestDto;
import edu.vestrin.wigelltravels.dto.request.PatchBookingRequestDto;
import edu.vestrin.wigelltravels.dto.response.BookingResponseDto;
import edu.vestrin.wigelltravels.entity.Booking;
import edu.vestrin.wigelltravels.entity.Destination;
import edu.vestrin.wigelltravels.mapper.BookingMapper;
import edu.vestrin.wigelltravels.repository.BookingRepository;
import edu.vestrin.wigelltravels.repository.CustomerRepository;
import edu.vestrin.wigelltravels.repository.DestinationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final static Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);
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
        logger.info("listById() - Requesting bookings from customer ID: {}", customerId);
        return bookingRepo.findByCustomerId(customerId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public BookingResponseDto create(BookingRequestDto request, String keycloakId) {

        logger.info("create() - Requesting creation of new booking: Destination ID = {}, Departure Date = {}, Number of Weeks = {}",
                request.destinationId(), request.departureDate(), request.numOfWeeks());

        var customer = customerRepo.findCustomerByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        var destination = destinationRepo.findById(request.destinationId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found"));

        BigDecimal totalSEK = destination.getPricePerWeek()
                .multiply(BigDecimal.valueOf(request.numOfWeeks()));

        BigDecimal totalPLN = convertTotalSEKToPLN(totalSEK);

        var booking = mapper.toEntity(request, customer, destination, totalSEK, totalPLN);

        var saved = mapper.toResponse(bookingRepo.save(booking));

        logger.debug("Booking persisted with ID: {}", saved.id());

        return saved;
    }

    @Override
    public BookingResponseDto patch(Long bookingId, PatchBookingRequestDto request) {

        logger.info("patch() - Requesting patch of booking with ID: {}. Requested patch: Number of Weeks = {}, Destination ID = {}, Hotel Name = {}",
                bookingId, request.destinationId(), request.numOfWeeks(), request.hotelName());

        var booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (request.numOfWeeks() != null) {
            logger.info("Number of weeks contains non-null value: Updating to requested value = {}", request.numOfWeeks());
            booking.setNumOfWeeks(request.numOfWeeks());
        }
        if (request.hotelName() != null) {
            logger.info("Hotel Name contains non-null value: Updating to requested value = {}", request.hotelName());
            booking.setHotelName(request.hotelName());
        }
        if (request.destinationId() != null) {
            logger.info("Destination ID contains non-null value: " +
                    "Finding and updating provided Destination to requested values...");

            var destination = destinationRepo.findById(request.destinationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Destination not found"));

            logger.info("Destination found. Applying update: Hotel Name = {}, City = {}, Country = {}, Price Per Week = {}",
                    destination.getHotelName(), destination.getCity(), destination.getCountry(), destination.getPricePerWeek());

            setBookingDestination(booking, destination);

            /*booking.setDestination(destination);
            booking.setCity(destination.getCity());
            booking.setCountry(destination.getCountry());

            BigDecimal newSEK = destination.getPricePerWeek()
                    .multiply(BigDecimal.valueOf(booking.getNumOfWeeks()));
            booking.setTotalPriceSEK(newSEK);


            booking.setTotalPricePLN(convertTotalSEKToPLN(newSEK));
            logger.info("Conversion from SEK to PLN successful.");*/
        }

        var saved = mapper.toResponse(bookingRepo.save(booking));

        logger.debug("Patch of Booking with ID: {} has been persisted.", saved.id());

        return saved;
    }

    private BigDecimal convertTotalSEKToPLN(BigDecimal sek) {
        logger.debug("convertTotalSEKToPLN() - Calling currency exchange service for the conversion of SEK -> PLN");

        return sek.multiply(BigDecimal.valueOf(0.40)); //TODO: REPLACE WITH CALL TO CURRENCY CONVERTER SERVICE
    }

    private void setBookingDestination(Booking booking, Destination destination) {

        booking.setDestination(destination);
        booking.setCity(destination.getCity());
        booking.setCountry(destination.getCountry());

        BigDecimal newSEK = destination.getPricePerWeek()
                .multiply(BigDecimal.valueOf(booking.getNumOfWeeks()));
        booking.setTotalPriceSEK(newSEK);


        booking.setTotalPricePLN(convertTotalSEKToPLN(newSEK)); // TODO: Replace with call to Currency Converter microservice.
        logger.info("Conversion from SEK to PLN successful.");
    }
}
