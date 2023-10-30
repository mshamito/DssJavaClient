package ru.cryptopro.support.DssJavaClient.enums;

import lombok.Getter;

public enum ActorTokenTypeEnum {
    JWT("urn:ietf:params:oauth:token-type:jwt");

    @Getter
    private final String value;

    ActorTokenTypeEnum(String value) {
        this.value = value;
    }
}
