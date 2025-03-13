package com.example.futurumapi.dto;

import lombok.Data;
import java.util.List;

@Data
public class DomainDTO {
    private Long id;
    private String name;
    private List<Long> articleIds; // Assuming only IDs of articles are needed
}
