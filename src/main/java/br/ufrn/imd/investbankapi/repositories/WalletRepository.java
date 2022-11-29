package br.ufrn.imd.investbankapi.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufrn.imd.investbankapi.models.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    boolean existsByNumber(int number);
    Optional<Wallet> findByNumber(int number);

}
