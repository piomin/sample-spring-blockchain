package pl.piomin.service.blockchain.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static pl.piomin.service.blockchain.config.Web3jProperties.WEB3J_PREFIX;

@ConfigurationProperties(prefix = WEB3J_PREFIX)
public class Web3jProperties {

    public static final String WEB3J_PREFIX = "web3j";

    private String clientAddress;

    private Boolean adminClient;

    private String networkId;

    private Long httpTimeoutSeconds;

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public Boolean isAdminClient() {
        return adminClient;
    }

    public void setAdminClient(Boolean adminClient) {
        this.adminClient = adminClient;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public Long getHttpTimeoutSeconds() {
        return httpTimeoutSeconds;
    }

    public void setHttpTimeoutSeconds(Long httpTimeoutSeconds) {
        this.httpTimeoutSeconds = httpTimeoutSeconds;
    }

}
