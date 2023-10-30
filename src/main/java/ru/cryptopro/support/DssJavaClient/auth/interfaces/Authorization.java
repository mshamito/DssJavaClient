package ru.cryptopro.support.DssJavaClient.auth.interfaces;

import ru.cryptopro.support.DssJavaClient.dto.common.UserCredentialDto;
import ru.cryptopro.support.DssJavaClient.dto.response.AuthResponse;

public interface Authorization {
    AuthResponse login(UserCredentialDto userCredential);
}
