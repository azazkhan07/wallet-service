
package com.novapay.wallet_service.controller;

import com.novapay.wallet_service.dto.request.CreditRequest;
import com.novapay.wallet_service.dto.request.DebitRequest;
import com.novapay.wallet_service.dto.request.WalletRequest;
import com.novapay.wallet_service.dto.response.BalanceResponse;
import com.novapay.wallet_service.dto.response.WalletResponse;
import com.novapay.wallet_service.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Wallets APIs", description = "Wallet Management Endpoints")
@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WalletController.class);
    private  final WalletService walletService;


    public WalletController(WalletService walletService) {
        this.walletService = walletService;

    }
    @Operation(summary = "Create wallet for user")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Wallet created"),
    @ApiResponse(responseCode = "409", description = "Wallet already exists")})
    @PostMapping()
    public ResponseEntity<WalletResponse> createWallet(@Valid @RequestBody WalletRequest walletRequest) {
        LOGGER.info("Creating wallet for user {}", walletRequest.getUserId());
        WalletResponse response = walletService.createWallet(walletRequest.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @Operation(summary = "Get wallet for user")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Wallet Found"),
    @ApiResponse(responseCode = "404", description = "Wallet Not Found")})
    @GetMapping("/{userId}")
    public ResponseEntity<WalletResponse> getWallet(@PathVariable Long userId) {
        WalletResponse response = walletService.getWalletByUserId(userId);
        LOGGER.info("Retrieving wallet with id {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get wallet balance")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Balance fetched successfully"),
    @ApiResponse(responseCode = "404", description = "Wallet not found")})
    @GetMapping("/{walletId}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable Long walletId) {
        LOGGER.info("Balance request received for walletId {}", walletId);
        BalanceResponse response = walletService.getBalance(walletId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Credit money into wallet")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Wallet credited successfully"),
    @ApiResponse(responseCode = "404", description = "Wallet not found")})
    @PostMapping("/credit")
    public ResponseEntity<String> creditWallet(@Valid @RequestBody CreditRequest request) {
        LOGGER.info("Credit request received | walletId={} amount={}", request.getWalletId(), request.getAmount());
        walletService.creditWallet(request);
        return ResponseEntity.status(HttpStatus.OK).body("Wallet credited successfully");
    }

    @Operation(summary = "Debit money from wallet")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Wallet debited successfully"),
    @ApiResponse(responseCode = "400", description = "Insufficient balance"),
    @ApiResponse(responseCode = "404", description = "Wallet not found")})
    @PostMapping("/debit")
    public ResponseEntity<String> debitWallet(@Valid @RequestBody DebitRequest request) {
        LOGGER.info("Debit request received | walletId={} amount={}", request.getWalletId(), request.getAmount());
        walletService.debitWallet(request);
        return ResponseEntity.status(HttpStatus.OK).body("Wallet debited successfully");
    }
}
