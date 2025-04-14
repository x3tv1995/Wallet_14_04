package ru.lesson.wallet.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.lesson.wallet.model.OperationType;
import ru.lesson.wallet.model.Wallet;
import ru.lesson.wallet.myException.MinusBalanceException;
import ru.lesson.wallet.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;



    public Wallet walletSearch(UUID walletId) {
        return walletRepository.findByWalletId(walletId).orElseThrow(
                ()-> new RuntimeException("Кошелёк не найден"+ walletId));
    }
    public BigDecimal getBalance(UUID walletId) {
        Optional<Wallet> byId = walletRepository.findById(walletId);
        if (byId.isPresent()) {
            return byId.get().getBalance();
        }
        return BigDecimal.ZERO;
    }

    @Transactional(rollbackOn = Exception.class)
    public Wallet walletBalanceChanges(UUID walletId, OperationType operationType, BigDecimal amount) throws MinusBalanceException {
        Wallet wallet = walletSearch(walletId);
        if (operationType == OperationType.DEPOSIT) {
            wallet.setBalance(wallet.getBalance().add(amount));
        } else if (operationType == OperationType.WITHDRAW) {
            if (wallet.getBalance().compareTo(amount) < 0) {
                throw new MinusBalanceException(wallet.getBalance(), amount);
            }
            wallet.setBalance(wallet.getBalance().subtract(amount));
        }
        return walletRepository.save(wallet);
    }

    public Wallet saveWallet(Wallet wallet) {
        Wallet wallet1 = walletRepository.findByWalletId(wallet.getWalletId()).orElse(null);
        if (wallet1 != null) {
            return  wallet1;
        }else {
            return walletRepository.save(wallet);
        }
    }

}
