package com.rabo.service.validate;

import com.rabo.model.CustomerStatement;
import com.rabo.repository.CustomerStatementRepository;
import com.rabo.rest.ValidationReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerStatementService {
    @Autowired
    private CustomerStatementValidator customerStatementValidator;
    @Autowired
    private CustomerStatementRepository customerStatementRepository;

    public ValidationReport validate(List<CustomerStatement> customerStatements, String fileName) {
        ValidationReport validationReport = new ValidationReport();
        validationReport.setFileName(fileName);

        customerStatements.stream()
                .map(statement -> customerStatementValidator.validate(statement, customerStatements))
                .filter(validatedStatement -> !validatedStatement.isValid())
                .map(validatedStatement -> validatedStatement.getCustomerStatement().asFaultyCustomerStatement())
                .forEach(validationReport::addValidatedStatement);

        if(validationReport.hasNoFaults()) {
            customerStatementRepository.saveAll(customerStatements);
        }

        return validationReport;
    }
}
