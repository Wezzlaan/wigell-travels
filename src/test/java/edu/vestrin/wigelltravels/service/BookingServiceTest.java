package edu.vestrin.wigelltravels.service;

import com.groupc.shared.client.CurrencyClient;
import edu.vestrin.wigelltravels.dto.request.BookingRequestDto;
import edu.vestrin.wigelltravels.dto.request.PatchBookingRequestDto;
import edu.vestrin.wigelltravels.dto.response.BookingResponseDto;
import edu.vestrin.wigelltravels.entity.Booking;
import edu.vestrin.wigelltravels.entity.Customer;
import edu.vestrin.wigelltravels.entity.Destination;
import edu.vestrin.wigelltravels.exceptions.BookingNotFoundException;
import edu.vestrin.wigelltravels.exceptions.DestinationNotFoundException;
import edu.vestrin.wigelltravels.mapper.BookingMapper;
import edu.vestrin.wigelltravels.repository.BookingRepository;
import edu.vestrin.wigelltravels.repository.CustomerRepository;
import edu.vestrin.wigelltravels.repository.DestinationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private DestinationRepository destinationRepository;

    @Mock
    private BookingMapper mapper;

    @Mock
    private CurrencyClient currencyClient;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void listById_shouldReturnMappedBookings() {
        var booking1 = new Booking();
        var booking2 = new Booking();

        var dto1 = mock(BookingResponseDto.class);
        var dto2 = mock(BookingResponseDto.class);

        when(bookingRepository.findByCustomerId(5L)).thenReturn(List.of(booking1, booking2));
        when(mapper.toResponse(booking1)).thenReturn(dto1);
        when(mapper.toResponse(booking2)).thenReturn(dto2);

        List<BookingResponseDto> result = bookingService.listById(5L);

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));

        verify(bookingRepository).findByCustomerId(5L);
        verify(mapper).toResponse(booking1);
        verify(mapper).toResponse(booking2);
    }

    @Test
    void create_shouldCreateBookingAndReturnResponse() {
        String keycloakId = "kc-123";

        var request = new BookingRequestDto(
                10L,
                LocalDate.now(),
                2
        );

        var customer = new Customer();

        var destination = new Destination();
        destination.setPricePerWeek(BigDecimal.valueOf(1000));

        when(currencyClient.getExchangeRate("SEK", "PLN")).thenReturn(0.4);

        when(customerRepository.findCustomerByKeycloakId(keycloakId))
                .thenReturn(Optional.of(customer));

        when(destinationRepository.findById(10L))
                .thenReturn(Optional.of(destination));

        var bookingEntity = new Booking();
        var savedBooking = new Booking();

        var responseDto = mock(BookingResponseDto.class);

        when(mapper.toEntity(eq(request), eq(customer), eq(destination),
                eq(BigDecimal.valueOf(2000)), eq(BigDecimal.valueOf(800.0))))
                .thenReturn(bookingEntity);

        when(bookingRepository.save(bookingEntity)).thenReturn(savedBooking);
        when(mapper.toResponse(savedBooking)).thenReturn(responseDto);

        var result = bookingService.createBooking(request, keycloakId);

        assertEquals(responseDto, result);

        verify(customerRepository).findCustomerByKeycloakId(keycloakId);
        verify(destinationRepository).findById(10L);
        verify(currencyClient).getExchangeRate("SEK", "PLN");
        verify(bookingRepository).save(bookingEntity);
    }

    @Test
    void patch_shouldThrowBookingNotFound_whenBookingMissing() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        var request = new PatchBookingRequestDto(null, 2L, null);

        assertThrows(BookingNotFoundException.class, () -> bookingService.patchBooking(1L, request));

        verify(bookingRepository).findById(1L);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void patch_shouldThrowDestinationNotFound_whenDestinationMissing() {
        var booking = new Booking();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(destinationRepository.findById(99L)).thenReturn(Optional.empty());

        var request = new PatchBookingRequestDto(null, 99L, null);

        assertThrows(DestinationNotFoundException.class, () -> bookingService.patchBooking(1L, request));

        verify(destinationRepository).findById(99L);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void patch_shouldUpdateWeeksAndRecalculatePrice() {
        var destination = new Destination();
        destination.setPricePerWeek(BigDecimal.valueOf(500));

        var booking = new Booking();
        booking.setDestination(destination);
        booking.setNumOfWeeks(3);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(currencyClient.getExchangeRate("SEK", "PLN")).thenReturn(0.5);

        var request = new PatchBookingRequestDto(3, null, null);

        var response = mock(BookingResponseDto.class);

        when(bookingRepository.save(booking)).thenReturn(booking);
        when(mapper.toResponse(booking)).thenReturn(response);

        var result = bookingService.patchBooking(1L, request);

        assertEquals(response, result);

        assertEquals(BigDecimal.valueOf(1500), booking.getTotalPriceSEK());
        assertEquals(BigDecimal.valueOf(750.0), booking.getTotalPricePLN());

        verify(currencyClient).getExchangeRate("SEK", "PLN");
        verify(bookingRepository).save(booking);
    }
}
