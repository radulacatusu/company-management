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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UpdateCompanyIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void updateCompanyWithoutOwners() throws Exception {
        Company newCompany = createCompany("CompanyName30");
        String json = mapper.writeValueAsString(newCompany);

        MvcResult result = this.mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json)).andExpect(status().isCreated()).andReturn();

        Company createdCompany = mapper.readValue(result.getResponse().getContentAsString(), Company.class);

        newCompany.setAddress("New Address");
        newCompany.setCity("New City");
        newCompany.setCountry("New Country");
        json = mapper.writeValueAsString(newCompany);

        this.mockMvc.perform(put("/companies/{id}", createdCompany.getId()).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(createdCompany.getId()))
                .andExpect(jsonPath("$.name").value(createdCompany.getName()))
                .andExpect(jsonPath("$.city").value(newCompany.getCity()))
                .andExpect(jsonPath("$.address").value(newCompany.getAddress()))
                .andExpect(jsonPath("$.city").value(newCompany.getCity()))
                .andExpect(jsonPath("$.country").value(newCompany.getCountry()))
                .andExpect(jsonPath("$.beneficialOwners").value(IsNull.nullValue()));
    }

    @Test
    public void updateCompanyWithOwners() throws Exception {
        Company newCompany = createCompany("CompanyName31");
        BeneficialOwner bo = createBeneficialOwner("BOName31");
        Set<BeneficialOwner> bos = new HashSet<>();
        bos.add(bo);
        newCompany.setBeneficialOwners(bos);
        String json = mapper.writeValueAsString(newCompany);

        MvcResult result = this.mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json)).andExpect(status().isCreated()).andReturn();

        Company createdCompany = mapper.readValue(result.getResponse().getContentAsString(), Company.class);

        bo = createBeneficialOwner("BOName32");
        bos = new HashSet<>();
        bos.add(bo);
        newCompany.setBeneficialOwners(bos);

        json = mapper.writeValueAsString(newCompany);

        this.mockMvc.perform(put("/companies/{id}", createdCompany.getId()).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Company can not be updated with beneficial owners. Please remove them."));
    }

    @Test
    public void updateNotExistingCompany() throws Exception {
        Company newCompany = createCompany("CompanyName32");
        String json = mapper.writeValueAsString(newCompany);

        this.mockMvc.perform(put("/companies/{id}", 60001).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json)).andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Company does not exist."));
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
