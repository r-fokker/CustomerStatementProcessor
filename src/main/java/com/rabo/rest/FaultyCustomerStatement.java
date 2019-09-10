package com.rabo.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class FaultyCustomerStatement implements Serializable {
    private String description;
    private Long transactionReference;
}
