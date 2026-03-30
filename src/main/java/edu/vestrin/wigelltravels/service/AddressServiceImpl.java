package edu.vestrin.wigelltravels.service;

import edu.vestrin.wigelltravels.dto.request.AddressRequestDto;
import edu.vestrin.wigelltravels.dto.response.AddressResponseDto;
import edu.vestrin.wigelltravels.mapper.AddressMapper;
import edu.vestrin.wigelltravels.repository.AddressRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepo;

    private final AddressMapper mapper;

    public AddressServiceImpl(AddressRepository addressRepo, AddressMapper mapper) {
        this.addressRepo = addressRepo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public AddressResponseDto create(AddressRequestDto request) {
        var address = mapper.toEntity(request);
        var saved = addressRepo.save(address);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        var address = addressRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not find address with id '%d'".formatted(id)));

        addressRepo.delete(address);
    }
}
