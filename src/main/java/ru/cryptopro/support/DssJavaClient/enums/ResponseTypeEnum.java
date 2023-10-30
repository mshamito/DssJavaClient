package ru.cryptopro.support.DssJavaClient.enums;

import lombok.Getter;

@Getter
public enum ResponseTypeEnum {
    CODE("code");

    private final String value;

    ResponseTypeEnum(String value) {
        this.value = value;
    }
}
