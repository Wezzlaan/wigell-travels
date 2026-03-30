package edu.vestrin.wigelltravels.service;

import edu.vestrin.wigelltravels.dto.request.CustomerRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateCustomerRequestDto;
import edu.vestrin.wigelltravels.dto.response.CustomerResponseDto;
import edu.vestrin.wigelltravels.mapper.CustomerMapper;
import edu.vestrin.wigelltravels.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepo;
    private final CustomerMapper mapper;

    public CustomerServiceImpl(CustomerRepository customerRepo, CustomerMapper mapper) {
        this.customerRepo = customerRepo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public List<CustomerResponseDto> list() {
        return customerRepo.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CustomerResponseDto create(CustomerRequestDto request, String keycloakId) {
        var customer = mapper.toEntity(request, keycloakId);
        var saved = customerRepo.save(customer);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CustomerResponseDto update(Long id, UpdateCustomerRequestDto request) {
        var customer = customerRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not find customer with id '%d".formatted(id)));
        var updated = customerRepo.save(mapper.applyUpdate(customer, request));

        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        var customer = customerRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not find customer with id '%d".formatted(id)));
        customerRepo.delete(customer);
    }
}
