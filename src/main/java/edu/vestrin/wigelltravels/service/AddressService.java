package edu.vestrin.wigelltravels.service;

import edu.vestrin.wigelltravels.dto.request.AddressRequestDto;
import edu.vestrin.wigelltravels.dto.response.AddressResponseDto;

public interface AddressService {

    AddressResponseDto create(AddressRequestDto request);

    void delete(Long id);
}
