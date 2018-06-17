package com.mine.management.model;

import javax.persistence.*;

/**
 * Table used to store beneficial owners
 */
@Entity
@Table(name = "BENEFICIAL_OWNER",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"NAME"})})
public class BeneficialOwner {

    public BeneficialOwner() {
    }

    public BeneficialOwner(String name, Company company) {
        this.name = name;
        this.company = company;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "BeneficialOwner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
