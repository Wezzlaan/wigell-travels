package edu.vestrin.wigelltravels.service;

import com.groupc.shared.exception.ResourceNotFoundException;
import edu.vestrin.wigelltravels.dto.request.AddressRequestDto;
import edu.vestrin.wigelltravels.dto.request.CustomerWithUserRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateCustomerRequestDto;
import edu.vestrin.wigelltravels.dto.response.CustomerResponseDto;
import edu.vestrin.wigelltravels.entity.Address;
import edu.vestrin.wigelltravels.entity.Customer;
import edu.vestrin.wigelltravels.exceptions.CustomerDeletionException;
import edu.vestrin.wigelltravels.exceptions.CustomerNotFoundException;
import edu.vestrin.wigelltravels.exceptions.OwnershipConflictException;
import edu.vestrin.wigelltravels.mapper.CustomerMapper;
import edu.vestrin.wigelltravels.repository.AddressRepository;
import edu.vestrin.wigelltravels.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final static Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final EntityManager entityManager;

    private final CustomerRepository customerRepo;
    private final AddressRepository addressRepo;
    private final CustomerMapper mapper;
    private final KeycloakUserService keycloakUserService;

    public CustomerServiceImpl(CustomerRepository customerRepo, AddressRepository addressRepo,
                               CustomerMapper mapper, KeycloakUserService keycloakUserService,
                               EntityManager entityManager) {
        this.customerRepo = customerRepo;
        this.addressRepo = addressRepo;
        this.mapper = mapper;
        this.keycloakUserService = keycloakUserService;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDto> findAll() {
        logger.info("findAll() - Begär alla customers...");
        return customerRepo.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CustomerResponseDto createCustomer(CustomerWithUserRequestDto request) {
        logger.info("createCustomer() - Begär skapande av Customer och Keycloak User...");
        String keycloakId = keycloakUserService.createUserKeycloak(
                request.email(),
                request.username(),
                request.password(),
                request.firstName(),
                request.lastName()
        );

        var customer = mapper.toEntity(request, keycloakId);
        var saved = customerRepo.save(customer);
        logger.info("createCustomer() - Customer sparad i databas med ID: {}", saved.getId());
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CustomerResponseDto updateCustomer(Long customerId, UpdateCustomerRequestDto request) {
        logger.info("updateCustomer() - Efterfrågar uppdatering av Customer med ID: {}...", customerId);
        var customer = findCustomer(customerId);

        keycloakUserService.updateUser(
                customer.getKeycloakId(),
                request.firstName(),
                request.lastName()
        );

        var updated = customerRepo.save(mapper.applyUpdate(customer, request));
        logger.info("updateCustomer() - Uppdatering av Customer med ID: {} har sparats.", updated.getId());
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    public CustomerResponseDto createAddress(Long customerId, AddressRequestDto request) {
        logger.info("createAddress() - Efterfrågar skapande av Address för Customer med ID: {}", customerId);
        var customer = findCustomer(customerId);

        var address = new Address(
                request.country(),
                request.city(),
                request.street(),
                request.postalCode()
        );
        addressRepo.save(address);
        logger.info("createAddress() - Address sparad till databas med ID: {}", address.getId());

        customer.getAddresses().add(address);

        var saved = mapper.toResponse(customerRepo.save(customer));
        logger.info("createAddress() - Address tillagd och sparad till Customer med ID: {}", saved.id());
        return saved;
    }

    @Override
    @Transactional
    public void deleteCustomer(Long customerId) {
        logger.info("deleteCustomer() - Begär radering av Customer med ID: {}", customerId );
        var customer = findCustomer(customerId);

        try {
            customerRepo.delete(customer);
            entityManager.flush();
            keycloakUserService.deleteUser(customer.getKeycloakId());
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new CustomerDeletionException("Kunde inte ta bort Customer med anledning: " + e.getMessage());
        }

        logger.info("deleteCustomer() - Radering av Customer med ID: {} lyckad.", customerId);
    }

    @Override
    @Transactional
    public void deleteAddress(Long customerId, Long addressId) {
        logger.info("deleteAddress() - Begär radering av Address med ID: {} som tillhör Customer med ID: {}",
                addressId, customerId);

        var customer = findCustomer(customerId);

        var addressToRemove = customer.getAddresses().stream()
                .filter(address -> address.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new OwnershipConflictException("Address ägs inte av denna Customer"));

        customer.getAddresses().remove(addressToRemove);

        customerRepo.save(customer);
        addressRepo.delete(addressToRemove);

        logger.info("deleteAddress() - Address med ID: {} har lyckats raderas och tas bort från Customer med ID: {}",
                addressId, customerId);
    }

    private Customer findCustomer(Long customerId) {
        logger.debug("findCustomer() - Söker efter Customer med ID: {}...", customerId);
        return customerRepo.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }
}
