package com.rafaelmgr12.finperson.controllers;

import com.rafaelmgr12.finperson.dto.ExpenseDTO;
import com.rafaelmgr12.finperson.entity.Expense;
import com.rafaelmgr12.finperson.repository.ExpenseRepository;
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
@RequestMapping("/despesas")
public class ExpenseController {
    @Autowired
    private ExpenseRepository expenseRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<?> save(@Valid @RequestBody ExpenseDTO expenseDTO, UriComponentsBuilder uriBuilder){
        if (expenseDTO.isRepeated(expenseRepository)){
            return ResponseEntity.badRequest().body("Despesa já cadastrada para este período");
        }
        Expense expense = expenseDTO.toExpense();
        Expense savedItem = expenseRepository.save(expense);

        URI uri = uriBuilder.path("/despesas/{id}").buildAndExpand(savedItem.getId()).toUri();
        return ResponseEntity.created(uri).body(new ExpenseDTO(savedItem));
    }

    @GetMapping
    public List<ExpenseDTO> readAll(){
        List<Expense> expenses = (List<Expense>) expenseRepository.findAll();
        return ExpenseDTO.convert(expenses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> readOne(@PathVariable String id){

        Optional<Expense> expense = expenseRepository.findById(UUID.fromString(id));
        if (expense.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ExpenseDTO(expense.get()));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateExepnse(@PathVariable String id, @Valid @RequestBody ExpenseDTO expenseDTO){
        Optional<Expense> expense = expenseRepository.findById(UUID.fromString(id));
        if (!expense.isPresent()){
            return ResponseEntity.notFound().build();
        }
        if (expenseDTO.isRepeated(expenseRepository)){
            return ResponseEntity.badRequest().body("Despesa já cadastrada para este período");
        }
        Expense updateItem = expenseDTO.update(UUID.fromString(id), expenseRepository);
        return ResponseEntity.ok(new ExpenseDTO(updateItem));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable String id){
        Optional<Expense> expense = expenseRepository.findById(UUID.fromString(id));
        if (!expense.isPresent()){
            return ResponseEntity.notFound().build();
        }
        expenseRepository.delete(expense.get());
        return ResponseEntity.ok().build();
    }

}
