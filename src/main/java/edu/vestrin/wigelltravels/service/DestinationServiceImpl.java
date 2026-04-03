package edu.vestrin.wigelltravels.service;

import com.groupc.shared.exception.ResourceNotFoundException;
import edu.vestrin.wigelltravels.dto.request.DestinationRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateDestinationRequestDto;
import edu.vestrin.wigelltravels.dto.response.DestinationResponseDto;
import edu.vestrin.wigelltravels.mapper.DestinationMapper;
import edu.vestrin.wigelltravels.repository.DestinationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DestinationServiceImpl implements DestinationService{

    private final static Logger logger = LoggerFactory.getLogger(DestinationServiceImpl.class);

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
        logger.info("findAll() - Requesting all destinations...");

        return destinationRepo.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public DestinationResponseDto create(DestinationRequestDto request) {
        logger.info("create() - Requesting creation of Destination: Hotel Name = {}, City = {}, Country = {}, Price Per Week = {}",
                request.hotelName(), request.city(), request.country(), request.pricePerWeek());

        var destination = mapper.toEntity(request);
        var saved = destinationRepo.save(destination);

        logger.debug("Destination persisted with ID: {}", saved.getId());
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public DestinationResponseDto update(Long id, UpdateDestinationRequestDto request) {
        logger.info("update() - Requesting update of Destination with ID: {}. Requested values to update:" +
                "Hotel Name = {}, City = {}, Country = {}, Price Per Week = {}",
                id, request.hotelName(), request.city(), request.country(), request.pricePerWeek());

        var destination = destinationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find destination with id '%d'".formatted(id)));
        var updated = destinationRepo.save(mapper.applyUpdate(destination, request));

        logger.debug("Update of Destination with ID: {} has been persisted.", updated.getId());
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        logger.info("delete() - Requesting deletion of Destination with ID: {}.", id);
        var destination = destinationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find destination with id '%d'".formatted(id)));
        destinationRepo.delete(destination);

        logger.debug("Destination with ID: {} has successfully been deleted.", id);
    }
}
