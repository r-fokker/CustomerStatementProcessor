package com.rabo.rest;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ValidationReport implements Serializable {
    private String fileName;
    private List<FaultyCustomerStatement> faultyStatements = new ArrayList<>();

    public void addValidatedStatement(FaultyCustomerStatement faultyCustomerStatement) {
        this.faultyStatements.add(faultyCustomerStatement);
    }

    public boolean hasNoFaults() {
        return faultyStatements.isEmpty();
    }
}
