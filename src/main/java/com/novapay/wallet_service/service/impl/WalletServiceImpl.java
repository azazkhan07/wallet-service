
package com.novapay.wallet_service.service.impl;

import com.novapay.wallet_service.dto.request.CreditRequest;
import com.novapay.wallet_service.dto.request.DebitRequest;
import com.novapay.wallet_service.dto.response.BalanceResponse;
import com.novapay.wallet_service.dto.response.WalletResponse;
import com.novapay.wallet_service.entity.Wallet;
import com.novapay.wallet_service.entity.WalletBalance;
import com.novapay.wallet_service.entity.enums.CurrencyCode;
import com.novapay.wallet_service.entity.enums.WalletStatus;
import com.novapay.wallet_service.exception.InsufficientBalanceException;
import com.novapay.wallet_service.exception.ResourceNotFoundException;
import com.novapay.wallet_service.exception.WalletAlreadyExistsException;
import com.novapay.wallet_service.mapper.WalletMapper;
import com.novapay.wallet_service.repository.WalletBalanceRepository;
import com.novapay.wallet_service.repository.WalletRepository;
import com.novapay.wallet_service.service.WalletService;
import com.novapay.wallet_service.util.WalletNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WalletServiceImpl.class);

    private final WalletRepository walletRepository;
    private final WalletBalanceRepository walletBalanceRepository;
    private final WalletNumberGenerator walletNumberGenerator;
    private final WalletMapper walletMapper;

    @Transactional
    @Override
    public WalletResponse createWallet(Long userId) {

        if (walletRepository.existsByUserId(userId)) {
            throw new WalletAlreadyExistsException("Wallet already exists for userId" + userId);
        }
        LOGGER.info("creating wallet for userId {}", userId);
        Wallet wallet = Wallet.builder()
                .walletNumber(walletNumberGenerator.generate())
                .userId(userId)
                .status(WalletStatus.ACTIVE)
                .currency(CurrencyCode.INR)
                .build();

        Wallet savedWallet = walletRepository.save(wallet);

        WalletBalance walletBalance = WalletBalance.builder()
                .wallet(savedWallet)
                .availableBalance(BigDecimal.ZERO)
                .blockedBalance(BigDecimal.ZERO)
                .updatedAt(LocalDateTime.now())
                .build();
        walletBalanceRepository.save(walletBalance);
        LOGGER.info("Wallet created for userId {} walletNumber {} ", userId, savedWallet.getWalletNumber());
        return walletMapper.toResponseDTO(savedWallet);
    }

    @Override
    public WalletResponse getWalletByUserId(Long userId) {
        LOGGER.info("Get wallet by user id {}", userId);
        Wallet wallet = walletRepository
                .findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for userId " + userId));
        return walletMapper.toResponseDTO(wallet);
    }

    @Override
    @Transactional(readOnly = true)
    public BalanceResponse getBalance(Long walletId) {

        LOGGER.info("Fetching balance for walletId {}", walletId);

        WalletBalance walletBalance = walletBalanceRepository.findByWalletId(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet balance not found for walletId " + walletId));
        LOGGER.info("Balance fetched successfully for walletId {}", walletId);

        return new BalanceResponse(
                walletId,
                walletBalance.getAvailableBalance(),
                walletBalance.getBlockedBalance()
        );
    }

    @Override
    @Transactional
    public void creditWallet(CreditRequest request) {

        LOGGER.info(
                "Credit request received | walletId={} amount={}",
                request.getWalletId(),
                request.getAmount());

        WalletBalance walletBalance = walletBalanceRepository
                .findByWalletId(request.getWalletId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Wallet balance not found for walletId "
                                + request.getWalletId())
                );

        walletBalance.setAvailableBalance(
                walletBalance.getAvailableBalance()
                        .add(request.getAmount()));
        walletBalance.setUpdatedAt(LocalDateTime.now());
        walletBalanceRepository.save(walletBalance);

        LOGGER.info(
                "Wallet credited successfully | walletId={} amount={} newBalance={}",
                request.getWalletId(),
                request.getAmount(),
                walletBalance.getAvailableBalance());
    }

    @Override
    @Transactional
    public void debitWallet(DebitRequest request) {

        LOGGER.info(
                "Debit request received | walletId={} amount={}",
                request.getWalletId(),
                request.getAmount());

        WalletBalance walletBalance = walletBalanceRepository
                .findByWalletId(request.getWalletId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Wallet balance not found for walletId "
                                + request.getWalletId())
                );

        if (walletBalance.getAvailableBalance()
                .compareTo(request.getAmount()) < 0) {

            LOGGER.warn(
                    "Insufficient balance | walletId={} availableBalance={} requestedAmount={}",
                    request.getWalletId(),
                    walletBalance.getAvailableBalance(),
                    request.getAmount());

            throw new InsufficientBalanceException(
                    "Insufficient balance in wallet");
        }

        walletBalance.setAvailableBalance(
                walletBalance.getAvailableBalance()
                        .subtract(request.getAmount()));

        walletBalance.setUpdatedAt(LocalDateTime.now());

        walletBalanceRepository.save(walletBalance);

        LOGGER.info(
                "Wallet debited successfully | walletId={} amount={} remainingBalance={}",
                request.getWalletId(),
                request.getAmount(),
                walletBalance.getAvailableBalance());
    }
}
