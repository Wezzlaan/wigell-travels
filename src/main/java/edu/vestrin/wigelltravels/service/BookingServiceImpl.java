package edu.vestrin.wigelltravels.service;

import com.groupc.shared.client.CurrencyClient;
import com.groupc.shared.exception.AccessDeniedException;
import com.groupc.shared.exception.ResourceNotFoundException;
import edu.vestrin.wigelltravels.dto.request.BookingRequestDto;
import edu.vestrin.wigelltravels.dto.request.PatchBookingRequestDto;
import edu.vestrin.wigelltravels.dto.response.BookingResponseDto;
import edu.vestrin.wigelltravels.entity.Booking;
import edu.vestrin.wigelltravels.entity.BookingStatus;
import edu.vestrin.wigelltravels.entity.Destination;
import edu.vestrin.wigelltravels.exceptions.BookingNotFoundException;
import edu.vestrin.wigelltravels.exceptions.CustomerNotFoundException;
import edu.vestrin.wigelltravels.exceptions.DestinationNotFoundException;
import edu.vestrin.wigelltravels.mapper.BookingMapper;
import edu.vestrin.wigelltravels.repository.BookingRepository;
import edu.vestrin.wigelltravels.repository.CustomerRepository;
import edu.vestrin.wigelltravels.repository.DestinationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final static Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);
    private final BookingRepository bookingRepo;
    private final CustomerRepository customerRepo;
    private final DestinationRepository destinationRepo;
    private final BookingMapper mapper;
    private final CurrencyClient currencyClient;

    public BookingServiceImpl(BookingRepository bookingRepo, CustomerRepository customerRepo,
                              DestinationRepository destinationRepo, BookingMapper mapper, CurrencyClient currencyClient) {
        this.bookingRepo = bookingRepo;
        this.customerRepo = customerRepo;
        this.destinationRepo = destinationRepo;
        this.mapper = mapper;
        this.currencyClient = currencyClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> listById(Long customerId) {
        logger.info("listById() - Begär Bookings från customer ID: {}", customerId);
        return bookingRepo.findByCustomerId(customerId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto request, String keycloakId) {

        logger.info("createCustomer() - Efterfrågar skapande av ny Booking = {}, Departure Date = {}, Number of Weeks = {}",
                request.destinationId(), request.departureDate(), request.numOfWeeks());

        var customer = customerRepo.findCustomerByKeycloakId(keycloakId)
                .orElseThrow(() -> new CustomerNotFoundException(keycloakId));

        var destination = destinationRepo.findById(request.destinationId())
                .orElseThrow(() -> new DestinationNotFoundException(request.destinationId()));

        BigDecimal totalSEK = destination.getPricePerWeek()
                .multiply(BigDecimal.valueOf(request.numOfWeeks()));

        BigDecimal totalPLN = convertTotalSEKToPLN(totalSEK);

        var booking = mapper.toEntity(request, customer, destination, totalSEK, totalPLN);

        var saved = mapper.toResponse(bookingRepo.save(booking));

        logger.debug("createCustomer() - Booking sparad med ID: {}", saved.id());

        return saved;
    }

    @Override
    @Transactional
    public BookingResponseDto patchBooking(Long bookingId, PatchBookingRequestDto request) {

        logger.info("patchBooking() - Efterfrågar patch av Booking med ID: {}. Efterfrågad patch: Number of Weeks = {}, Destination ID = {}, Hotel Name = {}",
                bookingId, request.destinationId(), request.numOfWeeks(), request.hotelName());

        var booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        boolean needsPriceRecalc = false;

        if (request.numOfWeeks() != null) {
            logger.debug("patchBooking() - numOfWeeks innehåller non-null värde: uppdaterar till begärt värde = {}", request.numOfWeeks());
            booking.setNumOfWeeks(request.numOfWeeks());
            needsPriceRecalc = true;
        }
        if (request.hotelName() != null) {
            logger.debug("patchBooking() - hotelName innehåller non-null värde: uppdaterar till begärt värde = {}", request.hotelName());
            booking.setHotelName(request.hotelName());
        }
        if (request.destinationId() != null) {
            logger.debug("patchBooking() - Destination ID innehåller non-null värde: " +
                    "Försöker hitta och uppdatera efterfrågad Destination till begärda värden...");

            var destination = destinationRepo.findById(request.destinationId())
                    .orElseThrow(() -> new DestinationNotFoundException(request.destinationId()));

            logger.debug("patchBooking() - Destination hittad. Applicerar updateCustomer: Hotel Name = {}, City = {}, Country = {}, Price Per Week = {}",
                    destination.getHotelName(), destination.getCity(), destination.getCountry(), destination.getPricePerWeek());

            booking.setDestination(destination);
            booking.setCity(destination.getCity());
            booking.setCountry(destination.getCountry());

            needsPriceRecalc = true;
        }

        if (needsPriceRecalc) {
            recalculateBookingPrice(booking);
        }

        booking.setStatus(BookingStatus.MODIFIED_CONFIRMED);
        var saved = mapper.toResponse(bookingRepo.save(booking));

        logger.info("patchBooking() - Patch av Booking med ID: {} har sparats.", saved.id());

        return saved;
    }

    // --- PRIVATE HELPER METHODS ---

    private BigDecimal convertTotalSEKToPLN(BigDecimal sek) {
        logger.debug("convertTotalSEKToPLN() - Anropar currency-client service för konvertering av SEK -> PLN");

        var exchangeRate = currencyClient.getExchangeRate("SEK", "PLN");
        return sek.multiply(BigDecimal.valueOf(exchangeRate));
    }

    private void recalculateBookingPrice(Booking booking) {
        logger.debug("recalculateBookingPrice() - Räknar om summa med uppdaterade bokningsdetaljer...");

        BigDecimal pricePerWeek = booking.getDestination().getPricePerWeek();
        BigDecimal newSEK = pricePerWeek.multiply(BigDecimal.valueOf(booking.getNumOfWeeks()));

        booking.setTotalPriceSEK(newSEK);
        booking.setTotalPricePLN(convertTotalSEKToPLN(newSEK));

        logger.debug("recalculateBookingPrice() - Kalkylering lyckad. Ny total = SEK: {}", newSEK);
    }
}
