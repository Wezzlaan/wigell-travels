package edu.vestrin.wigelltravels.service;

import com.groupc.shared.exception.ResourceNotFoundException;
import edu.vestrin.wigelltravels.dto.request.DestinationRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateDestinationRequestDto;
import edu.vestrin.wigelltravels.dto.response.DestinationResponseDto;
import edu.vestrin.wigelltravels.mapper.DestinationMapper;
import edu.vestrin.wigelltravels.repository.DestinationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DestinationServiceImpl implements DestinationService{

    private final DestinationRepository destinationRepo;
    private final DestinationMapper mapper;

    public DestinationServiceImpl(DestinationRepository destinationRepo, DestinationMapper mapper) {
        this.destinationRepo = destinationRepo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public List<DestinationResponseDto> findAll() {
        return destinationRepo.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public DestinationResponseDto create(DestinationRequestDto request) {
        var destination = mapper.toEntity(request);
        var saved = destinationRepo.save(destination);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public DestinationResponseDto update(Long id, UpdateDestinationRequestDto request) {
        var destination = destinationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find destination with id '%d'".formatted(id)));
        var updated = destinationRepo.save(mapper.applyUpdate(destination, request));

        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        var destination = destinationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find destination with id '%d'".formatted(id)));
        destinationRepo.delete(destination);
    }
}
