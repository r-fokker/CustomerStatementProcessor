package com.rabo.service.validate;

import com.rabo.model.CustomerStatement;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidatedStatement {
    private CustomerStatement customerStatement;
    private boolean isValid;
}
