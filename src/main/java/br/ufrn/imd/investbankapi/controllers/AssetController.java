package br.ufrn.imd.investbankapi.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.catalina.connector.Response;
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

import br.ufrn.imd.investbankapi.dtos.AssetDto;
import br.ufrn.imd.investbankapi.models.Asset;
import br.ufrn.imd.investbankapi.services.AssetService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/assets")
public class AssetController {

    final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssets() {
        return ResponseEntity.status(HttpStatus.OK).body(assetService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> createAsset(@RequestBody @Valid AssetDto assetDto) {
        if (assetService.existsByCode(assetDto.getCode())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(String.format("Code %s is already in use!", assetDto.getCode()));
        }

        var asset = new Asset();

        BeanUtils.copyProperties(assetDto, asset);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(assetService.save(asset));
    }

    @GetMapping("/{code}")
    public ResponseEntity<Object> getOneAsset(@PathVariable(value = "code") String code) {
        Optional<Asset> assetOptional = assetService.findByCode(code);

        if (!assetOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Asset with code %s not found.", code));
        }

        return ResponseEntity.status(HttpStatus.OK).body(assetOptional.get());
    }

    @PutMapping("/{code}")
    public ResponseEntity<Object> updateAsset(@PathVariable(value = "code") String code, @RequestBody @Valid AssetDto assetDto) {
        Optional<Asset> assetOptional = assetService.findByCode(code);

        if (!assetOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Asset with code %s not found.", code));
        }

        var asset = assetOptional.get();

        asset.setCode(assetDto.getCode());
        asset.setName(assetDto.getName());
        asset.setPrice(assetDto.getPrice());
        asset.setType(assetDto.getType());

        return ResponseEntity.status(HttpStatus.OK).body(assetService.save(asset));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Object> deleteAsset(@PathVariable(value = "code") String code) {
        Optional<Asset> assetOptional = assetService.findByCode(code);

        if (!assetOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Asset with code %s not found.", code));
        }

        assetService.delete(assetOptional.get());

        return ResponseEntity.status(HttpStatus.OK).body(String.format("Asset with code %s successfully deleted!", code));
    }

}
