package com.mine.management.api.model;

import javax.validation.Valid;

/**
 * Beneficial Owner API model
 */
public class BeneficialOwner {

    private long id;
    @Valid
    private String name;

    public BeneficialOwner() {
    }

    public BeneficialOwner(long id, String name) {
        this.id = id;
        this.name = name;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BeneficialOwner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
