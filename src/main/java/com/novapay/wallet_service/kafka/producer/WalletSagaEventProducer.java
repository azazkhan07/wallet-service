package com.novapay.wallet_service.kafka.producer;

import com.novapay.wallet_service.kafka.constants.KafkaTopics;
import com.novapay.wallet_service.saga.event.WalletDebitedEvent;
import com.novapay.wallet_service.saga.event.WalletRefundedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WalletSagaEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishWalletDebited(WalletDebitedEvent event) {

        log.info("Publishing WALLET_DEBITED event | paymentReference={}", event.paymentReference());

        kafkaTemplate.send(KafkaTopics.WALLET_EVENTS,
                event.paymentReference(),
                event);
    }

    public void publishWalletRefunded(WalletRefundedEvent event) {

        log.info("Publishing WALLET_REFUNDED event | paymentReference={}", event.paymentReference());

        kafkaTemplate.send(
                KafkaTopics.WALLET_EVENTS,
                event.paymentReference(),
                event);
    }
}
