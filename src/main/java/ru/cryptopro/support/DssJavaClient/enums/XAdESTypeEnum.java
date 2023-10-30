package ru.cryptopro.support.DssJavaClient.enums;

public enum XAdESTypeEnum {
    None(0),
    BES(1);
    private final int value;
    private final boolean FTSSignature;
    XAdESTypeEnum(int value) {
        this.value = value;
        this.FTSSignature = false;
    }
    XAdESTypeEnum(int value, boolean FTSSignature) {
        this.value = value;
        this.FTSSignature = FTSSignature;
    }
}
