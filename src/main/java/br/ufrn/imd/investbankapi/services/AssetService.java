package br.ufrn.imd.investbankapi.services;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.ufrn.imd.investbankapi.exceptions.PurchaseException;
import br.ufrn.imd.investbankapi.exceptions.SaleException;
import br.ufrn.imd.investbankapi.exceptions.WithdrawException;
import br.ufrn.imd.investbankapi.models.Asset;
import br.ufrn.imd.investbankapi.models.PurchasedAsset;
import br.ufrn.imd.investbankapi.models.Wallet;
import br.ufrn.imd.investbankapi.repositories.AssetRepository;
import br.ufrn.imd.investbankapi.repositories.PurchasedAssetRepository;

@Service
public class AssetService {
    
    final AssetRepository assetRepository;
    final PurchasedAssetRepository purchasedAssetRepository;

    public AssetService(AssetRepository assetRepository, PurchasedAssetRepository purchasedAssetRepository) {
        this.assetRepository = assetRepository;
        this.purchasedAssetRepository = purchasedAssetRepository;
    }

    public List<Asset> findAll() {
        return assetRepository.findAll();
    }

    public Optional<Asset> findByCode(String code) {
        return assetRepository.findByCode(code);
    }

    @Transactional
    public Asset save(Asset asset) {
        return assetRepository.save(asset);
    }

    @Transactional
    public void delete(Asset asset) {
        assetRepository.delete(asset);
    }

    public boolean existsByCode(String code) {
        return assetRepository.existsByCode(code);
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

    @Transactional
    public PurchasedAsset sale (Wallet wallet, Asset asset, int quantity) throws SaleException {
        Optional<PurchasedAsset> purchasedAssetOptional = purchasedAssetRepository.findByWalletAndAsset(wallet, asset);

        PurchasedAsset purchasedAsset;

        if (!purchasedAssetOptional.isPresent()) {
            String message = String.format("Sale denied. Asset %s not found at the wallet %x.", asset.getCode(), wallet.getNumber());
            throw new SaleException(message);
        } else {
            purchasedAsset = purchasedAssetOptional.get();
        }

        try {
            wallet.deposit(asset.getPrice().multiply(BigDecimal.valueOf(quantity)));

            if (purchasedAsset.getQuantity() == quantity) {
                purchasedAsset.saleAll();
                purchasedAssetRepository.delete(purchasedAsset);
            } else {
                purchasedAsset.sale(quantity);
                purchasedAssetRepository.save(purchasedAsset);
            }
        } catch (SaleException exception) {
            throw exception;
        }

        return purchasedAsset;
    }
}
