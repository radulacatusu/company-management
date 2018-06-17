package com.mine.management.service;

import com.mine.management.BaseResult;
import com.mine.management.model.BeneficialOwner;
import com.mine.management.model.Company;
import com.mine.management.repository.BeneficialOwnerRepository;
import com.mine.management.repository.CompanyRepository;
import com.mine.management.utils.CompanyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BeneficialOwnerRepository beneficialOwnerRepository;

    @Autowired
    private CompanyUtils companyUtils;

    @Override
    public BaseResult<?> save(Company company) {

        if (!CollectionUtils.isEmpty(company.getBeneficialOwners())) {
            BaseResult<?> existsOwnersResult = validateIfBeneficialOwnersExists(company.getBeneficialOwners());
            if (existsOwnersResult != null) return existsOwnersResult;
        }

        BaseResult<Company> result = new BaseResult<>();
        Optional<Company> existingCompany = companyRepository.findByName(company.getName());
        if (!existingCompany.isPresent()) {
            existingCompany = Optional.of(companyRepository.saveAndFlush(company));
        } else {
            result.addError("Company already exits");
        }
        result.setResult(existingCompany.get());
        return result;
    }

    private BaseResult<?> validateIfBeneficialOwnersExists(Set<BeneficialOwner> bos) {
        for (BeneficialOwner bo : bos) {
            Optional<BeneficialOwner> existingOwner = beneficialOwnerRepository.findByName(bo.getName());
            if (existingOwner.isPresent()) {
                BaseResult<BeneficialOwner> result = new BaseResult<>();
                result.addError("Beneficial owner already exits and it is assigned to a company.");
                result.setResult(existingOwner.get());
                return result;
            }
        }
        return null;
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public Optional<Company> findOne(long id) {
        return companyRepository.findById(id);
    }

    @Override
    public BaseResult<?> update(long id, Company update) {
        BaseResult<Company> result = new BaseResult<>();

        Optional<Company> current = companyRepository.findById(id);
        if (current.isPresent()) {
            companyUtils.applyUpdate(current.get(), update);
            companyRepository.save(current.get());
            result.setResult(current.get());
        } else {
            result.addError("Company does not exist.");
        }

        return result;
    }

    @Override
    public BaseResult<?> addBeneficialOwners(Set<BeneficialOwner> boList, long companyId) {

        BaseResult<?> existsOwnersResult = validateIfBeneficialOwnersExists(boList);
        if (existsOwnersResult != null) return existsOwnersResult;

        BaseResult<Company> result = new BaseResult<>();
        Optional<Company> current = companyRepository.findById(companyId);
        if (current.isPresent()) {
            for(BeneficialOwner bo : boList){
                bo.setCompany(current.get());
            }
            current.get().getBeneficialOwners().addAll(boList);
            companyRepository.save(current.get());
            result.setResult(current.get());
        } else {
            result.addError("Company does not exits.");
        }
        return result;
    }
}
