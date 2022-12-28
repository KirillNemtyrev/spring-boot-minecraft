package com.community.server.utils;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class KeyUtils {
	private static KeyPair keyPair = KeyUtils.generateKey();

	private KeyUtils() {}

	public static PublicKey getSignaturePublicKey() {
		return keyPair.getPublic();
	}

	public static String sign(String data) {
		try {
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(keyPair.getPrivate(), new SecureRandom());
			signature.update(data.getBytes(UTF_8));
			return Base64.getEncoder().encodeToString(signature.sign());
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}
	public static KeyPair generateKey() {
		try {
			KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
			gen.initialize(4096, new SecureRandom());
			return gen.genKeyPair();
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

	public static String toPEMPublicKey(PublicKey key) {
		byte[] encoded = ((RSAPublicKey) key).getEncoded();
		return "-----BEGIN PUBLIC KEY-----\n" +
				Base64.getMimeEncoder(76, new byte[] { '\n' }).encodeToString(encoded) +
				"\n-----END PUBLIC KEY-----\n";
	}
}
