package com.rafaelmgr12.finperson.dto;


import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

public interface CategoryProjection {
    @Value("#{target.category.description}")
    String getCategoria();
    BigDecimal getTotal();
}
