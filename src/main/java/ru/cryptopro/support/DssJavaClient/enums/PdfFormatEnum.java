package ru.cryptopro.support.DssJavaClient.enums;

import lombok.Getter;

public enum PdfFormatEnum {
    CMS(2),
    CAdEST(0),
    CAdES(1);

    @Getter
    private final int value;

    PdfFormatEnum(int value) {
        this.value = value;
    }
}
