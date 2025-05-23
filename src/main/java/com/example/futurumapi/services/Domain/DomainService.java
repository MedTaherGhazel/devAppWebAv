package com.example.futurumapi.services.Domain;

import com.example.futurumapi.dto.DomainDTO;
import com.example.futurumapi.entities.Domain;

import java.util.List;
import java.util.Optional;

public interface DomainService {
    List<DomainDTO> getAllDomains();
    Optional<DomainDTO> getDomainById(Long id);
    List<DomainDTO> getDomainsByNames(List<String> names);
    DomainDTO createDomain(Domain domain);
    Optional<DomainDTO> updateDomain(Long id, DomainDTO domainDTO);
    boolean deleteDomain(Long id);
}
