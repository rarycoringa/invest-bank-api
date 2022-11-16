package br.ufrn.imd.investbankapi.models;

import java.util.ArrayList;

import br.ufrn.imd.investbankapi.exceptions.PurchaseException;
import br.ufrn.imd.investbankapi.exceptions.SaleException;
import br.ufrn.imd.investbankapi.exceptions.WithdrawException;
import br.ufrn.imd.investbankapi.models.Asset;

public class Wallet {
    private int number;
    private String owner;
    private double balance;
    private ArrayList<Asset> assets;

    public Wallet(String owner) {
        this.setOwner(owner);
        this.setBalance(0.0);
        this.assets = new ArrayList<Asset>();
    }

    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public ArrayList<Asset> listAssets() {
        return assets;
    }
    public Asset getAsset(int i) {
        Asset asset = this.assets.get(i);
        return asset;
    }
    public void addAsset(Asset asset) {
        this.assets.add(asset);
    }
    public void removeAsset(Asset asset) {
        this.assets.remove(asset);
    }

    public void deposit(double value) {
        double current_balance = this.getBalance();
        double new_balance = current_balance + value;
        this.setBalance(new_balance);
    }

    public void withdraw(double value) throws WithdrawException{
        double current_balance = this.getBalance();

        if (current_balance < value) {
            String message = "Withdraw denied. Insufficient balance at this wallet."; 
            throw new WithdrawException(message);
        } else {
           double new_balance = current_balance - value;
           this.setBalance(new_balance);
        }
    }

    public void purchaseAsset(Asset asset) throws PurchaseException {
        double current_balance = this.getBalance();

        if (current_balance < asset.getPrice()) {
            String message = "Purchase denied. Insufficient balance at this wallet.";
            throw new PurchaseException(message);
        } else {
            double new_balance = current_balance - asset.getPrice();
            this.setBalance(new_balance);
            this.addAsset(asset);
        }
    }

    public void sellAsset(Asset asset) throws SaleException {
        boolean existsAsset = this.assets.contains(asset);

        if (!existsAsset) {
            String message = "Sale denied. Asset not found at this wallet.";
            throw new SaleException(message);
        } else {
            double current_balance = this.getBalance();
            double new_balance = current_balance + asset.getPrice();
            this.removeAsset(asset);
            this.setBalance(new_balance);
        }

    }
}
