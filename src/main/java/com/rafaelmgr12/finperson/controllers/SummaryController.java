package com.rafaelmgr12.finperson.controllers;

import com.rafaelmgr12.finperson.dto.SummaryDTO;
import com.rafaelmgr12.finperson.repository.ExpenseRepository;
import com.rafaelmgr12.finperson.repository.RevenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@RestController
@RequestMapping("/resumo")
public class SummaryController {

    @Autowired
    private RevenueRepository revenueRepository;
    @Autowired
    private ExpenseRepository expenseRepository;

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<?> summaryByMonth(@PathVariable Integer ano, @PathVariable Integer mes) {
        LocalDate startDate;
        try {
            startDate = LocalDate.of(ano, mes, 1);
        } catch (DateTimeException e) {
            return ResponseEntity.badRequest().build();
        }
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

        SummaryDTO summaryDTO = new SummaryDTO(startDate,endDate,revenueRepository,expenseRepository);

        return ResponseEntity.ok(summaryDTO);

    }
}
