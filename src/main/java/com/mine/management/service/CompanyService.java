package com.mine.management.service;

import com.mine.management.BaseResult;
import com.mine.management.model.BeneficialOwner;
import com.mine.management.model.Company;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 *
 */
public interface CompanyService {

    /**
     * @param company
     * @return
     */
    BaseResult<?> save(Company company);

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
     * @param id
     * @return
     */
    BaseResult<?> update(long id, Company company);

    /**
     * @param boList
     * @param companyId
     * @return
     */
    BaseResult<?> addBeneficialOwners(Set<BeneficialOwner> boList, long companyId);

}
