package com.novapay.wallet_service.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DebitRequest {
    @NotNull
    private Long walletId;
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;
}