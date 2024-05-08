package pl.piomin.service.blockchain.config;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.ipc.UnixIpcService;
import org.web3j.protocol.ipc.WindowsIpcService;

import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnClass(Web3j.class)
@EnableConfigurationProperties(Web3jProperties.class)
public class Web3jAutoConfiguration {

    private static Logger LOG = LoggerFactory.getLogger(Web3jAutoConfiguration.class);

    @Autowired
    private Web3jProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public Web3j web3j() {
        Web3jService web3jService = buildService(properties.getClientAddress());
        LOG.info("Building service for endpoint: {}", properties.getClientAddress());
        return Web3j.build(web3jService);
    }

    @Bean
    @ConditionalOnProperty(
            prefix = Web3jProperties.WEB3J_PREFIX, name = "admin-client", havingValue = "true")
    public Admin admin() {
        Web3jService web3jService = buildService(properties.getClientAddress());
        LOG.info("Building admin service for endpoint: {}", properties.getClientAddress());
        return Admin.build(web3jService);
    }

    private Web3jService buildService(String clientAddress) {
        Web3jService web3jService;

        if (clientAddress == null || clientAddress.equals("")) {
            web3jService = new HttpService(createOkHttpClient());
        } else if (clientAddress.startsWith("http")) {
            web3jService = new HttpService(clientAddress, createOkHttpClient(), false);
        } else if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            web3jService = new WindowsIpcService(clientAddress);
        } else {
            web3jService = new UnixIpcService(clientAddress);
        }

        return web3jService;
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        configureLogging(builder);
        configureTimeouts(builder);
        return builder.build();
    }

    private void configureTimeouts(OkHttpClient.Builder builder) {
        Long tos = properties.getHttpTimeoutSeconds();
        if (tos != null) {
            builder.connectTimeout(tos, TimeUnit.SECONDS);
            builder.readTimeout(tos, TimeUnit.SECONDS);  // Sets the socket timeout too
            builder.writeTimeout(tos, TimeUnit.SECONDS);
        }
    }

    private static void configureLogging(OkHttpClient.Builder builder) {
        if (LOG.isDebugEnabled()) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(LOG::debug);
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
    }


//    @Bean
//    @ConditionalOnBean(Web3j.class)
//    Web3jHealthIndicator web3jHealthIndicator(Web3j web3j) {
//        return new Web3jHealthIndicator(web3j);
//    }
}
