package com.mine.management.repository;

import com.mine.management.model.BeneficialOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BeneficialOwnerRepository extends JpaRepository<BeneficialOwner, Long> {

    Optional<BeneficialOwner> findByName(String name);
}
