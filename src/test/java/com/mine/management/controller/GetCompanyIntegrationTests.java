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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GetCompanyIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void getNonExistingCompany() throws Exception {
        this.mockMvc.perform(get("/companies/{id}", "60000")).andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Company with id 60000 not found."));
    }

    @Test
    public void getCompanyWithoutOwners() throws Exception {
        Company newCompany = createCompany("CompanyName10");
        String json = mapper.writeValueAsString(newCompany);

        MvcResult result = this.mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json)).andExpect(status().isCreated()).andReturn();

        Company createdCompany = mapper.readValue(result.getResponse().getContentAsString(), Company.class);

        this.mockMvc.perform(get("/companies/{id}", createdCompany.getId()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(createdCompany.getId()))
                .andExpect(jsonPath("$.name").value(newCompany.getName()))
                .andExpect(jsonPath("$.beneficialOwners").value(new ArrayList<>()));
    }

    @Test
    public void getCompanyWithOwners() throws Exception {
        Company newCompany = createCompany("CompanyName11");
        BeneficialOwner bo = createBeneficialOwner("BOName11");
        Set<BeneficialOwner> bos = new HashSet<>();
        bos.add(bo);
        newCompany.setBeneficialOwners(bos);
        String json = mapper.writeValueAsString(newCompany);

        MvcResult result = this.mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json)).andExpect(status().isCreated()).andReturn();

        Company createdCompany = mapper.readValue(result.getResponse().getContentAsString(), Company.class);

        this.mockMvc.perform(get("/companies/{id}", createdCompany.getId()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(createdCompany.getId()))
                .andExpect(jsonPath("$.name").value(newCompany.getName()))
                .andExpect(jsonPath("$.beneficialOwners[0].name").value(bo.getName()))
                .andExpect(jsonPath("$.beneficialOwners[0].id").value(IsNull.notNullValue()));
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
