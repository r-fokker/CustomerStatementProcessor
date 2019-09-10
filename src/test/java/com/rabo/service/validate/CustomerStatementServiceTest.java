package com.rabo.service.validate;

import com.rabo.model.CustomerStatement;
import com.rabo.repository.CustomerStatementRepository;
import com.rabo.rest.ValidationReport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerStatementServiceTest {

    @Mock
    private CustomerStatementValidator customerStatementValidator;
    @Mock
    private CustomerStatementRepository customerStatementRepository;
    @Captor
    private ArgumentCaptor<List<CustomerStatement>> listArgumentCaptor;

    @InjectMocks
    private CustomerStatementService customerStatementService;

    private MockHelper mock;

    @Before
    public void setUp() {
        mock = new MockHelper();
        mock.valididations();
    }

    @Test
    public void validate_setsProvidedFileName() {
        ValidationReport validationReport = customerStatementService.validate(mock.oneValidStatement(), mock.fileName);

        assertThat(validationReport, is(notNullValue()));
        assertThat(validationReport.getFileName(), is(mock.fileName));
    }

    @Test
    public void validate_validatesEachCustomerStatement() {
        List<CustomerStatement> fourStatements = mock.fourStatements();
        customerStatementService.validate(fourStatements, mock.fileName);

        verify(customerStatementValidator, times(4)).validate(any(CustomerStatement.class), any(List.class));
        verify(customerStatementValidator).validate(eq(mock.validStatement1), eq(fourStatements));
        verify(customerStatementValidator).validate(eq(mock.validStatement2), eq(fourStatements));
        verify(customerStatementValidator).validate(eq(mock.inValidStatement3), eq(fourStatements));
        verify(customerStatementValidator).validate(eq(mock.inValidStatement4), eq(fourStatements));
    }

    @Test
    public void validate_returnsOnlyFaultyStatements() {
        List<CustomerStatement> fourStatements = mock.fourStatements();

        ValidationReport validationReport = customerStatementService.validate(fourStatements, mock.fileName);

        List<Long> expectedReferences = Arrays.asList(mock.inValidStatement3.getReference(), mock.inValidStatement4.getReference());
        assertThat(validationReport.getFaultyStatements().size(), is(2));
        assertThat(expectedReferences.contains(validationReport.getFaultyStatements().get(0).getTransactionReference()), is(true));
        assertThat(expectedReferences.contains(validationReport.getFaultyStatements().get(1).getTransactionReference()), is(true));
    }


    @Test
    public void validate_whenAllStatementsAreValid_persistsAllStatements() {
        List<CustomerStatement> fourStatements = mock.twoValidStatements();

        customerStatementService.validate(fourStatements, mock.fileName);

        verify(customerStatementRepository).saveAll(listArgumentCaptor.capture());

        List<CustomerStatement> capturedStatements = listArgumentCaptor.getValue();
        assertThat(capturedStatements.size(), is(2));
        assertThat(capturedStatements.contains(mock.validStatement1), is(true));
        assertThat(capturedStatements.contains(mock.validStatement2), is(true));
    }

    @Test
    public void validate_whenValidationReportHasFaults_persistsNothing() {
        List<CustomerStatement> fourStatements = mock.twoInValidStatements();

        customerStatementService.validate(fourStatements, mock.fileName);

        verify(customerStatementRepository, never()).saveAll(any(List.class));
    }

    class MockHelper {
        String fileName = "fileName";

        CustomerStatement validStatement1 = CustomerStatement.builder().reference(1L).description("d1").build();
        CustomerStatement validStatement2 = CustomerStatement.builder().reference(2L).description("d2").build();
        CustomerStatement inValidStatement3 = CustomerStatement.builder().reference(3L).description("d3").build();
        CustomerStatement inValidStatement4 = CustomerStatement.builder().reference(4L).description("d4").build();

        ValidatedStatement validatedStatement1 = ValidatedStatement.builder().customerStatement(validStatement1).isValid(true).build();
        ValidatedStatement validatedStatement2 = ValidatedStatement.builder().customerStatement(validStatement2).isValid(true).build();
        ValidatedStatement validatedStatement3 = ValidatedStatement.builder().customerStatement(inValidStatement3).isValid(false).build();
        ValidatedStatement validatedStatement4 = ValidatedStatement.builder().customerStatement(inValidStatement4).isValid(false).build();

        void valididations() {
            when(customerStatementValidator.validate(eq(validStatement1), any(List.class)))
                    .thenReturn(validatedStatement1);
            when(customerStatementValidator.validate(eq(validStatement2), any(List.class)))
                    .thenReturn(validatedStatement2);
            when(customerStatementValidator.validate(eq(inValidStatement3), any(List.class)))
                    .thenReturn(validatedStatement3);
            when(customerStatementValidator.validate(eq(inValidStatement4), any(List.class)))
                    .thenReturn(validatedStatement4);
        }

        List<CustomerStatement> oneValidStatement() {
            return Arrays.asList(validStatement1);
        }

        public List<CustomerStatement> fourStatements() {
            return Arrays.asList(validStatement1, validStatement2, inValidStatement3, inValidStatement4);
        }

        public List<CustomerStatement> twoValidStatements() {
            return Arrays.asList(validStatement1, validStatement2);
        }

        public List<CustomerStatement> twoInValidStatements() {
            return Arrays.asList(inValidStatement3, inValidStatement4);
        }

    }
}