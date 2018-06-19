package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
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
        //LOGGER.info("Generating wallet file...");
        //String file = WalletUtils.generateLightNewWalletFile("piot123", null);
        //String to = "0x948ed3ec7cbec25961990535508a0f950ed5d247";
        //Credentials c = WalletUtils.loadCredentials("piot123", "UTC--2018-06-18T14-37-57.614000000Z--816804233e3beaf65f854f9f7a76ace1d1e78f07.json");
        //LOGGER.info("Generating wallet file: {}", file);
        //EthGetTransactionCount egtc = web3j.ethGetTransactionCount(c.getAddress(), DefaultBlockParameterName.LATEST).send();
        //LOGGER.info("Number of transactions: {}", egtc.getTransactionCount().intValue());


        //try {
        //    TransactionReceipt r = Transfer.sendFunds(web3j, c, to, BigDecimal.valueOf(3L), Convert.Unit.ETHER).send();
        //    LOGGER.info("Transaction: {}", r.getBlockHash());
        //} catch (Exception e) {
        //    LOGGER.error("Error sending transaction", e);
        //}
        //EthAccounts accounts = web3j.ethAccounts().send();
        //accounts.getAccounts().forEach(acc -> LOGGER.info("Account: {}", acc));
        //EthGetBalance balance = web3j.ethGetBalance(accounts.getAccounts().get(1), DefaultBlockParameterName.LATEST).send();
        //BigInteger i = balance.getBalance();
        //LOGGER.info("Balance get: {}", balance.getBalance().longValue());
        //RawTransaction trx = RawTransaction.createEtherTransaction(egtc.getTransactionCount(), BigInteger.valueOf(3L), BigInteger.valueOf(100L), to, BigInteger.valueOf(10L));
        //LOGGER.info("Transaction created: {}", trx.getData());
        //byte[] signedMessage = TransactionEncoder.signMessage(trx, c);
        //String hexValue = Numeric.toHexString(signedMessage);
        //EthSendTransaction trxSent = web3j.ethSendRawTransaction(hexValue).send();
        //if (trxSent.hasError()) {
        //    LOGGER.info("Transaction error: {}", trxSent.getError().getMessage());
        //}
        //LOGGER.info("Transaction sent: result={}, hash={}", trxSent.getResult(), trxSent.getTransactionHash());

        EthCoinbase coinbase = web3j.ethCoinbase().send();
        EthAccounts accounts = web3j.ethAccounts().send();
        EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(coinbase.getAddress(), DefaultBlockParameterName.LATEST).send();
        Transaction transaction = Transaction.createEtherTransaction(coinbase.getAddress(), transactionCount.getTransactionCount(), BigInteger.valueOf(3L), BigInteger.valueOf(21_000), accounts.getAccounts().get(1),BigInteger.valueOf(3L));
        EthSendTransaction response = web3j.ethSendTransaction(transaction).send();

        String txHash = response.getTransactionHash();
        LOGGER.info("Tx hash: {}", txHash);
        EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(txHash).send();
        if (receipt.getTransactionReceipt().isPresent()) {
            LOGGER.info("Tx receipt: {}", receipt.getTransactionReceipt().get().getCumulativeGasUsed().intValue());
        }
    }

}
