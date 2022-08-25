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
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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
    public List<ExpenseDTO> readAll(@RequestParam(required = false) String description){
        List<Expense> expenses;

        if (description == null){
            expenses = (List<Expense>) expenseRepository.findAll();}
        else {
            expenses = expenseRepository.findByDescriptionContaining(description);
        }

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

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<List<ExpenseDTO>> readByMonth(@PathVariable int ano, @PathVariable int mes){
       LocalDate startDate;
       try{
           startDate = LocalDate.of(ano, mes, 1);
       } catch (Exception e){
           return ResponseEntity.badRequest().build();
       }
       LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
         List<Expense> expenses = expenseRepository.findByDateBetween(startDate, endDate);
        return ResponseEntity.ok(ExpenseDTO.convert(expenses));


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
