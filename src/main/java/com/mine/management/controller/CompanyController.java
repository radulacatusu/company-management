package com.mine.management.controller;

import com.mine.management.BaseResult;
import com.mine.management.model.BeneficialOwner;
import com.mine.management.model.Company;
import com.mine.management.service.CompanyService;
import com.mine.management.utils.BeneficialOwnerConversion;
import com.mine.management.utils.CompanyConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller used to manage company
 */
@RestController
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyConversion companyConversion;

    @Autowired
    private BeneficialOwnerConversion beneficialOwnerConversion;

    @PostMapping(value = "companies")
    public ResponseEntity<?> createNewCompany(@Valid @RequestBody com.mine.management.api.model.Company company) {
        Company newCompany = companyConversion.getCompanyModel(company);
        BaseResult<?> result = companyService.save(newCompany);
        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getErrors(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(companyConversion.getCompanyApiModel((Company) result.getResult()), HttpStatus.CREATED);
    }

    /**
     * @return
     */
    @GetMapping("/companies/{id}")
    public ResponseEntity<?> getCompany(@PathVariable long id) {
        Optional<Company> company = companyService.findOne(id);
        if (company.isPresent()) {
            return new ResponseEntity<>(companyConversion.getCompanyApiModel(company.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>("Company with id " + id + " not found.", HttpStatus.NOT_FOUND);
    }

    /**
     * @return
     */
    @GetMapping("/companies")
    public ResponseEntity<?> getAllCompanies() {
        List<Company> all = companyService.findAll();
        return new ResponseEntity<>(all.stream().map(companyConversion::getCompanyApiModel)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity updateCompany(@PathVariable Long id,
                                        @Valid @RequestBody com.mine.management.api.model.Company company) {

        Company companyToUpdate = companyConversion.getCompanyModel(company);
        if (!CollectionUtils.isEmpty(companyToUpdate.getBeneficialOwners())) {
            return new ResponseEntity("Company can not be updated with beneficial owners. Please remove them.", HttpStatus.BAD_REQUEST);
        }
        BaseResult<?> result = companyService.update(id, companyToUpdate);

        if (!result.isSuccess()) {
            return new ResponseEntity(result.getErrors(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(companyConversion.getCompanyApiModel((Company) result.getResult()), HttpStatus.OK);
    }
    @PostMapping(value = "companies/{id}/owner")
    public ResponseEntity<?> addBeneficialOwners(@PathVariable Long id,
                                                 @Valid @RequestBody List<com.mine.management.api.model.BeneficialOwner> owners) {
        Set<BeneficialOwner> newBos = owners.stream().map(beneficialOwnerConversion::getCompanyModel).collect(Collectors.toSet());
        BaseResult<?> result = companyService.addBeneficialOwners(newBos, id);
        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getErrors(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(companyConversion.getCompanyApiModel((Company) result.getResult()), HttpStatus.OK);
    }


}
