package ru.cryptopro.support.DssJavaClient.util;

public class Consts {
    public static final String OAUTH_REDIRECT = "urn:ietf:wg:oauth:2.0:oob:auto";
    public static final String SCOPE = "dss";
    public static final String RESOURCE_PREFIX = "urn:cryptopro:dss:signserver:";
    public static final String AUTH_RESPONSE_EXAMPLE = "{\n" +
            "  \"access_token\": \"eyJ0eXAiO......CjbWZGTg\",\n" +
            "  \"expires_in\": 300,\n" +
            "  \"token_type\": \"Bearer\"\n" +
            "}";
    public static final String SIGN_REQUEST_EXAMPLE = "{\n" +
            "\"docs\":[\n" +
            "\"1c3f5f0d-2cfc-4b1e-82d7-f8a128fc0b9b\"\n" +
            "],\n" +
            "\"Signature\":{\n" +
            "\"CertificateId\":0,\n" +
            "\"PinCode\":\"\",\n" +
            "\"ProcessingTemplateId\":2\n" +
            "},\n" +
            "\"params\":{\n" +
            "\"forceConfirmation\":true" +
            "}\n" +
            "}";
}
