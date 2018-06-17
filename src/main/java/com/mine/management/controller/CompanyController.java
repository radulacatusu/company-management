package com.mine.management.controller;

import com.mine.management.model.Company;
import com.mine.management.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CompanyController {

    @Autowired
    CompanyService companyService;

    /**
     * @return
     */
    @GetMapping("/companies/{name}")
    public ResponseEntity<Company> getCompany(@PathVariable String name) {
        Company x = new Company();
        x.setName(name);
        return new ResponseEntity<>(x, HttpStatus.OK);
    }

    /**
     * @return
     */
    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> all = new ArrayList<>();

        Company x = new Company();
        x.setName("new C");
        all.add(x);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }
}
