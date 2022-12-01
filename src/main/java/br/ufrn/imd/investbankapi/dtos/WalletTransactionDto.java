package br.ufrn.imd.investbankapi.dtos;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;

public class WalletTransactionDto {
    
    @Positive
    @Digits(integer = 6, fraction = 2)
    private BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
