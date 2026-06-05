package com.novapay.wallet_service.mapper;


import com.novapay.wallet_service.dto.response.WalletResponse;
import com.novapay.wallet_service.entity.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletResponse toResponseDTO(Wallet wallet);
}
