package ru.cryptopro.support.DssJavaClient.enums;

import lombok.Getter;

@Getter
public enum GrantTypeEnum {
    PASSWORD("password"),
    CODE("authorization_code"),
    TOKEN_EXCHANGE("urn:ietf:params:oauth:grant-type:token-exchange");

    private final String value;

    GrantTypeEnum(String value) {
        this.value = value;
    }
}
