package com.whatshu.whatshu_be.global.config;

import com.whatshu.whatshu_be.global.util.Aes256CryptoProvider;
import com.whatshu.whatshu_be.global.util.CryptoProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QrTokenConfig {

    @Value("${qr.token.secret}")
    private String secretKey;

    @Bean
    public CryptoProvider cryptoProvider() {
        return new Aes256CryptoProvider(secretKey);
    }
}
