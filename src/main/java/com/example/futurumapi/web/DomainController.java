package com.example.futurumapi.web;

import com.example.futurumapi.dto.DomainDTO;
import com.example.futurumapi.services.Domain.DomainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/domains")
@CrossOrigin(origins = "http://localhost:4200")
public class DomainController {

    private final DomainService domainService;

    public DomainController(DomainService domainService) {
        this.domainService = domainService;
    }

    @GetMapping
    public ResponseEntity<List<DomainDTO>> getAllDomains() {
        return ResponseEntity.ok(domainService.getAllDomains());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DomainDTO> getDomainById(@PathVariable Long id) {
        return domainService.getDomainById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DomainDTO> createDomain(@RequestBody DomainDTO domainDTO) {
        DomainDTO createdDomain = domainService.createDomain(domainDTO);
        return ResponseEntity.ok(createdDomain);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DomainDTO> updateDomain(@PathVariable Long id, @RequestBody DomainDTO domainDTO) {
        Optional<DomainDTO> updatedDomain = domainService.updateDomain(id, domainDTO);
        return updatedDomain.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDomain(@PathVariable Long id) {
        if (domainService.deleteDomain(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
