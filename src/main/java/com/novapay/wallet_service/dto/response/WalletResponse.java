package com.novapay.wallet_service.dto.response;

import com.novapay.wallet_service.entity.enums.CurrencyCode;
import com.novapay.wallet_service.entity.enums.WalletStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "wallet response")
public record WalletResponse(
        @Schema(example = "1")
        Long id,
        @Schema(example = "WAL123456")
        String walletNumber,
        @Schema(example = "CCJ48R7V23234")
        Long userId,
        WalletStatus status,
        CurrencyCode currency,
        @Schema(example = "2026-02-06T12:30:00")
        LocalDateTime createdAt

) {
}
