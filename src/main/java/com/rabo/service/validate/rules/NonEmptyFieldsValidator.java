package com.rabo.service.validate.rules;

import com.rabo.model.CustomerStatement;
import org.springframework.stereotype.Component;

@Component
public class NonEmptyFieldsValidator {
    public boolean noEmptFields(CustomerStatement customerStatement) {
        return customerStatement.getReference() != null
                && customerStatement.getAccountNumber() != null && !customerStatement.getAccountNumber().isEmpty()
                && customerStatement.getDescription() != null && !customerStatement.getDescription().isEmpty()
                && customerStatement.getStartBalance() != null
                && customerStatement.getMutation() != null
                && customerStatement.getEndBalance() != null;
    }
}
