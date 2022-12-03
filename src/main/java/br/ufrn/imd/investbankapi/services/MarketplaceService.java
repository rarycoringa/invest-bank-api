package br.ufrn.imd.investbankapi.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import br.ufrn.imd.investbankapi.exceptions.PurchaseException;
import br.ufrn.imd.investbankapi.exceptions.WithdrawException;
import br.ufrn.imd.investbankapi.models.Asset;
import br.ufrn.imd.investbankapi.models.PurchasedAsset;
import br.ufrn.imd.investbankapi.models.Wallet;
import br.ufrn.imd.investbankapi.repositories.PurchasedAssetRepository;

@Service
public class MarketplaceService {

    final PurchasedAssetRepository purchasedAssetRepository;

    public MarketplaceService(PurchasedAssetRepository purchasedAssetRepository) {
        this.purchasedAssetRepository = purchasedAssetRepository;
    }

    public List<PurchasedAsset> findAssetsByWallet(Wallet wallet) {
        return purchasedAssetRepository.findByWallet(wallet);
    }

    @Transactional
    public PurchasedAsset purchase (Wallet wallet, Asset asset, int quantity) throws PurchaseException {
        Optional<PurchasedAsset> purchasedAssetOptional = purchasedAssetRepository.findByWalletAndAsset(wallet, asset);
        
        PurchasedAsset purchasedAsset;
        
        if (!purchasedAssetOptional.isPresent()) {
            purchasedAsset = new PurchasedAsset(wallet, asset);
        } else {
            purchasedAsset = purchasedAssetOptional.get();
        }
        
        try {
            wallet.withdraw(asset.getPrice().multiply(BigDecimal.valueOf(quantity)));
            purchasedAsset.purchase(quantity);
            purchasedAsset = purchasedAssetRepository.save(purchasedAsset);
        } catch (WithdrawException exception) {
            String message = String.format("Purchase denied. Insufficient balance at the wallet %x.", wallet.getNumber());
            throw new PurchaseException(message);
        }

        return purchasedAsset;

    }
}
