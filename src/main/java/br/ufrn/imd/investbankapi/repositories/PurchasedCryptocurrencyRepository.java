package br.ufrn.imd.investbankapi.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufrn.imd.investbankapi.models.Cryptocurrency;
import br.ufrn.imd.investbankapi.models.PurchasedCryptocurrency;
import br.ufrn.imd.investbankapi.models.Wallet;

@Repository
public interface PurchasedCryptocurrencyRepository extends JpaRepository<PurchasedCryptocurrency, UUID> {
    
    List<PurchasedCryptocurrency> findByWallet(Wallet wallet);
    Optional<PurchasedCryptocurrency> findByWalletAndCryptocurrency(Wallet wallet, Cryptocurrency cryptocurrency);

}
