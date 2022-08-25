package com.rafaelmgr12.finperson.repository;

import com.rafaelmgr12.finperson.entity.Revenue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RevenueRepository extends CrudRepository<Revenue, UUID> {
    public Optional<Revenue> findByDescriptionAndDateBetween(String description, LocalDate startDate, LocalDate endDate);
    public List<Revenue> findByDateBetween(LocalDate startDate, LocalDate endDate);
    public List<Revenue> findByDescriptionContaining(String description);
    @Query("SELECT SUM(r.value) FROM Revenue r WHERE r.date >= :startDate AND r.date <= :endDate")

    public Optional<BigDecimal>sumBetweenDate(LocalDate startDate, LocalDate endDate);

}
