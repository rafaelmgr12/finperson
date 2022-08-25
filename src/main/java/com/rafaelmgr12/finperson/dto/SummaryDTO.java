package com.rafaelmgr12.finperson.dto;

import com.rafaelmgr12.finperson.repository.ExpenseRepository;
import com.rafaelmgr12.finperson.repository.RevenueRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class SummaryDTO {
    private BigDecimal totalRevenue;
    private BigDecimal totalExpense;
    private BigDecimal Balance;

    private List<CategoryProjection> totalExpenseByCategory;

    public SummaryDTO(LocalDate startDate, LocalDate endDate, RevenueRepository revenueRepository, ExpenseRepository expenseRepository) {
        this.totalRevenue =  revenueRepository.sumBetweenDate(startDate, endDate).orElse(BigDecimal.ZERO);
        this.totalExpense =  expenseRepository.sumBetweenDate(startDate, endDate).orElse(BigDecimal.ZERO);
        this.Balance = this.totalRevenue.subtract(this.totalExpense);

        this.totalExpenseByCategory = expenseRepository.countTotalExpenseByCategoryBetweenData(startDate, endDate);
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public BigDecimal getBalance() {
        return Balance;
    }

    public List<CategoryProjection> getTotalExpenseByCategory() {
        return totalExpenseByCategory;
    }
}
