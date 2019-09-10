package com.rabo.rest;

import com.rabo.model.CustomerStatement;
import com.rabo.service.parse.CustomerStatementParser;
import com.rabo.CustomerStatementException;
import com.rabo.service.validate.CustomerStatementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/customerstatement",
                consumes = ALL_VALUE,
                produces = APPLICATION_JSON_VALUE)
@Slf4j
public class CustomerStatementController {

    @Autowired
    private CustomerStatementService customerStatementService;
    @Autowired
    private CustomerStatementParser customerStatementParser;

    @RequestMapping(method = RequestMethod.POST, path = "validate")
    @CrossOrigin("http://localhost:4200")
    public ResponseEntity<ValidationReport> validate(@RequestParam("customerStatement") MultipartFile customerStatementFile) {
        log.info("validate customer report message received");

        try (InputStream inputStream = customerStatementFile.getInputStream()) {
            List<CustomerStatement> statements = customerStatementParser.parse(inputStream, customerStatementFile.getOriginalFilename());
            ValidationReport validationReport = customerStatementService.validate(statements, customerStatementFile.getOriginalFilename());

            return ResponseEntity.ok(validationReport);
        } catch (IOException ex) {
            throw new CustomerStatementException("Unable to create InputStream on customerStatement file.", ex);
        }
    }
}
