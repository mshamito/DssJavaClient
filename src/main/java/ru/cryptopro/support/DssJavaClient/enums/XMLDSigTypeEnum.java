package ru.cryptopro.support.DssJavaClient.enums;

import lombok.Getter;

public enum XMLDSigTypeEnum {
    XMLEnveloped(0),
    XMLEnveloping(1),
    XMLTemplate(2);

    @Getter
    private final int value;

    XMLDSigTypeEnum(int value) {
        this.value = value;
    }
}
