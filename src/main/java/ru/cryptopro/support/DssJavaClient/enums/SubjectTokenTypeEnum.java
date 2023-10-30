package ru.cryptopro.support.DssJavaClient.enums;

import lombok.Getter;

@Getter
public enum SubjectTokenTypeEnum {
    JWT("urn:ietf:params:oauth:token-type:jwt");

    private final String value;

    SubjectTokenTypeEnum(String value) {
        this.value = value;
    }
}
