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

import br.ufrn.imd.investbankapi.dtos.CryptocurrencyDto;
import br.ufrn.imd.investbankapi.models.Cryptocurrency;
import br.ufrn.imd.investbankapi.services.CryptocurrencyService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/cryptocurrencies")
public class CryptocurrencyController {
    
    final CryptocurrencyService cryptocurrencyService;

    public CryptocurrencyController(CryptocurrencyService cryptocurrencyService) {
        this.cryptocurrencyService = cryptocurrencyService;
    }

    @GetMapping
    public ResponseEntity<List<Cryptocurrency>> getAllCryptocurrencies() {
        return ResponseEntity.status(HttpStatus.OK).body(cryptocurrencyService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> createCryptocurrency(@RequestBody @Valid CryptocurrencyDto cryptocurrencyDto) {
        if (cryptocurrencyService.existsByCode(cryptocurrencyDto.getCode())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(String.format("Code %s is already in use!", cryptocurrencyDto.getCode()));
        }

        var cryptocurrency = new Cryptocurrency();

        BeanUtils.copyProperties(cryptocurrencyDto, cryptocurrency);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(cryptocurrencyService.save(cryptocurrency));

    }

    @GetMapping("/{code}")
    public ResponseEntity<Object> getOneCryptocurrency(@PathVariable(value = "code") String code) {
        Optional<Cryptocurrency> cryptocurrencyOptional = cryptocurrencyService.findByCode(code);

        if (!cryptocurrencyOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Asset with code %s not found.", code));
        }

        Cryptocurrency cryptocurrency = cryptocurrencyOptional.get();

        return ResponseEntity.status(HttpStatus.OK).body(cryptocurrency);
    }

    @PutMapping("/{code}")
    public ResponseEntity<Object> updateAsset(@PathVariable(value = "code") String code, @RequestBody @Valid CryptocurrencyDto cryptocurrencyDto) {
        Optional<Cryptocurrency> cryptocurrencyOptional = cryptocurrencyService.findByCode(code);

        if (!cryptocurrencyOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Cryptocurrency with code %s not found.", code));
        }

        var cryptocurrency = cryptocurrencyOptional.get();

        cryptocurrency.setCode(cryptocurrencyDto.getCode());
        cryptocurrency.setName(cryptocurrencyDto.getName());
        cryptocurrency.setPrice(cryptocurrencyDto.getPrice());

        return ResponseEntity.status(HttpStatus.OK).body(cryptocurrencyService.save(cryptocurrency));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Object> deleteAsset(@PathVariable(value = "code") String code) {
        Optional<Cryptocurrency> cryptocurrencyOptional = cryptocurrencyService.findByCode(code);

        if (!cryptocurrencyOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Cryptocurrency with code %s not found.", code));
        }

        Cryptocurrency cryptocurrency = cryptocurrencyOptional.get();

        cryptocurrencyService.delete(cryptocurrency);

        return ResponseEntity.status(HttpStatus.OK).body(String.format("Cryptocurrency with code %s successfully deleted!", code));
    }

}
