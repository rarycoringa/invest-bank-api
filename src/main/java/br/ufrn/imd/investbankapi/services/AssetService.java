package br.ufrn.imd.investbankapi.services;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import br.ufrn.imd.investbankapi.models.Asset;
import br.ufrn.imd.investbankapi.repositories.AssetRepository;

@Service
public class AssetService {
    
    final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Transactional
    public Asset save(Asset asset) {
        return assetRepository.save(asset);
    }
}
