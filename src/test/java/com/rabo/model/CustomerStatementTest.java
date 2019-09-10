package com.rabo.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CustomerStatementTest {

    @Test
    public void allArgsConstructor_standardizesBigDecimalsToScale2() {
        CustomerStatement customerStatement = new CustomerStatement(1L, "acc", "des", new BigDecimal("0.00001"), new BigDecimal("0.009"),new BigDecimal("0.00"));

        assertThat(customerStatement.getStartBalance().scale(), is(2));
        assertThat(customerStatement.getStartBalance(), is(new BigDecimal("0.00")));
    }

    @Test
    public void setStartBalance_standardizesBigDecimalsToScale2() {
        CustomerStatement customerStatement = new CustomerStatement();
        customerStatement.setStartBalance(new BigDecimal("1.0005"));

        assertThat(customerStatement.getStartBalance().scale(), is(2));
        assertThat(customerStatement.getStartBalance(), is(new BigDecimal("1.00")));
        assertThat(customerStatement.getStartBalance().scale(), is(2));
    }

    @Test
    public void setMutation_standardizesBigDecimalsToScale2() {
        CustomerStatement customerStatement = new CustomerStatement();
        customerStatement.setMutation(new BigDecimal("1.0005"));

        assertThat(customerStatement.getMutation().scale(), is(2));
        assertThat(customerStatement.getMutation(), is(new BigDecimal("1.00")));
        assertThat(customerStatement.getMutation().scale(), is(2));
    }

    @Test
    public void setEndBalance_standardizesBigDecimalsToScale2() {
        CustomerStatement customerStatement = new CustomerStatement();
        customerStatement.setEndBalance(new BigDecimal("1.0005"));

        assertThat(customerStatement.getEndBalance().scale(), is(2));
        assertThat(customerStatement.getEndBalance(), is(new BigDecimal("1.00")));
        assertThat(customerStatement.getEndBalance().scale(), is(2));
    }

    @Test
    public void builder_standardizesBigDecimalsToScale2() {
        CustomerStatement customerStatement = CustomerStatement.builder()
                .startBalance(new BigDecimal("1.0005"))
                .mutation(new BigDecimal("0.0004"))
                .endBalance(new BigDecimal("-0.009"))
                .build();

        assertThat(customerStatement.getStartBalance(), is(new BigDecimal("1.00")));
        assertThat(customerStatement.getMutation(), is(new BigDecimal("0.00")));
        assertThat(customerStatement.getEndBalance(), is(new BigDecimal("0.00")));
    }

}