package br.ufrn.imd.investbankapi.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.imd.investbankapi.dtos.WalletDto;
import br.ufrn.imd.investbankapi.models.Wallet;
import br.ufrn.imd.investbankapi.services.WalletService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/wallets")
public class WalletController {
    
    final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
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

}
