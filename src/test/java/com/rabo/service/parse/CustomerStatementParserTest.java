package com.rabo.service.parse;

import com.rabo.CustomerStatementException;
import com.rabo.model.CustomerStatement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerStatementParserTest {

    @Mock
    private CsvCustomerStatementParser csvCustomerStatementParser;
    @Mock
    private XmlCustomerStatementParser xmlCustomerStatementParser;

    @Mock
    private InputStream inputStream;

    @InjectMocks
    private CustomerStatementParser customerStatementParser;

    @Test
    public void parse_derivesParserFromFileExtensionXml() {
        List<CustomerStatement> expectedCustomerStatements = new ArrayList<>();
        when(xmlCustomerStatementParser.parse(any(InputStream.class)))
                .thenReturn(expectedCustomerStatements);

        List<CustomerStatement> parsedStatements = customerStatementParser.parse(inputStream, "file.xml");

        verify(xmlCustomerStatementParser).parse(inputStream);
        assertThat(parsedStatements, is(expectedCustomerStatements));
    }

    @Test
    public void parse_derivesParserFromFileExtensionCsv() {
        List<CustomerStatement> expectedCustomerStatements = new ArrayList<>();
        when(csvCustomerStatementParser.parse(any(InputStream.class)))
                .thenReturn(expectedCustomerStatements);

        List<CustomerStatement> parsedStatements = customerStatementParser.parse(inputStream, "file.csv");

        verify(csvCustomerStatementParser).parse(inputStream);
        assertThat(parsedStatements, is(expectedCustomerStatements));
    }

    @Test(expected = CustomerStatementException.class)
    public void parse_withNoExtension_throwsParseException() {
        customerStatementParser.parse(inputStream, "file");
    }

    @Test(expected = CustomerStatementException.class)
    public void parse_withDotExtension_throwsParseException() {
        customerStatementParser.parse(inputStream, "file.");
    }
}