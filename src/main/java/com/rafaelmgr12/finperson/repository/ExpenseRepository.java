package com.rafaelmgr12.finperson.repository;

import com.rafaelmgr12.finperson.dto.CategoryProjection;
import com.rafaelmgr12.finperson.entity.Expense;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense, UUID> {
    public Optional<Expense> findByDescriptionAndDateBetween(String description, LocalDate startDate, LocalDate endDate);
    public List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);
    public List<Expense> findByDescriptionContaining(String description);


    @Query("SELECT SUM(r.value) FROM Expense r WHERE r.date >= :startDate AND r.date <= :endDate")

    public Optional<BigDecimal> sumBetweenDate(LocalDate startDate, LocalDate endDate);
    @Query("SELECT d.category AS category, SUM(d.value) AS total FROM Expense d WHERE d.date >= :startDate AND d.date <= :endDate GROUP BY d.category")
    public List<CategoryProjection> countTotalExpenseByCategoryBetweenData(LocalDate startDate, LocalDate endDate);
}
