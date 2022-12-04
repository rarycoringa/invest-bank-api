package br.ufrn.imd.investbankapi.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.imd.investbankapi.dtos.PurchaseAssetDto;
import br.ufrn.imd.investbankapi.dtos.SaleAssetDto;
import br.ufrn.imd.investbankapi.dtos.WalletDto;
import br.ufrn.imd.investbankapi.dtos.WalletTransactionDto;
import br.ufrn.imd.investbankapi.exceptions.PurchaseException;
import br.ufrn.imd.investbankapi.exceptions.SaleException;
import br.ufrn.imd.investbankapi.exceptions.WithdrawException;
import br.ufrn.imd.investbankapi.models.Asset;
import br.ufrn.imd.investbankapi.models.PurchasedAsset;
import br.ufrn.imd.investbankapi.models.Wallet;
import br.ufrn.imd.investbankapi.services.AssetService;
import br.ufrn.imd.investbankapi.services.WalletService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/wallets")
public class WalletController {
    
    final WalletService walletService;
    final AssetService assetService;

    public WalletController(WalletService walletService, AssetService assetService) {
        this.walletService = walletService;
        this.assetService = assetService;
    }

    @GetMapping
    public ResponseEntity<List<Wallet>> getAllWallet() {
        return ResponseEntity.status(HttpStatus.OK).body(walletService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> createWallet(@RequestBody @Valid WalletDto walletDto) {
        if (walletService.existsByNumber(walletDto.getNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(String.format("Number %s is already in use!", walletDto.getNumber()));
        }
        
        var wallet = new Wallet();
        
        BeanUtils.copyProperties(walletDto, wallet);

        return ResponseEntity.status(HttpStatus.CREATED).body(walletService.save(wallet));
    }

    @GetMapping("/{number}")
    public ResponseEntity<Object> getOneWallet(@PathVariable(value = "number") int number) {
        Optional<Wallet> walletOptional = walletService.findByNumber(number);

        if (!walletOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Wallet with number %s not found.", number));
        }

        return ResponseEntity.status(HttpStatus.OK).body(walletOptional.get());
    }

    @PutMapping("/{number}")
    public ResponseEntity<Object> updateWallet(@PathVariable(value = "number") int number, @RequestBody @Valid WalletDto walletDto) {
        Optional<Wallet> walletOptional = walletService.findByNumber(number);

        if (!walletOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Wallet with number %s not found.", number));
        }

        var wallet = walletOptional.get();

        wallet.setNumber(walletDto.getNumber());
        wallet.setOwner(walletDto.getOwner());

        return ResponseEntity.status(HttpStatus.OK).body(walletService.save(wallet));
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<Object> deleteWallet(@PathVariable(value = "number") int number) {
        Optional<Wallet> walletOptional = walletService.findByNumber(number);

        if (!walletOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Wallet with number %s not found.", number));
        }

        walletService.delete(walletOptional.get());
        
        return ResponseEntity.status(HttpStatus.OK).body(String.format("Wallet with number %s successfully deleted!", number));
    }

    @PutMapping("/{number}/deposit")
    public ResponseEntity<Object> walletDeposit(@PathVariable(value = "number") int number, @RequestBody @Valid WalletTransactionDto walletTransactionDto) {
        Optional<Wallet> walletOptional = walletService.findByNumber(number);

        if (!walletOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Wallet with number %s not found.", number));
        }

        var wallet = walletOptional.get();

        wallet.deposit(walletTransactionDto.getValue());

        return ResponseEntity.status(HttpStatus.OK).body(walletService.save(wallet));
    }

    @PutMapping("/{number}/withdraw")
    public ResponseEntity<Object> walletWithdraw(@PathVariable(value = "number") int number, @RequestBody @Valid WalletTransactionDto walletTransactionDto) {
        Optional<Wallet> walletOptional = walletService.findByNumber(number);

        if (!walletOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Wallet with number %s not found.", number));
        }

        var wallet = walletOptional.get();

        try {
            wallet.withdraw(walletTransactionDto.getValue());
        } catch (WithdrawException exception) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(walletService.save(wallet));
    }

    @GetMapping("/{number}/assets")
    public ResponseEntity<Object> getWalletAssets(@PathVariable(value = "number") int number) {
        Optional<Wallet> walletOptional = walletService.findByNumber(number);

        if (!walletOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Wallet with number %s not found.", number));
        }

        Wallet wallet = walletOptional.get();

        return ResponseEntity.status(HttpStatus.OK).body(assetService.findAssetsByWallet(wallet));
    }

    @PostMapping("/{number}/assets/purchase")
    public ResponseEntity<Object> purchaseAsset(@PathVariable(value = "number") int number, @RequestBody @Valid PurchaseAssetDto purchaseAssetDto) {
        Optional<Wallet> walletOptional = walletService.findByNumber(number);
        Optional<Asset> assetOptional = assetService.findByCode(purchaseAssetDto.getAssetCode());

        if (!walletOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Wallet with number %s not found.", number));
        }

        if (!assetOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Asset with code %s not found.", purchaseAssetDto.getAssetCode()));
        }

        Wallet wallet = walletOptional.get();
        Asset asset = assetOptional.get();
        int quantity = purchaseAssetDto.getQuantity();
        PurchasedAsset purchasedAsset;

        try {
            purchasedAsset = assetService.purchase(wallet, asset, quantity);
        } catch (PurchaseException exception) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(purchasedAsset);
    }

    @PostMapping("/{number}/assets/sale")
    public ResponseEntity<Object> saleAsset(@PathVariable(value = "number") int number, @RequestBody @Valid SaleAssetDto saleAssetDto) {
        Optional<Wallet> walletOptional = walletService.findByNumber(number);
        Optional<Asset> assetOptional = assetService.findByCode(saleAssetDto.getAssetCode());

        if (!walletOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Wallet with number %s not found.", number));
        }

        if (!assetOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Asset with code %s not found.", saleAssetDto.getAssetCode()));
        }

        Wallet wallet = walletOptional.get();
        Asset asset = assetOptional.get();
        int quantity = saleAssetDto.getQuantity();
        PurchasedAsset purchasedAsset;

        try {
            purchasedAsset = assetService.sale(wallet, asset, quantity);
        } catch (SaleException exception) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(purchasedAsset);
    }

}
