package br.ufrn.imd.investbankapi.services;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.ufrn.imd.investbankapi.models.Asset;
import br.ufrn.imd.investbankapi.repositories.AssetRepository;

@Service
public class AssetService {
    
    final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
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

}
