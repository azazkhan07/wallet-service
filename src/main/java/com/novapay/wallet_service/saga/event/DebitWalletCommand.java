package com.novapay.wallet_service.saga.event;

import java.math.BigDecimal;

public record DebitWalletCommand(
        String paymentReference,
        Long payerWalletId,
        Long payeeWalletId,
        BigDecimal amount
) { }
