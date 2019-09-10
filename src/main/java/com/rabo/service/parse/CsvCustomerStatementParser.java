package com.rabo.service.parse;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.rabo.CustomerStatementException;
import com.rabo.model.CustomerStatement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CsvCustomerStatementParser {

    private CsvMapper csvMapper;

    public CsvCustomerStatementParser() {
        csvMapper = new CsvMapper();
        csvMapper.enable(CsvParser.Feature.SKIP_EMPTY_LINES);
        csvMapper.enable(CsvParser.Feature.INSERT_NULLS_FOR_MISSING_COLUMNS);
    }

    public List<CustomerStatement> parse(InputStream inputStream) {
        CsvSchema customerStatementSchema = csvMapper.schemaFor(CustomerStatement.class).withHeader();

        List<CustomerStatement> resultList = new ArrayList<>();

        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1);
             MappingIterator<CustomerStatement> iterator = csvMapper.readerFor(CustomerStatement.class)
                                                            .with(customerStatementSchema)
                                                            .readValues(reader)) {

            while (iterator.hasNext()) {
                resultList.add(iterator.next());
            }
        } catch (IOException ex) {
            throw new CustomerStatementException("Unable to create reader on Csv file", ex);
        }

        return resultList;
    }
}
