package ru.lesson.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.lesson.wallet.model.OperationType;
import ru.lesson.wallet.model.Wallet;
import ru.lesson.wallet.model.dto.WalletDTO;
import ru.lesson.wallet.model.WalletTransactionRequest;
import ru.lesson.wallet.repository.WalletRepository;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WalletRepository walletRepository;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        walletRepository.deleteAll();
    }

    @Test
    void testProcessTransactionDeposit() throws Exception {
        Wallet wallet = new Wallet(null, BigDecimal.valueOf(100));
        wallet = walletRepository.save(wallet);


        WalletTransactionRequest request = new WalletTransactionRequest();
        request.setWalletId(wallet.getWalletId());
        request.setOperationType(OperationType.DEPOSIT);
        request.setAmount(BigDecimal.valueOf(50));

        MvcResult result = mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();


        String responseBody = result.getResponse().getContentAsString();
        WalletDTO response = objectMapper.readValue(responseBody, WalletDTO.class);

        assert response.getWalletId().equals(wallet.getWalletId());
        assert response.getBalance().compareTo(BigDecimal.valueOf(150)) == 0;
    }


    @Test
    void testGetBalance() throws Exception {

        Wallet wallet = new Wallet(null, BigDecimal.valueOf(200));
        wallet = walletRepository.save(wallet);

        // Выполняем GET-запрос
        mockMvc.perform(get("/api/v1/wallets/{WALLET_UUID}", wallet.getWalletId()))
                .andExpect(status().isOk())
                .andExpect(content().string("200.00"));
    }

    @Test
    void testProcessTransactionWithdrawWithInsufficientBalance() throws Exception {
        BigDecimal balance = BigDecimal.valueOf(50);
        Wallet wallet = new Wallet(null, balance);
        wallet = walletRepository.save(wallet);


        WalletTransactionRequest request = new WalletTransactionRequest();
        request.setWalletId(wallet.getWalletId());
        request.setOperationType(OperationType.WITHDRAW);
        request.setAmount(BigDecimal.valueOf(100));


        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Недостаточно средств: Ваш Баланс: "+balance));
    }
}