package edu.vestrin.wigelltravels.security;

import edu.vestrin.wigelltravels.repository.BookingRepository;
import edu.vestrin.wigelltravels.repository.CustomerRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service("securityService")
public class SecurityService {

    private final CustomerRepository customerRepo;
    private final BookingRepository bookingRepo;

    public SecurityService(CustomerRepository customerRepo, BookingRepository bookingRepo) {
        this.customerRepo = customerRepo;
        this.bookingRepo = bookingRepo;
    }

    public boolean isOwner(Long customerId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof JwtAuthenticationToken jwtAuth)) return false;

        String keycloakId = jwtAuth.getToken().getSubject();

        return customerRepo.findCustomerByKeycloakId(keycloakId)
                .map(customer -> customer.getId().equals(customerId))
                .orElse(false);
    }

    public boolean isBookingOwner(Long bookingId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof JwtAuthenticationToken jwtAuth)) return false;

        String keycloakId = jwtAuth.getToken().getSubject();

        return customerRepo.findCustomerByKeycloakId(keycloakId)
                .flatMap(customer -> bookingRepo.findById(bookingId)
                        .map(booking -> booking.getCustomer().getId().equals(customer.getId())))
                .orElse(false);
    }
}
