package br.ufrn.imd.investbankapi.dtos;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class CryptocurrencyDTO {
    
    @NotBlank
    @Size(max = 6)
    private String code;

    @NotBlank
    @Size(max = 30)
    private String name;

    @Positive
    @Digits(integer = 6, fraction = 2)
    private BigDecimal price;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
