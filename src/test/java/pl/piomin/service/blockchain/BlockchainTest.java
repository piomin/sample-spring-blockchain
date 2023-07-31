package pl.piomin.service.blockchain;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthAccounts;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class BlockchainTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockchainTest.class);

    @Container
    static final GenericContainer clientEthereum = new GenericContainer("ethereum/client-go")
            .withCommand("--http", "--http.corsdomain=*", "--http.addr=0.0.0.0")
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
    public void test() throws CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        LOGGER.info("Generating wallet file...");
        String file = WalletUtils.generateFullNewWalletFile("piot123", null);
        Credentials c = WalletUtils.loadCredentials("piot123", file);
        LOGGER.info("Generating wallet file: {}", file);
        EthAccounts accounts = web3j.ethAccounts().send();

        LOGGER.info("Accounts size: ", accounts.getAccounts().size());
        accounts.getAccounts().forEach(acc -> LOGGER.info("Account", acc));
    }
}
