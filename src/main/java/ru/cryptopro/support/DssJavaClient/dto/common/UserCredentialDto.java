package ru.cryptopro.support.DssJavaClient.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
public class UserCredentialDto {
    final private String username;
    final private String password;
    @Setter
    private boolean usingOperatorCertForAuth = false;

    public UserCredentialDto(String username,  String password) {
        this.username = username;
        this.password = password;
    }

    public UserCredentialDto(boolean usingOperatorCertForAuth) {
        this.username = null;
        this.password = null;
        this.usingOperatorCertForAuth = usingOperatorCertForAuth;
    }

    public boolean retrieveOperatorTokenOnly() {
        return isUsingOperatorCertForAuth() && username == null;
    }
}