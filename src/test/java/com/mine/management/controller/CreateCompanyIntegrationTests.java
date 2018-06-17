package com.mine.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mine.management.api.model.BeneficialOwner;
import com.mine.management.api.model.Company;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CreateCompanyIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void createCompanyWithoutOwners() throws Exception {
        Company newCompany = createCompany("CompanyName0");
        String json = mapper.writeValueAsString(newCompany);

        this.mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json)).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.name").value(newCompany.getName()))
                .andExpect(jsonPath("$.address").value(newCompany.getAddress()))
                .andExpect(jsonPath("$.city").value(newCompany.getCity()))
                .andExpect(jsonPath("$.country").value(newCompany.getCountry()))
                .andExpect(jsonPath("$.beneficialOwners").value(IsNull.nullValue()));
    }

    @Test
    public void createCompanyWithOwners() throws Exception {
        Company newCompany = createCompany("CompanyName1");
        BeneficialOwner bo = createBeneficialOwner("BOName1");
        Set<BeneficialOwner> bos = new HashSet<>();
        bos.add(bo);
        newCompany.setBeneficialOwners(bos);
        String json = mapper.writeValueAsString(newCompany);

        this.mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json)).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.name").value(newCompany.getName()))
                .andExpect(jsonPath("$.address").value(newCompany.getAddress()))
                .andExpect(jsonPath("$.city").value(newCompany.getCity()))
                .andExpect(jsonPath("$.country").value(newCompany.getCountry()))
                .andExpect(jsonPath("$.beneficialOwners[0].name").value(bo.getName()))
                .andExpect(jsonPath("$.beneficialOwners[0].id").value(IsNull.notNullValue()));
    }

    @Test
    public void createDuplicateCompanies() throws Exception {
        Company newCompany = createCompany("CompanyName2");
        String json = mapper.writeValueAsString(newCompany);

        this.mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json)).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        this.mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json)).andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").value("Company already exits"));
    }

    @Test
    public void createDifferentCompaniesWithSameOwner() throws Exception {
        Company newCompany = createCompany("CompanyName3");
        BeneficialOwner bo = createBeneficialOwner("BOName3");
        Set<BeneficialOwner> bos = new HashSet<>();
        bos.add(bo);
        newCompany.setBeneficialOwners(bos);
        String json = mapper.writeValueAsString(newCompany);
        this.mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json)).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.name").value(newCompany.getName()))
                .andExpect(jsonPath("$.beneficialOwners").value(IsNull.notNullValue()));
        newCompany = createCompany("CompanyName4");
        newCompany.setBeneficialOwners(bos);

        this.mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json)).andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").value("Beneficial owner already exits and it is assigned to a company."));
    }

    @Test
    public void createCompanyWithMissingMandatoryFields() throws Exception {
        Company newCompany = new Company();
        newCompany.setName("CompanyName5");
        newCompany.setAddress("Address");
        newCompany.setCity("City");

        String json = mapper.writeValueAsString(newCompany);
        this.mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json)).andExpect(status().isBadRequest());
    }

    public Company createCompany(String name) {
        Company newCompany = new Company();
        newCompany.setName(name);
        newCompany.setAddress("Address");
        newCompany.setCity("City");
        newCompany.setCountry("Country");
        return newCompany;
    }

    public BeneficialOwner createBeneficialOwner(String name) {
        BeneficialOwner newBo = new BeneficialOwner();
        newBo.setName(name);
        return newBo;
    }
}
