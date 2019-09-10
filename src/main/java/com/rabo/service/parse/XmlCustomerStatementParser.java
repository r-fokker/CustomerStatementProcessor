package com.rabo.service.parse;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.rabo.model.CustomerStatement;
import com.rabo.model.CustomerStatements;
import com.rabo.CustomerStatementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@Slf4j
public class XmlCustomerStatementParser {
    private XMLInputFactory xmlInputFactory;
    private XmlMapper xmlMapper;

    public XmlCustomerStatementParser() {
        xmlInputFactory =  XMLInputFactory.newInstance();
        xmlMapper = new XmlMapper();
    }

    public List<CustomerStatement> parse(InputStream inputStream) {
        CustomerStatements customerStatements;
        XMLStreamReader xmlReader = null;
        try {
            xmlReader = xmlInputFactory.createXMLStreamReader(inputStream);
            customerStatements = xmlMapper.readValue(xmlReader, CustomerStatements.class);
        } catch (XMLStreamException | IOException xse) {
            throw new CustomerStatementException("Unable to parse XML stream of customer statement", xse);
        }

        closeReader(xmlReader);

        return customerStatements.getCustomerStatements();
    }

    private void closeReader(XMLStreamReader xmlReader) {
        try {
            xmlReader.close();
        } catch (XMLStreamException e) {
            throw new CustomerStatementException(("unable to close XMLstreamreader after parsing customer statement"));
        }
    }
}
