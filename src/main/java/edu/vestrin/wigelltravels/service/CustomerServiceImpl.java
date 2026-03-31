package edu.vestrin.wigelltravels.service;

import com.groupc.shared.exception.ResourceNotFoundException;
import edu.vestrin.wigelltravels.dto.request.AddressRequestDto;
import edu.vestrin.wigelltravels.dto.request.CustomerRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateCustomerRequestDto;
import edu.vestrin.wigelltravels.dto.response.CustomerResponseDto;
import edu.vestrin.wigelltravels.entity.Address;
import edu.vestrin.wigelltravels.mapper.CustomerMapper;
import edu.vestrin.wigelltravels.repository.AddressRepository;
import edu.vestrin.wigelltravels.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepo;
    private final AddressRepository addressRepo;
    private final CustomerMapper mapper;

    public CustomerServiceImpl(CustomerRepository customerRepo, AddressRepository addressRepo, CustomerMapper mapper) {
        this.customerRepo = customerRepo;
        this.addressRepo = addressRepo;
        this.mapper = mapper;
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
    public CustomerResponseDto create(CustomerRequestDto request, String keycloakId) {
        var customer = mapper.toEntity(request, keycloakId);
        var saved = customerRepo.save(customer);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CustomerResponseDto update(Long id, UpdateCustomerRequestDto request) {
        var customer = customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find customer with id '%d".formatted(id)));
        var updated = customerRepo.save(mapper.applyUpdate(customer, request));

        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    public CustomerResponseDto createAddress(Long customerId, AddressRequestDto request) {
        var customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find customer with id '%d".formatted(customerId)));

        var address = new Address(
                request.country(),
                request.city(),
                request.street(),
                request.postalCode()
        );
        addressRepo.save(address);

        customer.setAddress(address);
        return mapper.toResponse(customerRepo.save(customer));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        var customer = customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find customer with id '%d".formatted(id)));
        customerRepo.delete(customer);
    }

    @Override
    public void deleteAddress(Long customerId, Long addressId) {
        var customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find customer with id '%d".formatted(customerId)));

        if (!customer.getAddress().getId().equals(addressId)) {
            throw new IllegalArgumentException("Address does not belong to this customer");
        }

        throw new IllegalStateException("Cannot delete the only address. Update the customer with a new address instead.");
    }
}
