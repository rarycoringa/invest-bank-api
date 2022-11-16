package br.ufrn.imd.investbankapi.models;

import java.util.ArrayList;

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

    public void deposit(double value) {
        double current_balance = this.getBalance();
        double new_balance = current_balance + value;
        this.setBalance(new_balance);
    }

    public void withdraw(double value) throws WithdrawException{
        double current_balance = this.getBalance();

        if (current_balance < value) {
            String message = "Withdraw refused. Insufficient balance."; 
            throw new WithdrawException(message);
        } else {

        }
    }
}
