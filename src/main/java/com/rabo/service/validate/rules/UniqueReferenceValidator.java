package com.rabo.service.validate.rules;

import com.rabo.model.CustomerStatement;
import com.rabo.repository.CustomerStatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UniqueReferenceValidator {
    @Autowired
    private CustomerStatementRepository customerStatementRepository;

    public boolean transactionReferenceIsUnique(CustomerStatement customerStatement, List<CustomerStatement> newCustomerStatements) {
        boolean isInRepository = customerStatementRepository.findById(customerStatement.getReference()).isPresent();
        boolean isUniqueWithinNewStatements = newCustomerStatements.stream()
                .filter(cs -> customerStatement.getReference().equals(cs.getReference()))
                .count() == 1;

        return !isInRepository && isUniqueWithinNewStatements;
    }
}
