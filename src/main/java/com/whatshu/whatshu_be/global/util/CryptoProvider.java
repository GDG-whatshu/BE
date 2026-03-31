package com.whatshu.whatshu_be.global.util;

public interface CryptoProvider {

    String encode(Object data);
    Object decode(String token);
}
