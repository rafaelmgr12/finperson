package com.rafaelmgr12.finperson.dto;


import com.rafaelmgr12.finperson.entity.Revenue;
import com.rafaelmgr12.finperson.repository.RevenueRepository;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;

public class RevenueDTO {

    private UUID id;
    @NotEmpty(message = "Description is required")
    private String description;
    @NotEmpty(message = "Value is required")
    private Double value;
    @NotEmpty(message = "Date is required")
    private String date;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public RevenueDTO() {
    }

    public RevenueDTO(Revenue revenue){
        this.id = revenue.getId();
        this.description = revenue.getDescription();
        this.value = revenue.getValue();
        this.date = revenue.getDate().format(formatter);
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

    public boolean isRepeated(RevenueRepository revenueRepository) {
        LocalDate startDate = LocalDate.parse(date, formatter)
                .with(TemporalAdjusters.firstDayOfMonth());

        LocalDate endDate = LocalDate.parse(date, formatter)
                .with(TemporalAdjusters.lastDayOfMonth());

        return revenueRepository.findByDescriptionAndDateBetween(description, startDate, endDate).isPresent();

    }

    public Revenue update(UUID id, RevenueRepository revenueRepository) {
        Revenue revenue = revenueRepository.findById(id).get();
        revenue.setDescription(description);
        revenue.setValue(value);

        LocalDate localDate = LocalDate.parse(date, formatter);
        revenue.setDate(localDate);

        return  revenue;

    }
    public Revenue toRevenue(){
        Revenue revenue = new Revenue();
        revenue.setId(id);
        revenue.setDescription(description);
        revenue.setValue(value);
        revenue.setDate(LocalDate.parse(date, formatter));
        return revenue;
    }


    public static List<RevenueDTO> convert(List<Revenue> revenues){
        return revenues.stream().map(RevenueDTO::new).collect(java.util.stream.Collectors.toList());
    }
}
