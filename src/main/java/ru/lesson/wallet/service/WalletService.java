package ru.lesson.wallet.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import ru.lesson.wallet.model.OperationType;
import ru.lesson.wallet.model.Wallet;
import ru.lesson.wallet.myException.MinusBalanceException;
import ru.lesson.wallet.myException.WalletNotFoundException;
import ru.lesson.wallet.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;



    public Wallet walletSearch(UUID walletId) {
        return walletRepository.findByWalletId(walletId).orElseThrow(
                () -> new WalletNotFoundException("Кошелёк не найден " + walletId));
    }

    public BigDecimal getBalance(UUID walletId) {
        Optional<Wallet> byId = walletRepository.findById(walletId);
        if (byId.isPresent()) {
            return byId.get().getBalance();
        }
        return BigDecimal.ZERO;
    }

    @Async("taskExecutor")
    @Transactional(rollbackOn = Exception.class)
    public Future<Wallet> walletBalanceChanges(UUID walletId, OperationType operationType, BigDecimal amount) throws MinusBalanceException, ExecutionException, InterruptedException {
        Wallet wallet = walletSearch(walletId);
        if (operationType == OperationType.DEPOSIT) {
            wallet.setBalance(wallet.getBalance().add(amount));
        } else if (operationType == OperationType.WITHDRAW) {
            if (wallet.getBalance().compareTo(amount) < 0) {
                throw new MinusBalanceException(wallet.getBalance());
            }
            wallet.setBalance(wallet.getBalance().subtract(amount));
        }
        return new AsyncResult<>(walletRepository.save(wallet));

    }

}
