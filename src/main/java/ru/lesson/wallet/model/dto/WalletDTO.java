package ru.lesson.wallet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;
@Data
@AllArgsConstructor
public class WalletDTO {
    private UUID walletId;
    private BigDecimal balance;
}
