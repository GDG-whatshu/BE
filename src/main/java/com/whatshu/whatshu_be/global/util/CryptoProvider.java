package com.whatshu.whatshu_be.global.util;

public interface CryptoProvider {

    public String encode(Object data);
    public Object decode(String token);
}
