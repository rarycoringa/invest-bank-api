package br.ufrn.imd.investbankapi.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.imd.investbankapi.dtos.PurchaseAssetDto;
import br.ufrn.imd.investbankapi.dtos.SaleAssetDto;
import br.ufrn.imd.investbankapi.exceptions.PurchaseException;
import br.ufrn.imd.investbankapi.exceptions.SaleException;
import br.ufrn.imd.investbankapi.models.Asset;
import br.ufrn.imd.investbankapi.models.PurchasedAsset;
import br.ufrn.imd.investbankapi.models.Wallet;
import br.ufrn.imd.investbankapi.services.WalletService;
import br.ufrn.imd.investbankapi.services.AssetService;
import br.ufrn.imd.investbankapi.services.MarketplaceService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/marketplace")
public class MarketplaceController {

    final MarketplaceService marketplaceService;
    final WalletService walletService;
    final AssetService assetService;
    
    public MarketplaceController(MarketplaceService marketplaceService, WalletService walletService, AssetService assetService) {
        this.marketplaceService = marketplaceService;
        this.walletService = walletService;
        this.assetService = assetService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<Object> purchaseAsset(@RequestBody @Valid PurchaseAssetDto purchaseAssetDto) {
        Optional<Wallet> walletOptional = walletService.findByNumber(purchaseAssetDto.getWalletNumber());
        Optional<Asset> assetOptional = assetService.findByCode(purchaseAssetDto.getAssetCode());

        if (!walletOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Wallet with number %s not found.", purchaseAssetDto.getWalletNumber()));
        }

        if (!assetOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Asset with code %s not found.", purchaseAssetDto.getAssetCode()));
        }

        Wallet wallet = walletOptional.get();
        Asset asset = assetOptional.get();
        int quantity = purchaseAssetDto.getQuantity();
        PurchasedAsset purchasedAsset;

        try {
            purchasedAsset = marketplaceService.purchase(wallet, asset, quantity);
        } catch (PurchaseException exception) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(purchasedAsset);
    }

    @PostMapping("/sale")
    public ResponseEntity<Object> saleAsset(@RequestBody @Valid SaleAssetDto saleAssetDto) {
        Optional<Wallet> walletOptional = walletService.findByNumber(saleAssetDto.getWalletNumber());
        Optional<Asset> assetOptional = assetService.findByCode(saleAssetDto.getAssetCode());

        if (!walletOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Wallet with number %s not found.", saleAssetDto.getWalletNumber()));
        }

        if (!assetOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Asset with code %s not found.", saleAssetDto.getAssetCode()));
        }

        Wallet wallet = walletOptional.get();
        Asset asset = assetOptional.get();
        int quantity = saleAssetDto.getQuantity();
        PurchasedAsset purchasedAsset;

        try {
            purchasedAsset = marketplaceService.sale(wallet, asset, quantity);
        } catch (SaleException exception) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(purchasedAsset);
    }
}
