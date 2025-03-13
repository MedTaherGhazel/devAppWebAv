package com.example.futurumapi.repositories;

import com.example.futurumapi.entities.Article;
import com.example.futurumapi.entities.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long> {
    List<Domain> findByNameIn(List<String> names);
    Optional<Domain> findByName(String name);
}
