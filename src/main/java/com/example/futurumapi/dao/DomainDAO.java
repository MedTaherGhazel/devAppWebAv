package com.example.futurumapi.dao;

import com.example.futurumapi.entities.Domain;
import com.example.futurumapi.repositories.DomainRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DomainDAO {

    private final DomainRepository domainRepository;

    public DomainDAO(DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    public List<Domain> findAll() {
        return domainRepository.findAll();
    }

    public Optional<Domain> findById(Long id) {
        return domainRepository.findById(id);
    }

    public List<Domain> findByNames(List<String> names) {
        return domainRepository.findByNameIn(names);
    }

    public Domain save(Domain domain) {
        return domainRepository.save(domain);
    }

    public void deleteById(Long id) {
        domainRepository.deleteById(id);
    }
}
