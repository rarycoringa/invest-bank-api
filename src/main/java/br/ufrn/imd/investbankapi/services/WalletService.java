package br.ufrn.imd.investbankapi.services;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import br.ufrn.imd.investbankapi.models.Wallet;
import br.ufrn.imd.investbankapi.repositories.WalletRepository;

@Service
public class WalletService {
    
    final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public List<Wallet> findAll() {
        return walletRepository.findAll();
    }

    public Optional<Wallet> findByNumber(int number) {
        return walletRepository.findByNumber(number);
    }

    @Transactional
    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Transactional
    public void delete(Wallet wallet) {
        walletRepository.delete(wallet);
    }

    public boolean existsByNumber(int number) {
        return walletRepository.existsByNumber(number);
    }

}
