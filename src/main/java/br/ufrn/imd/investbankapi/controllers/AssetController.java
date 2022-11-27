package br.ufrn.imd.investbankapi.controllers;

import java.util.ArrayList;

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

    @PostMapping()
    public ResponseEntity<Object> createAsset(@RequestBody @Valid AssetDto assetDto) {
        var asset = new Asset();

        BeanUtils.copyProperties(assetDto, asset);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(assetService.save(asset));
    }

    // @GetMapping("/assets")
    // public ArrayList<Asset> getAssets() {
    //     ArrayList<Asset> assets = new ArrayList<Asset>();

    //     Asset google = new Asset(1, "GOGL34", "Google BDR", 44.33, "Stock");
    //     Asset amazon = new Asset(2, "AMZO34", "Amazon BDR", 26.10, "Stock");
    //     Asset microsoft = new Asset(3, "MSFT34", "Microsoft BDR", 54.34, "Stock");
    //     Asset maxi = new Asset(4, "MXRF11", "Maxi Renda FII", 105.16, "Building Fund");

    //     assets.add(google);
    //     assets.add(amazon);
    //     assets.add(microsoft);
    //     assets.add(maxi);

    //     return assets;
    // }
}
