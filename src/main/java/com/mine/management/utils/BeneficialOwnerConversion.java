package com.mine.management.utils;

import com.mine.management.model.BeneficialOwner;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class BeneficialOwnerConversion {

    /**
     * @param bo
     * @return
     */
    public BeneficialOwner getCompanyModel(com.mine.management.api.model.BeneficialOwner bo) {
        BeneficialOwner newBo;
        BeanUtils.copyProperties(bo, newBo = new BeneficialOwner());
        return newBo;
    }
}
