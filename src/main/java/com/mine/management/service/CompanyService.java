package com.mine.management.service;

import com.mine.management.BaseResult;
import com.mine.management.model.BeneficialOwner;
import com.mine.management.model.Company;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface CompanyService {

    /**
     * @param company
     * @return
     */
    BaseResult<?> saveCompany(Company company);

    /**
     * @return
     */
    List<Company> findAll();

    /**
     * @param id
     * @return
     */
    Optional<Company> findOne(long id);

    /**
     * @param company
     * @return
     */
    boolean updateCompany(Company company);

    /**
     * @param boList
     * @param companyId
     * @return
     */
    Company addBeneficialOwners(List<BeneficialOwner> boList, long companyId);

}
