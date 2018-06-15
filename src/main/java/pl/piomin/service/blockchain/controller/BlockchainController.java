package pl.piomin.service.blockchain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.CipherException;
import pl.piomin.service.blockchain.service.BlockchainService;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@RestController
public class BlockchainController {

    @Autowired
    BlockchainService service;

    @GetMapping("/execute")
    public String execute() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CipherException, IOException {
        service.process();
        return "ok";
    }

}
