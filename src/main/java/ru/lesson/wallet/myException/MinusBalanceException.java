package ru.lesson.wallet.myException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MinusBalanceException extends Exception {
    public MinusBalanceException(BigDecimal balance) {
        super("Ваш Баланс: " + balance);
    }
}
