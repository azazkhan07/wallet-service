package com.novapay.wallet_service.repository;

import com.novapay.wallet_service.entity.WalletBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletBalanceRepository extends JpaRepository<WalletBalance, Long> {

    Optional<WalletBalance> findByWalletId(Long walletId);
}
