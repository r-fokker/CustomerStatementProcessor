package com.rabo.service.validate.rules;

import com.rabo.model.CustomerStatement;
import org.springframework.stereotype.Component;

@Component
public class BalanceValidator {
    public boolean endBalanceEqualsSumStartAndMutation(CustomerStatement customerStatement) {
        return customerStatement
                .getStartBalance()
                .add(customerStatement.getMutation())
                .equals(customerStatement.getEndBalance());
    }
}
