package com.example.futurumapi.services.Domain;

import com.example.futurumapi.dao.DomainDAO;
import com.example.futurumapi.dto.DomainDTO;
import com.example.futurumapi.entities.Domain;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DomainServiceImpl implements DomainService {

    private final DomainDAO domainDAO;

    public DomainServiceImpl(DomainDAO domainDAO) {
        this.domainDAO = domainDAO;
    }

    @Override
    public List<DomainDTO> getAllDomains() {
        return domainDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<DomainDTO> getDomainById(Long id) {
        return domainDAO.findById(id).map(this::convertToDTO);
    }

    @Override
    public List<DomainDTO> getDomainsByNames(List<String> names) {
        return domainDAO.findByNames(names).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public DomainDTO createDomain(DomainDTO domainDTO) {
        Domain domain = new Domain();
        domain.setName(domainDTO.getName());
        Domain savedDomain = domainDAO.save(domain);
        return convertToDTO(savedDomain);
    }

    @Override
    public Optional<DomainDTO> updateDomain(Long id, DomainDTO domainDTO) {
        Optional<Domain> existingDomain = domainDAO.findById(id);
        if (existingDomain.isPresent()) {
            Domain domain = existingDomain.get();
            domain.setName(domainDTO.getName());
            Domain updatedDomain = domainDAO.save(domain);
            return Optional.of(convertToDTO(updatedDomain));
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteDomain(Long id) {
        if (domainDAO.findById(id).isPresent()) {
            domainDAO.deleteById(id);
            return true;
        }
        return false;
    }

    private DomainDTO convertToDTO(Domain domain) {
        DomainDTO dto = new DomainDTO();
        dto.setId(domain.getId());
        dto.setName(domain.getName());
        dto.setArticleIds(domain.getArticles().stream().map(article -> article.getId()).collect(Collectors.toList()));
        return dto;
    }
}
