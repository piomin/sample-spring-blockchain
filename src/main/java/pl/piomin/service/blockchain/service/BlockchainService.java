package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
public class BlockchainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockchainService.class);

    @Autowired
    Web3j web3j;

    public void process() throws CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        LOGGER.info("Generating wallet file...");
        String file = WalletUtils.generateLightNewWalletFile("piot123", null);
        String to = "1afbddf5917346f7f89b82475306ef2b844809d0";
        Credentials c = WalletUtils.loadCredentials("piot123", file);
        LOGGER.info("Generating wallet file: {}", file);
        EthGetTransactionCount egtc = web3j.ethGetTransactionCount(c.getAddress(), DefaultBlockParameterName.LATEST).send();
        LOGGER.info("Number of transactions: {}", egtc.getTransactionCount().intValue());

        EthAccounts accounts = web3j.ethAccounts().send();
        accounts.getAccounts().forEach(acc -> LOGGER.info("Account", acc));
        EthGetBalance balance = web3j.ethGetBalance(accounts.getAccounts().get(0), DefaultBlockParameterName.LATEST).send();
        LOGGER.info("Balance get: {}", balance.getBalance().intValue());
        RawTransaction trx = RawTransaction.createEtherTransaction(egtc.getTransactionCount(), BigInteger.valueOf(3L), BigInteger.valueOf(100L), to, BigInteger.valueOf(10L));
        LOGGER.info("Transaction created: {}", trx.getData());
        byte[] signedMessage = TransactionEncoder.signMessage(trx, c);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction trxSent = web3j.ethSendRawTransaction(hexValue).send();
        if (trxSent.hasError()) {
            LOGGER.info("Transaction error: {}", trxSent.getError().getMessage());
        }
        LOGGER.info("Transaction sent: result={}, hash={}", trxSent.getResult(), trxSent.getTransactionHash());
    }

}
