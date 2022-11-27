package br.ufrn.imd.investbankapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import br.ufrn.imd.investbankapi.models.Asset;

@Repository
public interface AssetRepository extends JpaRepository<Asset, UUID> {
    
    boolean existsByCode(String code);
    Optional<Asset> findByCode(String code);
}
