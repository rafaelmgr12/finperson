package com.rafaelmgr12.finperson.repository;

import com.rafaelmgr12.finperson.entity.Expense;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense, UUID> {
    public Optional<Expense> findByDescriptionAndDateBetween(String description, LocalDate startDate, LocalDate endDate);
    public List<Expense> findByDescriptionContaining(String description);
}
