package edu.vestrin.wigelltravels.service;

import com.groupc.shared.exception.ResourceNotFoundException;
import edu.vestrin.wigelltravels.dto.request.AddressRequestDto;
import edu.vestrin.wigelltravels.dto.request.CustomerWithUserRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateCustomerRequestDto;
import edu.vestrin.wigelltravels.dto.response.CustomerResponseDto;
import edu.vestrin.wigelltravels.entity.Address;
import edu.vestrin.wigelltravels.entity.Customer;
import edu.vestrin.wigelltravels.mapper.CustomerMapper;
import edu.vestrin.wigelltravels.repository.AddressRepository;
import edu.vestrin.wigelltravels.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

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
    @Transactional
    public List<CustomerResponseDto> findAll() {
        return customerRepo.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CustomerResponseDto create(CustomerWithUserRequestDto request) {

        String keycloakId = keycloakUserService.createUserKeycloak(
                request.email(),
                request.username(),
                request.password(),
                request.firstName(),
                request.lastName()
        );

        var customer = mapper.toEntity(request, keycloakId);
        var saved = customerRepo.save(customer);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CustomerResponseDto update(Long customerId, UpdateCustomerRequestDto request) {
        var customer = findCustomer(customerId);

        keycloakUserService.updateUser(
                customer.getKeycloakId(),
                request.firstName(),
                request.lastName()
        );

        var updated = customerRepo.save(mapper.applyUpdate(customer, request));

        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    public CustomerResponseDto createAddress(Long customerId, AddressRequestDto request) {
        var customer = findCustomer(customerId);

        var address = new Address(
                request.country(),
                request.city(),
                request.street(),
                request.postalCode()
        );
        addressRepo.save(address);

        customer.getAddresses().add(address);

        return mapper.toResponse(customerRepo.save(customer));
    }

    @Override
    @Transactional
    public void delete(Long customerId) {
        var customer = findCustomer(customerId);

        keycloakUserService.deleteUser(customer.getKeycloakId());

        customerRepo.delete(customer);
    }

    @Override
    @Transactional
    public void deleteAddress(Long customerId, Long addressId) {
        var customer = findCustomer(customerId);

        var addressToRemove = customer.getAddresses().stream()
                .filter(address -> address.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Address does not belong to this customer"));

        customer.getAddresses().remove(addressToRemove);

        customerRepo.save(customer);
        addressRepo.delete(addressToRemove);
    }

    private Customer findCustomer(Long customerId) {
        return customerRepo.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find customer with id '%d'".formatted(customerId)));
    }
}
