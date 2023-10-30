package ru.cryptopro.support.DssJavaClient.enums;

import lombok.Getter;

public enum CADESTypeEnum {
    BES(0),
    XLT1(1),
    T(2);

    @Getter
    private final int value;

    CADESTypeEnum(int value) {
        this.value = value;
    }
}
