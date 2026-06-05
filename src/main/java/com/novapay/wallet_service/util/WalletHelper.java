package com.novapay.wallet_service.util;


import com.novapay.wallet_service.entity.Wallet;
import com.novapay.wallet_service.exception.ResourceNotFoundException;
import com.novapay.wallet_service.repository.WalletRepository;
import org.springframework.stereotype.Component;

@Component
public class WalletHelper {

    private final WalletRepository walletRepository;

    public WalletHelper(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }
    public Wallet findWalletById(Long userId) {
        return walletRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Wallet Not Found with UserId " + userId));
    }
}
