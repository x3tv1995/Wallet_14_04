package ru.lesson.wallet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallets")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Wallet {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "wallet_id", updatable = false, nullable = false)
    private UUID walletId;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

}
