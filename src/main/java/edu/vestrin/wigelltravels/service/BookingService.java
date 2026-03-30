package edu.vestrin.wigelltravels.service;

import edu.vestrin.wigelltravels.dto.request.BookingRequestDto;
import edu.vestrin.wigelltravels.dto.request.PatchBookingRequestDto;
import edu.vestrin.wigelltravels.dto.response.BookingResponseDto;
import edu.vestrin.wigelltravels.repository.BookingRepository;

import java.util.List;

public interface BookingService {

    List<BookingResponseDto> listById(Long id);

    BookingResponseDto create(BookingRequestDto request, String keycloakId);

    BookingResponseDto patch(Long bookingId, PatchBookingRequestDto request);
}
