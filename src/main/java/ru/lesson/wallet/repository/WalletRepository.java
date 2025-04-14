package ru.lesson.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lesson.wallet.model.Wallet;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Optional<Wallet> findByWalletId(UUID walletId);
}
