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
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AddOwnerCompanyIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void addOwnerCompany() throws Exception {
        Company newCompany = createCompany("CompanyName40");
        String json = mapper.writeValueAsString(newCompany);

        MvcResult result = this.mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.beneficialOwners").value(IsNull.nullValue())).andReturn();

        Company createdCompany = mapper.readValue(result.getResponse().getContentAsString(), Company.class);

        BeneficialOwner bo = createBeneficialOwner("BOName40");
        Set<BeneficialOwner> bos = new HashSet<>();
        bos.add(bo);
        json = mapper.writeValueAsString(bos);

        this.mockMvc.perform(post("/companies/{id}/owner", createdCompany.getId()).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json)).andExpect(status().isOk())
                .andExpect(jsonPath("$.beneficialOwners").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.beneficialOwners[0].name").value(bo.getName()));
    }

    @Test
    public void addExistingOwnerCompany() throws Exception {
        Company newCompany = createCompany("CompanyName41");
        BeneficialOwner bo = createBeneficialOwner("BOName41");
        Set<BeneficialOwner> bos = new HashSet<>();
        bos.add(bo);
        newCompany.setBeneficialOwners(bos);
        String json = mapper.writeValueAsString(newCompany);

        MvcResult result = this.mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.beneficialOwners").value(IsNull.notNullValue())).andReturn();

        Company createdCompany = mapper.readValue(result.getResponse().getContentAsString(), Company.class);

        json = mapper.writeValueAsString(bos);

        this.mockMvc.perform(post("/companies/{id}/owner", createdCompany.getId()).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json)).andExpect(status().isConflict())
                .andExpect(jsonPath("$").value("Beneficial owner already exits and it is assigned to a company."));
    }

    @Test
    public void addOwnerNonExistingCompany() throws Exception {
        Company newCompany = createCompany("CompanyName42");
        String json = mapper.writeValueAsString(newCompany);

        MvcResult result = this.mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.beneficialOwners").value(IsNull.nullValue())).andReturn();

        Company createdCompany = mapper.readValue(result.getResponse().getContentAsString(), Company.class);

        BeneficialOwner bo = createBeneficialOwner("BOName45");
        Set<BeneficialOwner> bos = new HashSet<>();
        bos.add(bo);
        json = mapper.writeValueAsString(bos);

        this.mockMvc.perform(post("/companies/{id}/owner", 60002).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json))
                .andExpect(jsonPath("$").value("Company does not exits."));
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
