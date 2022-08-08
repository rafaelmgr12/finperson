package com.rafaelmgr12.finperson.controllers;


import com.rafaelmgr12.finperson.dto.RevenueDTO;
import com.rafaelmgr12.finperson.entity.Revenue;
import com.rafaelmgr12.finperson.repository.RevenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/receitas")
public class RevenueController {
    @Autowired
    private RevenueRepository revenueRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<?> save(@Valid @RequestBody RevenueDTO revenueDTO, UriComponentsBuilder uriBuilder) {
       if (revenueDTO.isRepeated(revenueRepository)){
              return ResponseEntity.badRequest().body("Receita já cadastrada para este período");
        }
        Revenue revenue = revenueDTO.toRevenue();
        Revenue savedItem = revenueRepository.save(revenue);

        URI uri = uriBuilder.path("/receitas/{id}").buildAndExpand(savedItem.getId()).toUri();
        return ResponseEntity.created(uri).body(new RevenueDTO(savedItem));
    }

    @GetMapping
    public List<RevenueDTO> readAll() {
        List<Revenue> revenues = (List<Revenue>) revenueRepository.findAll();
        return RevenueDTO.convert(revenues);
    }
}
