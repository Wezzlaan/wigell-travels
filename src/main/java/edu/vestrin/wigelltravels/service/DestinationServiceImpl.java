package edu.vestrin.wigelltravels.service;

import com.groupc.shared.exception.ResourceNotFoundException;
import edu.vestrin.wigelltravels.dto.request.DestinationRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateDestinationRequestDto;
import edu.vestrin.wigelltravels.dto.response.DestinationResponseDto;
import edu.vestrin.wigelltravels.exceptions.DestinationNotFoundException;
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
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('USER')")
    public List<DestinationResponseDto> findAll() {
        logger.info("findAll() - Begär alla destinationer...");

        return destinationRepo.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public DestinationResponseDto createDestination(DestinationRequestDto request) {
        logger.info("createDestination() - Begär skapande av Destination: Hotel Name = {}, City = {}, Country = {}, Price Per Week = {}",
                request.hotelName(), request.city(), request.country(), request.pricePerWeek());

        var destination = mapper.toEntity(request);
        var saved = destinationRepo.save(destination);

        logger.info("createDestination - Destination sparad i databas with ID: {}", saved.getId());
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public DestinationResponseDto updateDestination(Long id, UpdateDestinationRequestDto request) {
        /*logger.info("updateCustomer() - Requesting updateCustomer of Destination with ID: {}. Requested values to updateCustomer:" +
                "Hotel Name = {}, City = {}, Country = {}, Price Per Week = {}",
                id, request.hotelName(), request.city(), request.country(), request.pricePerWeek());*/

        logger.info("updateDestination() - Begär uppdatering av Destination med ID: {}. Begärde värden:" +
                        "Hotel Name = {}, City = {}, Country = {}, Price Per Week = {}",
                id, request.hotelName(), request.city(), request.country(), request.pricePerWeek());

        var destination = destinationRepo.findById(id)
                .orElseThrow(() -> new DestinationNotFoundException(id));
        var updated = destinationRepo.save(mapper.applyUpdate(destination, request));

        logger.info("updateDestination() - Uppdatering av Destination med ID: {} har sparats i databas.", updated.getId());
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDestination(Long id) {
        logger.info("deleteCustomer() - Behär borttagning av Destination med ID: {}.", id);
        var destination = destinationRepo.findById(id)
                .orElseThrow(() -> new DestinationNotFoundException(id));
        destinationRepo.delete(destination);

        logger.info("Destination med ID: {} har tagits bort.", id);
    }
}
