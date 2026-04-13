package edu.vestrin.wigelltravels.service;

import edu.vestrin.wigelltravels.dto.request.DestinationRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateDestinationRequestDto;
import edu.vestrin.wigelltravels.dto.response.DestinationResponseDto;

import java.util.List;

public interface DestinationService {

    List<DestinationResponseDto> findAll();

    DestinationResponseDto createDestination(DestinationRequestDto request);

    DestinationResponseDto updateDestination(Long id, UpdateDestinationRequestDto request);

    void deleteDestination(Long id);


}
