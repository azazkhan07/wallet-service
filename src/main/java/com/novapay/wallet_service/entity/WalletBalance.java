package com.novapay.wallet_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "wallet_balances")
public class WalletBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;
    @Column(name = "available_balance", nullable = false)
    private BigDecimal availableBalance;
    @Column(name= "blocked_balance")
    private BigDecimal blockedBalance;
    @Version
    private Integer version;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
