package com.rafaelmgr12.finperson.entity;

public enum Category {
    ALIEMENTACAO("Alimentação"),
    SAUDE("Saúde"),
    MORADIA("Moradia"),
    TRANSPORTE("Transporte"),
    EDUCACAO("Educação"),
    LAZER("Lazer"),
    IMPREVISTOS("Imprevistos"),
    OUTROS("Outros");

    private final String description;

    Category(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;

    }


}
