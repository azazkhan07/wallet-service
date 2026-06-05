package com.novapay.wallet_service.service;

import com.novapay.wallet_service.dto.request.CreditRequest;
import com.novapay.wallet_service.dto.request.DebitRequest;
import com.novapay.wallet_service.dto.response.BalanceResponse;
import com.novapay.wallet_service.dto.response.WalletResponse;

public interface WalletService {

    WalletResponse createWallet(Long userId);

    WalletResponse getWalletByUserId(Long userId);

    BalanceResponse getBalance(Long walletId);

    void creditWallet(CreditRequest request);

    void debitWallet(DebitRequest request);
}
