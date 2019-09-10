package com.rabo.service.validate;

import com.rabo.model.CustomerStatement;
import com.rabo.service.validate.rules.BalanceValidator;
import com.rabo.service.validate.rules.NonEmptyFieldsValidator;
import com.rabo.service.validate.rules.UniqueReferenceValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerStatementValidatorTest {

    @Mock
    private BalanceValidator balanceValidator;
    @Mock
    private NonEmptyFieldsValidator nonEmptyFieldsValidator;
    @Mock
    private UniqueReferenceValidator uniqueReferenceValidator;

    @InjectMocks
    private CustomerStatementValidator customerStatementValidator;

    @Test
    public void validate_validatedStatementContainsOriginalStatement() {
        CustomerStatement customerStatement = new CustomerStatement();
        List<CustomerStatement> customerStatements = Arrays.asList(customerStatement);

        ValidatedStatement validatedStatement = customerStatementValidator.validate(customerStatement, customerStatements);

        assertThat(validatedStatement.getCustomerStatement(), is(customerStatement));
    }

    @Test
    public void validate_checksNonEmptyFields() {
        emptyFieldValidator(false);
        balanceValidator(true);
        referenceValidator(true);

        CustomerStatement customerStatement = new CustomerStatement();
        List<CustomerStatement> customerStatements = Arrays.asList(customerStatement);

        customerStatementValidator.validate(customerStatement, customerStatements);

        verify(nonEmptyFieldsValidator).noEmptFields(customerStatement);
    }

    @Test
    public void validate_checksBalance() {
        emptyFieldValidator(true);
        balanceValidator(false);
        referenceValidator(true);
        CustomerStatement customerStatement = new CustomerStatement();
        List<CustomerStatement> customerStatements = Arrays.asList(customerStatement);

        customerStatementValidator.validate(customerStatement, customerStatements);

        verify(balanceValidator).endBalanceEqualsSumStartAndMutation(customerStatement);
    }

    @Test
    public void validate_checksReference() {
        emptyFieldValidator(true);
        balanceValidator(true);
        referenceValidator(false);
        CustomerStatement customerStatement = new CustomerStatement();
        List<CustomerStatement> customerStatements = Arrays.asList(customerStatement);

        customerStatementValidator.validate(customerStatement, customerStatements);

        verify(uniqueReferenceValidator).transactionReferenceIsUnique(customerStatement, customerStatements);
    }

    void emptyFieldValidator(boolean isValid) {
        when(nonEmptyFieldsValidator.noEmptFields(any(CustomerStatement.class))).thenReturn(isValid);
    }
    void balanceValidator(boolean isValid) {
        when(balanceValidator.endBalanceEqualsSumStartAndMutation(any(CustomerStatement.class))).thenReturn(isValid);
    }
    void referenceValidator(boolean isValid) {
        when(uniqueReferenceValidator.transactionReferenceIsUnique(any(CustomerStatement.class), any(List.class))).thenReturn(isValid);
    }
}