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

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PostMapping("/save")
    public ResponseEntity<Wallet> saveWallet(Wallet wallet) {
        Wallet wallet1 = walletService.saveWallet(wallet);
        return ResponseEntity.ok(wallet1);
    }

    @PostMapping("/transaction")
    public ResponseEntity<WalletDTO> processTransaction(@Valid @RequestBody WalletTransactionRequest request) throws MinusBalanceException {
        Wallet wallet = walletService.walletBalanceChanges(request.getWalletId(),
                request.getOperationType(), request.getAmount());

        WalletDTO response = new WalletDTO(wallet.getWalletId(), wallet.getBalance());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getBalance")
    public ResponseEntity<BigDecimal> getBalance(UUID walletId) {
        BigDecimal balance = walletService.getBalance(walletId);
        return ResponseEntity.ok(balance);
    }




}
