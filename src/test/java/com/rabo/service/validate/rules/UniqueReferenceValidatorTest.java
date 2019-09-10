package com.rabo.service.validate.rules;

import com.rabo.model.CustomerStatement;
import com.rabo.repository.CustomerStatementRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UniqueReferenceValidatorTest {

    @Mock
    private CustomerStatementRepository customerStatementRepository;

    @InjectMocks
    private UniqueReferenceValidator uniqueReferenceValidator;

    @Test
    public void transactionReferenceIsUnique_whenDoesNotExistInRepository_isValid() {
        CustomerStatement customerStatement = CustomerStatement.builder()
                .reference(1L)
                .build();
        when(customerStatementRepository.findById(anyLong())).thenReturn(Optional.empty());

        List<CustomerStatement> customerStatements = Arrays.asList(customerStatement);

        boolean isUnique = uniqueReferenceValidator.transactionReferenceIsUnique(customerStatement, customerStatements);

        assertThat(isUnique, is(true));
    }


    @Test
    public void transactionReferenceIsUnique_whenExistInRepository_isInValid() {
        CustomerStatement customerStatement = CustomerStatement.builder()
                .reference(1L)
                .build();
        when(customerStatementRepository.findById(anyLong())).thenReturn(Optional.of(customerStatement));

        List<CustomerStatement> customerStatements = Arrays.asList(customerStatement);

        boolean isUnique = uniqueReferenceValidator.transactionReferenceIsUnique(customerStatement, customerStatements);

        assertThat(isUnique, is(false));
    }

    @Test
    public void transactionReferenceIsUnique_whenReferenceIsUniqueInProvidedList_isValid() {
        CustomerStatement customerStatement = CustomerStatement.builder().reference(1L).build();
        CustomerStatement customerStatement2 = CustomerStatement.builder().reference(2L).build();

        when(customerStatementRepository.findById(anyLong())).thenReturn(Optional.empty());

        List<CustomerStatement> customerStatements = Arrays.asList(customerStatement, customerStatement2);

        boolean isUnique = uniqueReferenceValidator.transactionReferenceIsUnique(customerStatement, customerStatements);

        assertThat(isUnique, is(true));
    }

    @Test
    public void transactionReferenceIsUnique_whenReferenceExistsTwiceInProvidedList_isInValid() {
        CustomerStatement customerStatement = CustomerStatement.builder().reference(1L).build();
        CustomerStatement customerStatement_sameReference = CustomerStatement.builder().reference(1L).build();

        when(customerStatementRepository.findById(anyLong())).thenReturn(Optional.empty());

        List<CustomerStatement> customerStatements = Arrays.asList(customerStatement, customerStatement_sameReference);

        boolean isUnique = uniqueReferenceValidator.transactionReferenceIsUnique(customerStatement, customerStatements);

        assertThat(isUnique, is(false));
    }
}