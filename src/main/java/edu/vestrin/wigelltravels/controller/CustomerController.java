package edu.vestrin.wigelltravels.controller;

import edu.vestrin.wigelltravels.dto.request.AddressRequestDto;
import edu.vestrin.wigelltravels.dto.request.CustomerWithUserRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateCustomerRequestDto;
import edu.vestrin.wigelltravels.dto.response.CustomerResponseDto;
import edu.vestrin.wigelltravels.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@PreAuthorize("hasRole('ADMIN')")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDto> create(
            @Valid @RequestBody CustomerWithUserRequestDto request) {

        var created = service.create(request);
        var location = URI.create("/api/v1/customers/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequestDto request) {

        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{customerId}/addresses")
    public ResponseEntity<CustomerResponseDto> addAddress(
            @PathVariable Long customerId,
            @Valid @RequestBody AddressRequestDto request) {

        var created = service.createAddress(customerId, request);
        var location = URI.create("/api/v1/customers/" + customerId + "/addresses/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @DeleteMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long customerId,
            @PathVariable Long addressId) {

        service.deleteAddress(customerId, addressId);
        return ResponseEntity.noContent().build();
    }
}
