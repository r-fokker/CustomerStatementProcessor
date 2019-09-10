package com.rabo.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.rabo.rest.FaultyCustomerStatement;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@NoArgsConstructor
@JsonPropertyOrder({"reference", "accountNumber", "description", "startBalance", "mutation", "endBalance"})
@Builder
@Entity
public class CustomerStatement implements Serializable {
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.DOWN;

    @Id
    @JacksonXmlProperty(isAttribute = true)
    @Setter
    private Long reference;
    @JacksonXmlProperty
    @Setter
    private String accountNumber;
    @JacksonXmlProperty
    @Setter
    private String description;
    @JacksonXmlProperty
    @Column(scale = 2)
    private BigDecimal startBalance;
    @JacksonXmlProperty
    @Column(scale = 2)
    private BigDecimal mutation;
    @JacksonXmlProperty
    @Column(scale = 2)
    private BigDecimal endBalance;

    public CustomerStatement(Long reference, String accountNumber, String description, BigDecimal startBalance, BigDecimal mutation, BigDecimal endBalance) {
        this.reference = reference;
        this.accountNumber = accountNumber;
        this.description = description;
        this.setStartBalance(startBalance);
        this.setMutation(mutation);
        this.setEndBalance(endBalance);
    }

    public static CustomerStatementBuilder builder() {
        return new CustomerStatementBuilder();
    }

    public void setStartBalance(BigDecimal startBalance) {
        this.startBalance = standardizeRoundingModeAndScale(startBalance);
    }

    public void setMutation(BigDecimal mutation) {
        this.mutation = standardizeRoundingModeAndScale(mutation);
    }

    public void setEndBalance(BigDecimal endBalance) {
        this.endBalance = standardizeRoundingModeAndScale(endBalance);
    }

    public FaultyCustomerStatement asFaultyCustomerStatement() {
        return new FaultyCustomerStatement(this.getDescription(), this.getReference());
    }

    private BigDecimal standardizeRoundingModeAndScale(BigDecimal bigDecimal) {
        if(bigDecimal != null) {
            return bigDecimal.setScale(SCALE, ROUNDING_MODE);
        } else {
            return null;
        }
    }
}
