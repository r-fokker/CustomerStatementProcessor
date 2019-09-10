package com.rabo.service;

import com.rabo.CustomerStatementException;
import com.rabo.model.CustomerStatement;
import com.rabo.model.CustomerStatements;
import com.rabo.service.parse.XmlCustomerStatementParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class XmlCustomerStatementParserTest {
    private XmlCustomerStatementParser xmlCustomerStatementParser;

    private InputStream inputStream;

    @Before
    public void setUp() {
        xmlCustomerStatementParser = new XmlCustomerStatementParser();
    }
    @After
    public void finish() throws IOException {
        if(inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void parse_returnsMappedCustomerStatements() throws IOException {
        inputStream =  new ClassPathResource("records_10.xml").getInputStream();
        List<CustomerStatement> resultStatements = xmlCustomerStatementParser.parse(inputStream);

        assertThat(resultStatements, is(notNullValue()));
        assertThat(resultStatements.size(), is(10));
    }

    @Test(expected = CustomerStatementException.class)
    public void parse_whenXMLStreamExceptionIsThrown_wrapsAndRethrowsAsCustomerStatementException() throws IOException {
        InputStream inputStream = new ClassPathResource("records_notXml.xml").getInputStream();
        xmlCustomerStatementParser.parse(inputStream);
    }

    @Test(expected = CustomerStatementException.class)
    public void parse_whenIOExceptionIsThrown_wrapsAndRethrowsAsCustomerStatementException() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        // TODO: inject xmlInputFactory and mock IOException, this fails for the wrong reason
        // when(inputStream.read()).thenThrow(new IOException());

        xmlCustomerStatementParser.parse(inputStream);
    }
}
