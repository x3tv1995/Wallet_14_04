package ru.lesson.wallet.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lesson.wallet.model.Wallet;
import ru.lesson.wallet.model.WalletTransactionRequest;
import ru.lesson.wallet.model.dto.WalletDTO;
import ru.lesson.wallet.myException.MinusBalanceException;
import ru.lesson.wallet.service.WalletService;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;


    @PostMapping("/wallet")
    public ResponseEntity<WalletDTO> processTransaction(@Valid @RequestBody WalletTransactionRequest request) throws MinusBalanceException, ExecutionException, InterruptedException {
        Future<Wallet> wallet = walletService.walletBalanceChanges(request.getWalletId(),
                request.getOperationType(), request.getAmount());

        WalletDTO response = new WalletDTO(wallet.get().getWalletId(), wallet.get().getBalance());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/wallets/{WALLET_UUID}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable UUID WALLET_UUID) {
        BigDecimal balance = walletService.getBalance(WALLET_UUID);
        return ResponseEntity.ok(balance);
    }


}
