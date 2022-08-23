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
import java.util.Optional;
import java.util.UUID;

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
    public List<RevenueDTO> readAll(@RequestParam(required = false) String description){
        List<Revenue> revenues;

        if (description==null){
            revenues = (List<Revenue>) revenueRepository.findAll();
        } else {
            revenues = revenueRepository.findByDescriptionContaining(description);
        }

        return RevenueDTO.convert(revenues);
    }






    @GetMapping("/{id}")
    public ResponseEntity<RevenueDTO> readOne(@PathVariable String id) {

        Optional<Revenue> revenue = revenueRepository.findById(UUID.fromString(id));
        if (revenue.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new RevenueDTO(revenue.get()));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateRevenue(@PathVariable String id, @Valid @RequestBody RevenueDTO revenueDTO) {
        Optional<Revenue> revenue = revenueRepository.findById(UUID.fromString(id));
        if (!revenue.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        if (revenueDTO.isRepeated(revenueRepository)){
            return ResponseEntity.badRequest().body("Receita já cadastrada para este período");
        }
        Revenue updateItem = revenueDTO.update(UUID.fromString(id), revenueRepository);
        return ResponseEntity.ok(new RevenueDTO(updateItem));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteRevenue(@PathVariable String id) {
        Optional<Revenue> revenue = revenueRepository.findById(UUID.fromString(id));
        if (!revenue.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        revenueRepository.delete(revenue.get());
        return ResponseEntity.ok().build();
    }

}
