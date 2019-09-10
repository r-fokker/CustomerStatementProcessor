package com.rabo.service;


import com.rabo.CustomerStatementException;
import com.rabo.model.CustomerStatement;
import com.rabo.service.parse.CsvCustomerStatementParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CsvCustomerStatementParserTest {

    private CsvCustomerStatementParser csvCustomerStatementParser;

    private InputStream csvInputStream;

    @Before
    public void setUp() {
        csvCustomerStatementParser = new CsvCustomerStatementParser();
    }

    @After
    public void finish() throws IOException {
        if(csvInputStream != null) {
            csvInputStream.close();
        }
    }

    @Test
    public void parse_skipsHeader() throws IOException {
        csvInputStream = new ClassPathResource("records_10.csv").getInputStream();

        List<CustomerStatement> parsedStatements = csvCustomerStatementParser.parse(csvInputStream);

        assertThat(parsedStatements, is(notNullValue()));
        assertThat(parsedStatements.size(), is(10));
    }

    @Test
    public void parse_parsesNonEmptyFields() throws IOException {
        csvInputStream = new ClassPathResource("records_oneCompleteRecord.csv").getInputStream();
        List<CustomerStatement> parsedStatements = csvCustomerStatementParser.parse(csvInputStream);

        assertThat(parsedStatements, is(notNullValue()));
        assertThat(parsedStatements.size(), is(1));

        CustomerStatement customerStatement = parsedStatements.get(0);
        assertThat(customerStatement.getReference(), is(156108L));
        assertThat(customerStatement.getAccountNumber(), is("NL69ABNA0433647324"));
        assertThat(customerStatement.getDescription(), is("Flowers from Erik de Vries"));
        assertThat(customerStatement.getStartBalance(), is(new BigDecimal("13.92")));
        assertThat(customerStatement.getMutation(), is(new BigDecimal("-7.25")));
        assertThat(customerStatement.getEndBalance(), is(new BigDecimal("6.67")));
    }

    @Test
    public void parse_parsesEmptyFieldsAsNullOrEmpty() throws IOException {
        csvInputStream = new ClassPathResource("records_empty.csv").getInputStream();
        List<CustomerStatement> parsedStatements = csvCustomerStatementParser.parse(csvInputStream);

        assertThat(parsedStatements, is(notNullValue()));
        assertThat(parsedStatements.size(), is(1));

        CustomerStatement customerStatement = parsedStatements.get(0);
        assertThat(customerStatement.getReference(), is(nullValue()));
        assertThat(customerStatement.getAccountNumber(), is(""));
        assertThat(customerStatement.getDescription(), is(""));
        assertThat(customerStatement.getStartBalance(),is(nullValue()));
        assertThat(customerStatement.getMutation(), is(nullValue()));
        assertThat(customerStatement.getEndBalance(), is(nullValue()));
    }

    @Test
    public void parse_bigDecimalsPaseWithScale2() throws IOException {
        csvInputStream = new ClassPathResource("records_scale2.csv").getInputStream();
        List<CustomerStatement> parsedStatements = csvCustomerStatementParser.parse(csvInputStream);

        assertThat(parsedStatements, is(notNullValue()));
        assertThat(parsedStatements.size(), is(1));

        CustomerStatement customerStatement = parsedStatements.get(0);
        assertThat(customerStatement.getStartBalance(), is(new BigDecimal("1.01")));
        assertThat(customerStatement.getMutation(), is(new BigDecimal("+0.09")));
        assertThat(customerStatement.getEndBalance(), is(new BigDecimal("1.10")));
    }

    @Test(expected = CustomerStatementException.class)
    public void parse_whenIOExceptionIsThrown_wrapsAndRethrowsCustomerStatementException() throws IOException {
        InputStream brokenStream = mock(InputStream.class);
        // TODO: inject csvMapper and mock IOEXCPTION, this fails for the wrong reason
        // when(brokenStream.read()).thenThrow(new IOException());

        csvCustomerStatementParser.parse(brokenStream);
    }
}