package ru.cryptopro.support.DssJavaClient.enums;

import lombok.Getter;

@Getter
public enum SignatureTypeEnum {
    XMLDSig(0),
    GOST3410(1),
    CAdES(2),
    PDF(3),
    MSOffice(4),
    CMS(5);
    private final int value;
    SignatureTypeEnum(int value) {
        this.value = value;
    }
}
