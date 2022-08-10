package com.rafaelmgr12.finperson.dto;

import com.rafaelmgr12.finperson.entity.Expense;
import com.rafaelmgr12.finperson.repository.ExpenseRepository;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ExpenseDTO {
    private UUID id;
    @NotEmpty(message = "Description is required")
    private String description;
    @NotEmpty(message = "Value is required")
    private Double value;
    @NotEmpty(message = "Date is required")
    private String date;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ExpenseDTO() {
    }

    public ExpenseDTO(Expense expense){
        this.id = expense.getId();
        this.description = expense.getDescription();
        this.value = expense.getValue();
        this.date = expense.getDate().format(formatter);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isRepeated(ExpenseRepository expenseRepository){
        LocalDate startDate = LocalDate.parse(date, formatter).with(TemporalAdjusters.lastDayOfMonth());
        LocalDate endDate = LocalDate.parse(date, formatter).with(TemporalAdjusters.firstDayOfNextMonth());

        return expenseRepository.findByDescriptionAndDateBetween(description, startDate, endDate).isPresent();

    }
    
    public Expense update(UUID id, ExpenseRepository expenseRepository){
        Expense expense = expenseRepository.findById(id).get();
        expense.setDescription(description);
        expense.setValue(value);
        expense.setDate(LocalDate.parse(date, formatter));
        return expense;
    }

    public Expense toExpense(){
        Expense expense = new Expense();
        expense.setId(id);
        expense.setDescription(description);
        expense.setValue(value);
        expense.setDate(LocalDate.parse(date, formatter));
        return expense;
    }

    public static List<ExpenseDTO> convert(List<Expense> expense){
        return expense.stream().map(ExpenseDTO::new).collect(Collectors.toList());
    }
}
