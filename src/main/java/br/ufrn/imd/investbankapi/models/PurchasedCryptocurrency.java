package br.ufrn.imd.investbankapi.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufrn.imd.investbankapi.exceptions.SaleException;

@Entity
@Table(name = "purchased_cryptocurrency")
public class PurchasedCryptocurrency {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private Cryptocurrency cryptocurrency;

    @Column(nullable = false)
    private int quantity = 0;

    public PurchasedCryptocurrency() {}

    public PurchasedCryptocurrency(Wallet wallet, Cryptocurrency cryptocurrency) {
        this.wallet = wallet;
        this.cryptocurrency = cryptocurrency;
    }

    public UUID getId() {
        return id;
    }

    @JsonIgnore
    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Cryptocurrency getCryptocurrency() {
        return cryptocurrency;
    }

    public void setCryptocurrency(Cryptocurrency cryptocurrency) {
        this.cryptocurrency = cryptocurrency;
    }

    public int getQuantity() {
        return quantity;
    }

    public void purchase(int quantity) {
        this.quantity += quantity;
    }

    public void sale(int quantity) throws SaleException {
        if (this.quantity < quantity) {
            String message = String.format("Sale denied. Insufficient quantity of cryptocurrency %s at the wallet %x.", this.cryptocurrency.getCode(), this.wallet.getNumber());
            throw new SaleException(message);
        }

        this.quantity -= quantity;
    }

    public void saleAll() {
        this.quantity = 0;
    }


}
