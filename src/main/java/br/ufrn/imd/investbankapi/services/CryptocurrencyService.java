package br.ufrn.imd.investbankapi.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import br.ufrn.imd.investbankapi.models.Cryptocurrency;
import br.ufrn.imd.investbankapi.repositories.CryptocurrencyRepository;

@Service
public class CryptocurrencyService {
    
    final CryptocurrencyRepository cryptocurrencyRepository;

    public CryptocurrencyService(CryptocurrencyRepository cryptocurrencyRepository) {
        this.cryptocurrencyRepository = cryptocurrencyRepository;
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
}
