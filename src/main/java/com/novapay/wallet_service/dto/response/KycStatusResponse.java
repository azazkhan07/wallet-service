package com.novapay.wallet_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record KycStatusResponse(
        @Schema(example = "true")
        Boolean kycStatus) {
}
