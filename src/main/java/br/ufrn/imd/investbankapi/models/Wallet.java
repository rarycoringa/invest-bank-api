package br.ufrn.imd.investbankapi.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.ufrn.imd.investbankapi.exceptions.WithdrawException;

@Entity
@Table(name = "wallets")
public class Wallet implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private int number;

    @Column(nullable = false, length = 30)
    private String owner;

    @Column(nullable = false, scale = 2)
    private BigDecimal balance = new BigDecimal("0.00");

    public Wallet() {}

    public Wallet(int number, String owner) {
        this.number = number;
        this.owner = owner;
    }
    
    public UUID getId() {
        return id;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void deposit(BigDecimal value) {
        this.balance = this.balance.add(value);
    }

    public void withdraw(BigDecimal value) throws WithdrawException {
        if (this.balance.compareTo(value) == -1) {
            String message = String.format("Withdraw denied. Insufficient balance at the wallet %x.", this.number);
            throw new WithdrawException(message);
        }

        this.balance = this.balance.subtract(value);
    }

}
