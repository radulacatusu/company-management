package com.mine.management.api.model;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Company API model
 */
public class Company {

    private long id;
    @NotNull
    private String name;
    @NotNull
    private String address;
    @NotNull
    private String city;
    @NotNull
    private String country;
    private String email;
    private String phoneNumber;
    private Set<BeneficialOwner> beneficialOwners;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<BeneficialOwner> getBeneficialOwners() {
        return beneficialOwners;
    }

    public void setBeneficialOwners(Set<BeneficialOwner> beneficialOwners) {
        this.beneficialOwners = beneficialOwners;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", beneficialOwners=" + beneficialOwners +
                '}';
    }
}
