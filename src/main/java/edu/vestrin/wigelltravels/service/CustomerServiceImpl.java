package edu.vestrin.wigelltravels.service;

import com.groupc.shared.exception.ResourceNotFoundException;
import edu.vestrin.wigelltravels.dto.request.AddressRequestDto;
import edu.vestrin.wigelltravels.dto.request.CustomerWithUserRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateCustomerRequestDto;
import edu.vestrin.wigelltravels.dto.response.CustomerResponseDto;
import edu.vestrin.wigelltravels.entity.Address;
import edu.vestrin.wigelltravels.entity.Customer;
import edu.vestrin.wigelltravels.exceptions.CustomerNotFoundException;
import edu.vestrin.wigelltravels.exceptions.OwnershipConflictException;
import edu.vestrin.wigelltravels.mapper.CustomerMapper;
import edu.vestrin.wigelltravels.repository.AddressRepository;
import edu.vestrin.wigelltravels.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final static Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepo;
    private final AddressRepository addressRepo;
    private final CustomerMapper mapper;
    private final KeycloakUserService keycloakUserService;

    public CustomerServiceImpl(CustomerRepository customerRepo, AddressRepository addressRepo,
                               CustomerMapper mapper, KeycloakUserService keycloakUserService) {
        this.customerRepo = customerRepo;
        this.addressRepo = addressRepo;
        this.mapper = mapper;
        this.keycloakUserService = keycloakUserService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDto> findAll() {
        logger.info("findAll() - Requesting all customers...");
        return customerRepo.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CustomerResponseDto createCustomer(CustomerWithUserRequestDto request) {
        logger.info("createCustomer - Requesting creation of Customer and Keycloak User...");
        String keycloakId = keycloakUserService.createUserKeycloak(
                request.email(),
                request.username(),
                request.password(),
                request.firstName(),
                request.lastName()
        );

        var customer = mapper.toEntity(request, keycloakId);
        var saved = customerRepo.save(customer);
        logger.info("Customer persisted with ID: {}", saved.getId());
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CustomerResponseDto updateCustomer(Long customerId, UpdateCustomerRequestDto request) {
        logger.info("updateCustomer() - Requesting updateCustomer of Customer with ID: {}...", customerId);
        var customer = findCustomer(customerId);

        keycloakUserService.updateUser(
                customer.getKeycloakId(),
                request.firstName(),
                request.lastName()
        );

        var updated = customerRepo.save(mapper.applyUpdate(customer, request));
        logger.info("Update of Customer with ID: {} has been persisted.", updated.getId());
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    public CustomerResponseDto createAddress(Long customerId, AddressRequestDto request) {
        logger.info("createAddress - Requesting creation of Address for Customer with ID: {}.", customerId);

        var customer = findCustomer(customerId);

        var address = new Address(
                request.country(),
                request.city(),
                request.street(),
                request.postalCode()
        );
        addressRepo.save(address);
        logger.info("Address persisted with ID: {}", address.getId());

        customer.getAddresses().add(address);

        var saved = mapper.toResponse(customerRepo.save(customer));
        logger.info("Address added and persisted to Customer with ID: {}", saved.id());
        return saved;
    }

    @Override
    @Transactional
    public void deleteCustomer(Long customerId) {
        logger.info("Delete() - Requesting deletion of Customer with ID: {}", customerId );
        var customer = findCustomer(customerId);

        keycloakUserService.deleteUser(customer.getKeycloakId());

        customerRepo.delete(customer);
        logger.info("Customer with ID: {} has successfully been deleted.", customerId);
    }

    @Override
    @Transactional
    public void deleteAddress(Long customerId, Long addressId) {
        logger.info("deleteAddress() - Requesting deletion of Address with ID: {} belonging to Customer with ID: {}",
                addressId, customerId);

        var customer = findCustomer(customerId);

        var addressToRemove = customer.getAddresses().stream()
                .filter(address -> address.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new OwnershipConflictException("Address does not belong to this customer"));

        customer.getAddresses().remove(addressToRemove);

        customerRepo.save(customer);
        addressRepo.delete(addressToRemove);

        logger.debug("Address with ID: {} has been successfully deleted and removed from Customer with ID: {}",
                addressId, customerId);
    }

    private Customer findCustomer(Long customerId) {
        logger.debug("Searching for Customer with ID: {}...", customerId);
        return customerRepo.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }
}
