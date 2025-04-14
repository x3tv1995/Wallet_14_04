package ru.lesson.wallet.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WalletTransactionRequest {
    @NotNull(message = "Идентификатор кошелька не может быть нулевым")
    private UUID walletId;

    @NotNull(message = "Тип операции не может быть равен null")
    private OperationType operationType;

    @NotNull(message = "Сумма не может быть нулевой")
    @DecimalMin(value = "0.01", message = "Сумма должна быть больше 0,01")
    private BigDecimal amount;
}
