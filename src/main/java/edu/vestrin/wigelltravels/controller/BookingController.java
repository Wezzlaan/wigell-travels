package edu.vestrin.wigelltravels.controller;

import edu.vestrin.wigelltravels.dto.request.BookingRequestDto;
import edu.vestrin.wigelltravels.dto.request.PatchBookingRequestDto;
import edu.vestrin.wigelltravels.dto.response.BookingResponseDto;
import edu.vestrin.wigelltravels.security.SecurityService;
import edu.vestrin.wigelltravels.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER') AND @securityService.isOwner(#customerId)")
    public ResponseEntity<List<BookingResponseDto>> list(@RequestParam Long customerId) {
        return ResponseEntity.ok(service.listById(customerId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<BookingResponseDto> create(
            @Valid @RequestBody BookingRequestDto request,
            JwtAuthenticationToken token) {

        String keycloakId = token.getToken().getSubject();
        var created = service.create(request, keycloakId);
        var location = URI.create("/api/v1/bookings/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @PatchMapping("/{bookingId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER') AND @securityService.isBookingOwner(#bookingId)")
    public ResponseEntity<BookingResponseDto> patch(@PathVariable Long bookingId,
                                                    @Valid @RequestBody PatchBookingRequestDto request) {

        return ResponseEntity.ok(service.patch(bookingId, request));
    }
}
