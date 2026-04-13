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
        logger.info("isOwner() - Anropad med Customer ID = {}", customerId);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
            logger.warn("isOwner() - Autentisering är inte av typ JwtAuthenticationToken, nekar åtkomst.");
            return false;
        }

        String keycloakId = jwtAuth.getToken().getSubject();

        boolean result = customerRepo.findCustomerByKeycloakId(keycloakId)
                .map(customer -> customer.getId().equals(customerId))
                .orElse(false);

        logger.debug("isOwner() - resultat = {} för keycloak ID = {}, Customer ID = {}", result, keycloakId, customerId);
        return result;
    }

    public boolean isBookingOwner(Long bookingId) {
        logger.info("isBookingOwner() anropad med bookingId={}", bookingId);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
            logger.warn("isBookingOwner() - Autentisering är inte av typ JwtAuthenticationToken, nekar åtkomst.");
            return false;
        }

        String keycloakId = jwtAuth.getToken().getSubject();

        boolean result = customerRepo.findCustomerByKeycloakId(keycloakId)
                .flatMap(customer -> bookingRepo.findById(bookingId)
                        .map(booking -> booking.getCustomer().getId().equals(customer.getId())))
                .orElse(false);

        logger.info("isBookingOwner() - resultat = {} för Keycloak ID = {}, Booking ID = {}", result, keycloakId, bookingId);
        return result;
    }
}
