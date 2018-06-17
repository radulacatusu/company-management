package com.mine.management.utils;

import com.mine.management.model.BeneficialOwner;
import com.mine.management.model.Company;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CompanyConversion {

    public Company getCompanyModel(com.mine.management.api.model.Company company) {
        Company newCompany;
        BeanUtils.copyProperties(company, newCompany = new Company());

        if (!CollectionUtils.isEmpty(company.getBeneficialOwners())) {
            Set<BeneficialOwner> bos = company.getBeneficialOwners().stream()
                    .map(bo -> new BeneficialOwner(bo.getName(), newCompany)).collect(Collectors.toSet());
            newCompany.setBeneficialOwners(bos);
        }
        return newCompany;
    }

    public com.mine.management.api.model.Company getCompanyApiModel(Company company) {
        com.mine.management.api.model.Company newCompany;
        BeanUtils.copyProperties(company, newCompany = new com.mine.management.api.model.Company());

        if (!CollectionUtils.isEmpty(company.getBeneficialOwners())) {
            Set<com.mine.management.api.model.BeneficialOwner> bos = company.getBeneficialOwners().stream()
                    .map(bo -> new com.mine.management.api.model.BeneficialOwner(bo.getId(),
                            bo.getName())).collect(Collectors.toSet());
            newCompany.setBeneficialOwners(bos);
        }
        return newCompany;
    }
}
