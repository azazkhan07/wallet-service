package com.novapay.wallet_service.util;

import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class WalletNumberGenerator {
    public String generate() {
        return "WALLET-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public String referenceIdGenerator(){
        return "reference-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
