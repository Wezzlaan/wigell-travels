package edu.vestrin.wigelltravels.security;

import edu.vestrin.wigelltravels.repository.BookingRepository;
import edu.vestrin.wigelltravels.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service("securityService")
public class SecurityService {

    private final static Logger logger = LoggerFactory.getLogger(SecurityService.class);

    private final CustomerRepository customerRepo;
    private final BookingRepository bookingRepo;

    public SecurityService(CustomerRepository customerRepo, BookingRepository bookingRepo) {
        this.customerRepo = customerRepo;
        this.bookingRepo = bookingRepo;
    }

    public boolean isOwner(Long customerId) {
        logger.info("isOwner() called with Customer ID = {}", customerId);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
            logger.warn("isOwner() - authentication is not a JwtAuthenticationToken, denying access.");
            return false;
        }

        String keycloakId = jwtAuth.getToken().getSubject();

        boolean result = customerRepo.findCustomerByKeycloakId(keycloakId)
                .map(customer -> customer.getId().equals(customerId))
                .orElse(false);

        logger.debug("isOwner() - result = {} for keycloak ID = {}, Customer ID = {}", result, keycloakId, customerId);
        return result;
    }

    public boolean isBookingOwner(Long bookingId) {
        logger.debug("isBookingOwner() called with bookingId={}", bookingId);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
            logger.warn("isBookingOwner() - authentication is not a JwtAuthenticationToken, denying access.");
            return false;
        }

        String keycloakId = jwtAuth.getToken().getSubject();

        boolean result = customerRepo.findCustomerByKeycloakId(keycloakId)
                .flatMap(customer -> bookingRepo.findById(bookingId)
                        .map(booking -> booking.getCustomer().getId().equals(customer.getId())))
                .orElse(false);

        logger.debug("isBookingOwner() - result = {} for Keycloak ID = {}, Booking ID = {}", result, keycloakId, bookingId);
        return result;
    }
}
