package com.example.futurumapi.web;

import com.example.futurumapi.dto.CreateDomainDTO;
import com.example.futurumapi.dto.DomainDTO;
import com.example.futurumapi.entities.Domain;
import com.example.futurumapi.services.Domain.DomainService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/domains")
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DomainDTO> createDomain( @RequestBody CreateDomainDTO domainDTO) {
        Domain domain = new Domain();
        domain.setName(domainDTO.getName());
        DomainDTO createdDomain = domainService.createDomain(domain);
        return ResponseEntity.ok(createdDomain);
    }
    @PutMapping("/{id}")
    public ResponseEntity<DomainDTO> updateDomain(@PathVariable Long id, @RequestBody DomainDTO domainDTO) {
        return domainService.updateDomain(id, domainDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDomain(@PathVariable Long id) {
        return domainService.deleteDomain(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}