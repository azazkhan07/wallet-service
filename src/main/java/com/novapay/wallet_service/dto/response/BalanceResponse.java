package com.novapay.wallet_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record BalanceResponse(
         @Schema(example = "W45155c1sdc1sd")
         Long walletId,
         @Schema(example = "100.00")
         BigDecimal availableBalance,
         @Schema(example = "250.00")
         BigDecimal blockedBalance) {
}
