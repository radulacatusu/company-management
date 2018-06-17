package com.mine.management.service;

import com.mine.management.BaseResult;
import com.mine.management.model.BeneficialOwner;
import com.mine.management.model.Company;
import com.mine.management.repository.BeneficialOwnerRepository;
import com.mine.management.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BeneficialOwnerRepository beneficialOwnerRepository;

    @Override
    public BaseResult<?> saveCompany(Company company) {

        BaseResult<?> existsOwnersResult = validateIfBeneficialOwnersExists(company);
        if (existsOwnersResult != null) return existsOwnersResult;

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

    private BaseResult<?> validateIfBeneficialOwnersExists(Company company) {
        if (!CollectionUtils.isEmpty(company.getBeneficialOwners())) {
            for (BeneficialOwner bo : company.getBeneficialOwners()) {
                Optional<BeneficialOwner> existingOwner = beneficialOwnerRepository.findByName(bo.getName());
                if (existingOwner.isPresent()) {
                    BaseResult<BeneficialOwner> result = new BaseResult<>();
                    result.addError("Beneficial owner already exits");
                    result.setResult(existingOwner.get());
                    return result;
                }
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
    public boolean updateCompany(Company company) {
        return false;
    }

    @Override
    public Company addBeneficialOwners(List<BeneficialOwner> boList, long companyId) {
        return null;
    }
}
