package com.mine.management.utils;

import com.mine.management.model.Company;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CompanyUtils {

    /**
     * @param r target
     * @param update update to apply
     */
    public static void applyUpdate(Company r, Company update){
        Long id;
        if(r == null){
            throw new IllegalArgumentException("No company given");
        }
        if(update == null){
            update = new Company();
        }
        id = r.getId();

        BeanUtils.copyProperties(update, r);

        //if the a already has an id leave it alone, otherwise set the one what came wih the update
        r.setId(id != null ? id : update.getId());

        if(id == null){
            r.setId(update.getId());
        }else{
            //leave the id untouched
            r.setId(id);
        }
    }


    /**
     * @param source
     * @return
     */
    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
