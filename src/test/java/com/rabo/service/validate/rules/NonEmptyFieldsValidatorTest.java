package com.rabo.service.validate.rules;

import com.rabo.model.CustomerStatement;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class NonEmptyFieldsValidatorTest {
    private NonEmptyFieldsValidator nonEmptyFieldsValidator;
    @Before
    public void setUp() {
        nonEmptyFieldsValidator = new NonEmptyFieldsValidator();
    }

    @Test
    public void noEmptFields_whenAllFieldsAreFilled_isValid() {
        CustomerStatement customerStatement = filledStatement();

        boolean hasNoEmptyFields = nonEmptyFieldsValidator.noEmptFields(customerStatement);

        assertThat(hasNoEmptyFields, is(true));
    }

    @Test
    public void noEmptFields_withEmptyAccountNumber_isInValid() {
        CustomerStatement customerStatement = filledStatement();
        customerStatement.setAccountNumber(null);

        boolean hasNoEmptyFields = nonEmptyFieldsValidator.noEmptFields(customerStatement);

        assertThat(hasNoEmptyFields, is(false));
    }

    @Test
    public void noEmptFields_withEmptyDescription_isInValid() {
        CustomerStatement customerStatement = filledStatement();
        customerStatement.setDescription(null);

        boolean hasNoEmptyFields = nonEmptyFieldsValidator.noEmptFields(customerStatement);

        assertThat(hasNoEmptyFields, is(false));
    }

    @Test
    public void noEmptFields_withEmptyReference_isInValid() {
        CustomerStatement customerStatement = filledStatement();
        customerStatement.setReference(null);

        boolean hasNoEmptyFields = nonEmptyFieldsValidator.noEmptFields(customerStatement);

        assertThat(hasNoEmptyFields, is(false));
    }

    @Test
    public void noEmptFields_withEmptyEndBalance_isInValid() {
        CustomerStatement customerStatement = filledStatement();
        customerStatement.setEndBalance(null);

        boolean hasNoEmptyFields = nonEmptyFieldsValidator.noEmptFields(customerStatement);

        assertThat(hasNoEmptyFields, is(false));
    }

    @Test
    public void noEmptFields_withEmptyMutation_isInValid() {
        CustomerStatement customerStatement = filledStatement();
        customerStatement.setMutation(null);

        boolean hasNoEmptyFields = nonEmptyFieldsValidator.noEmptFields(customerStatement);

        assertThat(hasNoEmptyFields, is(false));
    }

    @Test
    public void noEmptFields_withEmptyStartBalance_isInValid() {
        CustomerStatement customerStatement = filledStatement();
        customerStatement.setStartBalance(null);

        boolean hasNoEmptyFields = nonEmptyFieldsValidator.noEmptFields(customerStatement);

        assertThat(hasNoEmptyFields, is(false));
    }

    private CustomerStatement filledStatement() {
        return CustomerStatement.builder()
                .endBalance(BigDecimal.ONE)
                .mutation(BigDecimal.ONE)
                .startBalance(BigDecimal.ONE)
                .description("des")
                .accountNumber("nr")
                .reference(1L)
                .build();
    }
}