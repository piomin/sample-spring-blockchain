package pl.piomin.service.blockchain;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthCoinbase;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import pl.piomin.service.blockchain.model.BlockchainTransaction;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BlockchainTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockchainTest.class);

    @Container
    static final GenericContainer clientEthereum = new GenericContainer("ethereum/client-go")
            .withCommand("--http", "--http.corsdomain=*", "--http.addr=0.0.0.0", "--dev")
            .withExposedPorts(8545);

    @DynamicPropertySource
    static void registerCeProperties(DynamicPropertyRegistry registry) {
        registry.add("web3j.client-address",
                () -> String.format("http://localhost:%d", clientEthereum.getFirstMappedPort()));
    }

    @Autowired
    Web3j web3j;

    @Test
    void shouldStart() {

    }

    @Test
    @Order(1)
    void shouldCreateAccounts() throws CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        LOGGER.info("Generating wallet file...");
        String file = WalletUtils.generateFullNewWalletFile("piot123", null);
        Credentials c = WalletUtils.loadCredentials("piot123", file);
        LOGGER.info("Generating wallet file: {}", file);

        NewAccountIdentifier acc1 = admin.personalNewAccount("123456").send();
        LOGGER.info("Account created: {}", acc1.getId());
        NewAccountIdentifier acc2 = admin.personalNewAccount("123457").send();
        LOGGER.info("Account created: {}", acc2.getId());

        EthCoinbase coinbase = web3j.ethCoinbase().send();
        LOGGER.info("Coinbase: {}", coinbase.getAddress());

        Transaction transaction = Transaction.createEtherTransaction(coinbase.getAddress(), BigInteger.ZERO, BigInteger.valueOf(1000), BigInteger.valueOf(21_000), acc1.getAccountId(), BigInteger.valueOf(1000));
        EthSendTransaction trx = web3j.ethSendTransaction(transaction).send();
        LOGGER.info("Trx: {}", trx.getTransactionHash());

        EthAccounts accounts = web3j.ethAccounts().send();

        LOGGER.info("Accounts size: ", accounts.getAccounts().size());
        accounts.getAccounts().forEach(acc -> LOGGER.info("Account", acc));
    }

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    Admin admin;

    @Test
    void shouldRunTransaction() throws IOException {
        BlockchainTransaction trx = new BlockchainTransaction(1, 2, 100);
        trx = restTemplate.postForObject("/transaction", trx, BlockchainTransaction.class);
        LOGGER.info("Trx: {}", trx);
    }
}
