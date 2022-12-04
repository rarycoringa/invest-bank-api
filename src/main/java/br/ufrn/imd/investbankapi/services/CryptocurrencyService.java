package br.ufrn.imd.investbankapi.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import br.ufrn.imd.investbankapi.exceptions.PurchaseException;
import br.ufrn.imd.investbankapi.exceptions.SaleException;
import br.ufrn.imd.investbankapi.exceptions.WithdrawException;
import br.ufrn.imd.investbankapi.models.Cryptocurrency;
import br.ufrn.imd.investbankapi.models.PurchasedCryptocurrency;
import br.ufrn.imd.investbankapi.models.Wallet;
import br.ufrn.imd.investbankapi.repositories.CryptocurrencyRepository;
import br.ufrn.imd.investbankapi.repositories.PurchasedCryptocurrencyRepository;

@Service
public class CryptocurrencyService {
    
    final CryptocurrencyRepository cryptocurrencyRepository;
    final PurchasedCryptocurrencyRepository purchasedCryptocurrencyRepository;

    public CryptocurrencyService(CryptocurrencyRepository cryptocurrencyRepository, PurchasedCryptocurrencyRepository purchasedCryptocurrencyRepository) {
        this.cryptocurrencyRepository = cryptocurrencyRepository;
        this.purchasedCryptocurrencyRepository = purchasedCryptocurrencyRepository;
    }

    public List<Cryptocurrency> findAll() {
        return cryptocurrencyRepository.findAll();
    }

    public Optional<Cryptocurrency> findByCode(String code) {
        return cryptocurrencyRepository.findByCode(code);
    }

    @Transactional
    public Cryptocurrency save(Cryptocurrency cryptocurrency) {
        return cryptocurrencyRepository.save(cryptocurrency);
    }

    @Transactional
    public void delete(Cryptocurrency cryptocurrency) {
        cryptocurrencyRepository.delete(cryptocurrency);
    }

    public boolean existsByCode(String code) {
        return cryptocurrencyRepository.existsByCode(code);
    }

    public List<PurchasedCryptocurrency> findCryptocurrenciesByWallet(Wallet wallet) {
        return purchasedCryptocurrencyRepository.findByWallet(wallet);
    }

    @Transactional
    public PurchasedCryptocurrency purchase (Wallet wallet, Cryptocurrency cryptocurrency, int quantity) throws PurchaseException {
        Optional<PurchasedCryptocurrency> purchasedCryptocurrencyOptional = purchasedCryptocurrencyRepository.findByWalletAndCryptocurrency(wallet, cryptocurrency);
        
        PurchasedCryptocurrency purchasedCryptocurrency;
        
        if (!purchasedCryptocurrencyOptional.isPresent()) {
            purchasedCryptocurrency = new PurchasedCryptocurrency(wallet, cryptocurrency);
        } else {
            purchasedCryptocurrency = purchasedCryptocurrencyOptional.get();
        }
        
        try {
            wallet.withdraw(cryptocurrency.getPrice().multiply(BigDecimal.valueOf(quantity)));
            purchasedCryptocurrency.purchase(quantity);
            purchasedCryptocurrency = purchasedCryptocurrencyRepository.save(purchasedCryptocurrency);
        } catch (WithdrawException exception) {
            String message = String.format("Purchase denied. Insufficient balance at the wallet %x.", wallet.getNumber());
            throw new PurchaseException(message);
        }

        return purchasedCryptocurrency;

    }

    @Transactional
    public PurchasedCryptocurrency sale (Wallet wallet, Cryptocurrency cryptocurrency, int quantity) throws SaleException {
        Optional<PurchasedCryptocurrency> purchasedCryptocurrencyOptional = purchasedCryptocurrencyRepository.findByWalletAndCryptocurrency(wallet, cryptocurrency);

        PurchasedCryptocurrency purchasedCryptocurrency;

        if (!purchasedCryptocurrencyOptional.isPresent()) {
            String message = String.format("Sale denied. Cryptocurrency %s not found at the wallet %x.", cryptocurrency.getCode(), wallet.getNumber());
            throw new SaleException(message);
        } else {
            purchasedCryptocurrency = purchasedCryptocurrencyOptional.get();
        }

        try {
            wallet.deposit(cryptocurrency.getPrice().multiply(BigDecimal.valueOf(quantity)));

            if (purchasedCryptocurrency.getQuantity() == quantity) {
                purchasedCryptocurrency.saleAll();
                purchasedCryptocurrencyRepository.delete(purchasedCryptocurrency);
            } else {
                purchasedCryptocurrency.sale(quantity);
                purchasedCryptocurrencyRepository.save(purchasedCryptocurrency);
            }
        } catch (SaleException exception) {
            throw exception;
        }

        return purchasedCryptocurrency;
    }
}
