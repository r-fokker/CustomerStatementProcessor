package com.rabo.service.validate;

import com.rabo.model.CustomerStatement;
import com.rabo.service.validate.rules.BalanceValidator;
import com.rabo.service.validate.rules.NonEmptyFieldsValidator;
import com.rabo.service.validate.rules.UniqueReferenceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerStatementValidator {
    @Autowired
    private BalanceValidator balanceValidator;
    @Autowired
    private NonEmptyFieldsValidator nonEmptyFieldsValidator;
    @Autowired
    private UniqueReferenceValidator uniqueReferenceValidator;

    public ValidatedStatement validate(CustomerStatement customerStatement, List<CustomerStatement> customerStatements) {
        ValidatedStatement.ValidatedStatementBuilder builder = ValidatedStatement.builder()
                .customerStatement(customerStatement);

        boolean isValid = nonEmptyFieldsValidator.noEmptFields(customerStatement)
                            && balanceValidator.endBalanceEqualsSumStartAndMutation(customerStatement)
                            && uniqueReferenceValidator.transactionReferenceIsUnique(customerStatement, customerStatements);
        builder.isValid(isValid);

        return builder.build();
    }
}
