package ru.lesson.wallet.myException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MinusBalanceException extends Exception {
    public MinusBalanceException(BigDecimal balance, BigDecimal sum) {
        super("Недостаточно средств на счёте \n Баланс: " + balance + "\n Сумма: " + sum);
    }
}
