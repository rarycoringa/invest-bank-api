package br.ufrn.imd.investbankapi.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufrn.imd.investbankapi.models.Asset;
import br.ufrn.imd.investbankapi.models.PurchasedAsset;
import br.ufrn.imd.investbankapi.models.Wallet;

@Repository
public interface PurchasedAssetRepository extends JpaRepository<PurchasedAsset, UUID> {
    
    List<PurchasedAsset> findByWallet(Wallet wallet);
    Optional<PurchasedAsset> findByWalletAndAsset(Wallet wallet, Asset asset);

}
