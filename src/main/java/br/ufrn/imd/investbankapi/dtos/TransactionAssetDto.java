package br.ufrn.imd.investbankapi.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public abstract class TransactionAssetDto {

    @NotNull
    @Positive
    private int walletNumber;

    @NotBlank
    @Size(max = 6)
    private String assetCode;

    @NotNull
    @Positive
    private int quantity;

    public int getWalletNumber() {
        return walletNumber;
    }

    public void setWalletNumber(int walletNumber) {
        this.walletNumber = walletNumber;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
