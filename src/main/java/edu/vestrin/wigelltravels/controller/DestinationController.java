package edu.vestrin.wigelltravels.controller;

import edu.vestrin.wigelltravels.dto.request.DestinationRequestDto;
import edu.vestrin.wigelltravels.dto.request.UpdateDestinationRequestDto;
import edu.vestrin.wigelltravels.dto.response.DestinationResponseDto;
import edu.vestrin.wigelltravels.service.DestinationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/destinations")
public class DestinationController {

    private final DestinationService service;

    public DestinationController(DestinationService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<DestinationResponseDto>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DestinationResponseDto> create(@Valid @RequestBody DestinationRequestDto request) {
        var created = service.create(request);
        var location = URI.create("/api/v1/destinations" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DestinationResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDestinationRequestDto request) {

        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }



}
