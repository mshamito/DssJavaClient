package ru.cryptopro.support.DssJavaClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.security.Security;
import java.security.Provider;
import java.util.prefs.Preferences;

@SpringBootApplication
@EnableScheduling
public class DssJavaClientApplication {
	static {
		System.setProperty("com.ibm.security.enableCRLDP", "true"); //crl online
		System.setProperty("com.sun.security.enableCRLDP", "true"); //crl online

		// or disable revocation for tls

//		Preferences prefs = Preferences.userRoot();
//		prefs.node("/ru/CryptoPro/ssl").remove("Enable_CRL_revocation_online_default");
//		prefs.node("/ru/CryptoPro/ssl").remove("Enable_revocation_default");
//		prefs.node("/ru/CryptoPro/ssl").put("Enable_CRL_revocation_online_default", "false");
//		prefs.node("/ru/CryptoPro/ssl").put("Enable_revocation_default", "false");


//		System.setProperty("tls_prohibit_disabled_validation", "false");

//		System.setProperty("com.sun.security.enableAIAcaIssuers", "true"); // для загрузки сертификатов по AIA из сети
//		System.setProperty("ru.CryptoPro.reprov.enableAIAcaIssuers", "true"); // для загрузки сертификатов по AIA из сети

		// https://support.cryptopro.ru/index.php?/Knowledgebase/Article/View/315/6/warning-couldnt-flush-system-prefs-javautilprefsbackingstoreexception--sreate-failed
		System.setProperty("java.util.prefs.syncInterval", "99999");

		try {
			Security.addProvider( (Provider) Class.forName("ru.CryptoPro.JCSP.JCSP").newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Security.addProvider(new ru.CryptoPro.JCP.JCP());
		Security.addProvider(new ru.CryptoPro.reprov.RevCheck());
		Security.addProvider(new ru.CryptoPro.Crypto.CryptoProvider());
		Security.addProvider(new ru.CryptoPro.ssl.Provider());
//		Security.addProvider(new ru.CryptoPro.sspiSSL.SSPISSL());
	}

	public static void main(String[] args) {
		SpringApplication.run(DssJavaClientApplication.class, args);
	}

}
