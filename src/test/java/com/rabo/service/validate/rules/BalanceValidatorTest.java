package com.rabo.service.validate.rules;

import com.rabo.model.CustomerStatement;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class BalanceValidatorTest {

    private BalanceValidator balanceValidator;

    @Before
    public void setUp() {
        balanceValidator = new BalanceValidator();
    }

    @Test
    public void endBalanceEqualsSumStartAndMutation_addsMutationToStartBalance() {
        CustomerStatement customerStatement = CustomerStatement.builder()
                .startBalance(new BigDecimal("10.1"))
                .mutation(new BigDecimal("1.22"))
                .endBalance(new BigDecimal("11.32"))
                .build();

        boolean endBalanceIsValid = balanceValidator.endBalanceEqualsSumStartAndMutation(customerStatement);

        assertThat(endBalanceIsValid, is(true));
    }

    @Test
    public void endBalanceEqualsSumStartAndMutation_subtractsNegativeMutations() {
        CustomerStatement customerStatement = CustomerStatement.builder()
                .startBalance(new BigDecimal("10"))
                .mutation(new BigDecimal("-0.99"))
                .endBalance(new BigDecimal("9.01"))
                .build();

        boolean endBalanceIsValid = balanceValidator.endBalanceEqualsSumStartAndMutation(customerStatement);

        assertThat(endBalanceIsValid, is(true));
    }

    @Test
    public void endBalanceEqualsSumStartAndMutation_canHaveNegativeBalance() {
        CustomerStatement customerStatement = CustomerStatement.builder()
                .startBalance(new BigDecimal("1"))
                .mutation(new BigDecimal("-2.99"))
                .endBalance(new BigDecimal("-1.99"))
                .build();

        boolean endBalanceIsValid = balanceValidator.endBalanceEqualsSumStartAndMutation(customerStatement);

        assertThat(endBalanceIsValid, is(true));
    }

    @Test
    public void endBalanceEqualsSumStartAndMutation_isInvalidWhenSumsIncorrectly() {
        CustomerStatement customerStatement = CustomerStatement.builder()
                .startBalance(new BigDecimal("1"))
                .mutation(new BigDecimal("+2"))
                .endBalance(new BigDecimal("0"))
                .build();

        boolean endBalanceIsValid = balanceValidator.endBalanceEqualsSumStartAndMutation(customerStatement);

        assertThat(endBalanceIsValid, is(false));
    }

    @Test
    public void endBalanceEqualsSumStartAndMutation_comparesWithScale2() {
        CustomerStatement customerStatement = CustomerStatement.builder()
                .startBalance(new BigDecimal("1"))
                .mutation(new BigDecimal("0.11"))
                .endBalance(new BigDecimal("1.11"))
                .build();

        boolean endBalanceIsValid = balanceValidator.endBalanceEqualsSumStartAndMutation(customerStatement);

        assertThat(endBalanceIsValid, is(true));
    }

    @Test
    public void endBalanceEqualsSumStartAndMutation_increasesScaleBeforeComparing() {
        CustomerStatement customerStatement = CustomerStatement.builder()
                .startBalance(new BigDecimal("1.01"))
                .mutation(new BigDecimal("0.09"))
                .endBalance(new BigDecimal("1.1"))
                .build();

        boolean endBalanceIsValid = balanceValidator.endBalanceEqualsSumStartAndMutation(customerStatement);

        assertThat(endBalanceIsValid, is(true));
    }
}