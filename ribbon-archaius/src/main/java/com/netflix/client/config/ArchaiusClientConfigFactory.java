package com.netflix.client.config;


/**
 * Archaius的客户端配置工厂
 */
public class ArchaiusClientConfigFactory implements ClientConfigFactory {
    @Override
    public IClientConfig newConfig() {
        return new DefaultClientConfigImpl();
    }
}
