package com.rabo.service.parse;

import com.rabo.model.CustomerStatement;
import com.rabo.CustomerStatementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@Slf4j
public class CustomerStatementParser {
    @Autowired
    private CsvCustomerStatementParser csvCustomerStatementParser;
    @Autowired
    private XmlCustomerStatementParser xmlCustomerStatementParser;

    public List<CustomerStatement> parse(InputStream inputStream, String fileName) throws CustomerStatementException {
        List<CustomerStatement> customerStatements;

        String fileType = getFileType(fileName);
        if ("csv".equals(fileType)) {
            customerStatements = csvCustomerStatementParser.parse(inputStream);
        } else if ("xml".equals(fileType)) {
            customerStatements = xmlCustomerStatementParser.parse(inputStream);
        } else {
            throw new CustomerStatementException("Unparseable file type. Provided type: " + fileType);
        }

        return customerStatements;
    }

    private String getFileType(String fileName) {
        int lastDot = fileName.lastIndexOf(".");

        if(lastDot == -1) {
            throw new CustomerStatementException("no file extension on customer statement file");
        }
        int afterLastDot =  lastDot + 1;
        return fileName.substring(afterLastDot).toLowerCase();
    }
}
