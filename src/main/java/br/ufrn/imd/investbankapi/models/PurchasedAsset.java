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
@Table(name = "purchased_assets")
public class PurchasedAsset {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @Column(nullable = false)
    private int quantity = 0;

    public PurchasedAsset() {}

    public PurchasedAsset(Wallet wallet, Asset asset) {
        this.wallet = wallet;
        this.asset = asset;
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

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public int getQuantity() {
        return quantity;
    }

    public void purchase(int quantity) {
        this.quantity += quantity;
    }

    public void sale(int quantity) throws SaleException {
        if (this.quantity < quantity) {
            String message = String.format("Sale denied. Insufficient quantity of asset %s at the wallet %x.", this.asset.getCode(), this.wallet.getNumber());
            throw new SaleException(message);
        }

        this.quantity -= quantity;
    }

    public void saleAll() {
        this.quantity = 0;
    }


}
