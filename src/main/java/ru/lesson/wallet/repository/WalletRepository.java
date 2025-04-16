package ru.lesson.wallet.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.lesson.wallet.model.Wallet;

import java.util.Optional;
import java.util.UUID;

@Repository
@Component
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Wallet> findByWalletId(UUID walletId);
}
