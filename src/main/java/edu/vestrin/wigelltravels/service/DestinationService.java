package edu.vestrin.wigelltravels.service;

import edu.vestrin.wigelltravels.dto.request.DestinationRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateDestinationRequestDto;
import edu.vestrin.wigelltravels.dto.response.DestinationResponseDto;

import java.util.List;

public interface DestinationService {

    List<DestinationResponseDto> list();

    DestinationResponseDto create(DestinationRequestDto request);

    DestinationResponseDto update(Long id, UpdateDestinationRequestDto request);

    void delete(Long id);


}
