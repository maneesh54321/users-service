package org.service.user.validation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    @Getter
    @Setter
    private boolean valid = true;
    @Getter
    private List<String> errors;

    public void addError(String error){
        if(errors == null){
            errors = new ArrayList<>();
        }
        errors.add(error);
    }

    public String getErrorMessage(){
        return CollectionUtils.isEmpty(errors)? "" : String.join(",", errors);
    }
}
