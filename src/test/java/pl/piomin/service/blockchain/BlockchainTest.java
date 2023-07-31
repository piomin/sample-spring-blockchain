package pl.piomin.service.blockchain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.Web3j;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

//@SpringBootTest
//@RunWith(SpringRunner.class)
public class BlockchainTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockchainTest.class);

    @Autowired
    Web3j web3j;

//    @Test
    public void test() throws CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
//        LOGGER.info("Generating wallet file...");
//        String file = WalletUtils.generateFullNewWalletFile("piot123", null);
//        Credentials c = WalletUtils.loadCredentials("piot123", file);
//        LOGGER.info("Generating wallet file: {}", file);
//        EthAccounts accounts = web3j.ethAccounts().send();
//
//        LOGGER.info("Accounts size: ", accounts.getAccounts().size());
//        accounts.getAccounts().forEach(acc -> LOGGER.info("Account", acc));
    }
}
