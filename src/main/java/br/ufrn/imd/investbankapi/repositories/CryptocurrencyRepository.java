package br.ufrn.imd.investbankapi.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufrn.imd.investbankapi.models.Cryptocurrency;

@Repository
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, UUID> {
    boolean existsByCode(String code);
    Optional<Cryptocurrency> findByCode(String code);
}
