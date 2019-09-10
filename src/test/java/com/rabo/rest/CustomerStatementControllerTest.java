package com.rabo.rest;

import com.rabo.CustomerStatementException;
import com.rabo.model.CustomerStatement;
import com.rabo.service.parse.CustomerStatementParser;
import com.rabo.service.validate.CustomerStatementService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerStatementControllerTest {
    @Mock
    private CustomerStatementParser customerStatementParser;
    @Mock
    private CustomerStatementService customerStatementService;
    @Mock
    private MultipartFile multipartFile;
    @Mock
    private InputStream inputStream;
    private MockHelper mock;

    @InjectMocks
    private CustomerStatementController customerStatementController;

    @Before
    public void setUp() {
        mock = new MockHelper();
    }

    @Test
    public void validate_parsesProvidedFile() throws IOException {
        String expectedFileName = mock.filename;
        mock.aMultiPartFile();

        customerStatementController.validate(multipartFile);

        verify(customerStatementParser).parse(eq(inputStream), eq(expectedFileName));
    }

    @Test
    public void validate_validatesParsedFileOnCustomerStatementService() throws IOException {
        mock.aMultiPartFile();
        mock.customerStatementServiceValidates();
        customerStatementController.validate(multipartFile);

        verify(customerStatementService).validate(eq(mock.customerStatements), eq(mock.filename));
    }

    @Test(expected = CustomerStatementException.class)
    public void validate_whneIOExceptionIsThrown_wrapsAndThrowsAsCustomerStatementException() throws IOException {
        when(multipartFile.getInputStream()).thenThrow(new IOException());

        customerStatementController.validate(multipartFile);
    }

    @Test
    public void validate_returnsValidationReport() throws IOException {
        ValidationReport expectedReport = mock.validationReport;
        mock.aMultiPartFile();
        mock.parsesToCustomerStatements();
        mock.customerStatementServiceValidates();

        ResponseEntity<ValidationReport> resultResponse = customerStatementController.validate(multipartFile);

        assertThat(resultResponse, is((notNullValue())));
        assertThat(resultResponse.getBody(), is(expectedReport));
        assertThat(resultResponse.getStatusCode(), is(HttpStatus.OK));
    }

    class MockHelper {
        String filename = "FileName";
        List<CustomerStatement> customerStatements = new ArrayList<>();
        ValidationReport validationReport = new ValidationReport();

        void aMultiPartFile() throws IOException {
            when(multipartFile.getOriginalFilename()).thenReturn(filename);
            when(multipartFile.getInputStream()).thenReturn(inputStream);
        }

        void parsesToCustomerStatements() {
            when(customerStatementParser.parse(any(InputStream.class), anyString()))
                    .thenReturn(mock.customerStatements);
        }

        void customerStatementServiceValidates() {
            when(customerStatementService.validate(anyList(), anyString()))
                    .thenReturn(validationReport);
        }
    }

}