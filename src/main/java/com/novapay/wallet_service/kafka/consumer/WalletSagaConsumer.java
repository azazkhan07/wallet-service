package com.novapay.wallet_service.kafka.consumer;

import com.novapay.wallet_service.dto.request.CreditRequest;
import com.novapay.wallet_service.dto.request.DebitRequest;
import com.novapay.wallet_service.kafka.constants.KafkaTopics;
import com.novapay.wallet_service.kafka.producer.WalletSagaEventProducer;
import com.novapay.wallet_service.saga.event.DebitWalletCommand;
import com.novapay.wallet_service.saga.event.WalletDebitedEvent;
import com.novapay.wallet_service.saga.event.WalletRefundCommand;
import com.novapay.wallet_service.saga.event.WalletRefundedEvent;
import com.novapay.wallet_service.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@RetryableTopic(
        attempts = "4",
        backoff = @Backoff(
                delay = 2000,
                multiplier = 2.0))
@KafkaListener(
        topics = KafkaTopics.WALLET_COMMANDS,
        groupId = "wallet-saga-group")
public class WalletSagaConsumer{

    private final WalletService walletService;
    private final WalletSagaEventProducer walletSagaEventProducer;

    @KafkaHandler
    public void handleDebitWallet(DebitWalletCommand command) {

        log.info("Received DEBIT_WALLET command | paymentReference={} walletId={} amount={}",
                command.paymentReference(),
                command.payerWalletId(),
                command.amount());

        DebitRequest request = new DebitRequest();
        request.setWalletId(command.payerWalletId());
        request.setAmount(command.amount());

        walletService.debitWallet(request);

        walletSagaEventProducer.publishWalletDebited(
                new WalletDebitedEvent(
                        command.paymentReference(),
                        command.payerWalletId(),
                        command.payeeWalletId(),
                        command.amount()));
    }

    @KafkaHandler
    public void handleRefundWallet(WalletRefundCommand command) {

        log.info("Received REFUND_WALLET command | paymentReference={} walletId={} amount={}",
                command.paymentReference(),
                command.payerWalletId(),
                command.amount());

        CreditRequest request = new CreditRequest();
        request.setWalletId(command.payerWalletId());
        request.setAmount(command.amount());

        walletService.creditWallet(request);

        walletSagaEventProducer.publishWalletRefunded(
                new WalletRefundedEvent(
                        command.paymentReference(),
                        command.payerWalletId(),
                        command.amount()));
    }

    @DltHandler
    public void handleDlt(Object event) {
        log.error("Message moved to DLT after retries exhausted. Event: {}", event);
    }
}
