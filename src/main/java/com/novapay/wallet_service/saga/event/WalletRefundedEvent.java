package com.novapay.wallet_service.saga.event;

import java.math.BigDecimal;

public record WalletRefundedEvent(
        String paymentReference,
        Long payerWalletId,
        BigDecimal amount
) { }
