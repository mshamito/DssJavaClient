package ru.cryptopro.support.DssJavaClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.reflect.InvocationTargetException;
import java.security.Security;
import java.security.Provider;
import java.util.prefs.Preferences;

@SpringBootApplication
@EnableScheduling
public class DssJavaClientApplication extends SpringBootServletInitializer {
	static {
		System.setProperty("com.ibm.security.enableCRLDP", "true"); // allow download crl online
		System.setProperty("com.sun.security.enableCRLDP", "true"); // allow download crl online

/* or disable revocation for tls
		Preferences prefs = Preferences.userRoot();
		prefs.node("/ru/CryptoPro/ssl").remove("Enable_CRL_revocation_online_default");
		prefs.node("/ru/CryptoPro/ssl").remove("Enable_revocation_default");
		prefs.node("/ru/CryptoPro/ssl").put("Enable_CRL_revocation_online_default", "false");
		prefs.node("/ru/CryptoPro/ssl").put("Enable_revocation_default", "false");
*/


//		System.setProperty("tls_prohibit_disabled_validation", "false");

//		System.setProperty("com.sun.security.enableAIAcaIssuers", "true"); // для загрузки сертификатов из AIA по сети
//		System.setProperty("ru.CryptoPro.reprov.enableAIAcaIssuers", "true"); // для загрузки сертификатов из AIA по сети

		boolean isJCSPExists = addProvider("ru.CryptoPro.JCSP.JCSP");
		if (isJCSPExists) {
			if (!addProvider("ru.CryptoPro.sspiSSL.SSPISSL"))
				addProvider("ru.CryptoPro.ssl.Provider");
		} else {
			addProvider("ru.CryptoPro.JCP.JCP");
			addProvider("ru.CryptoPro.Crypto.CryptoProvider");
			addProvider("ru.CryptoPro.ssl.Provider");
		}
		addProvider("ru.CryptoPro.reprov.RevCheck");

	}

	private static boolean addProvider(String fullName) {
		try {
			Security.addProvider((Provider) Class.forName(fullName).getConstructor().newInstance());
			return true;
		} catch (InstantiationException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException |
				 InvocationTargetException e) {
			System.out.println("Failed add provider: " + fullName);
			return false;
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(DssJavaClientApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(DssJavaClientApplication.class);
	}

}
