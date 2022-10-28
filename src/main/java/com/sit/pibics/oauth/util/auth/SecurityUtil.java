package com.sit.pibics.oauth.util.auth;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Calendar;
import java.util.Locale;

import javax.xml.bind.DatatypeConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sit.pibics.oauth.core.config.parameter.domain.Certification;
import com.sit.pibics.oauth.util.JsonUtil;

import util.calendar.CalendarUtil;
import util.cryptography.PKIUtil;
import util.cryptography.enums.KeyStoreType;
import util.tranp.TranpUtil;

public class SecurityUtil {
	private Logger logger = LogManager.getLogger(this.getClass().getSimpleName());

	private static String keyTool = "$2a$08$";
	private SecureRandom random = new SecureRandom();

	private Logger getLogger() {
		return logger;
	}

	/**
	 * generate client id
	 * 
	 * @return
	 * @throws Exception
	 */
	public String genClientId() {
		return Hex.toHexString(SecureRandom.getSeed(24));
	}
	
	/**
	 * generate secret
	 * 
	 * @return
	 * @throws Exception
	 */
	public String genSecret() {
		return DatatypeConverter.printBase64Binary(SecureRandom.getSeed(32));
	}

	/**
	 * generate salt for encrypt id
	 * 
	 * @return
	 * @throws Exception
	 */
	public String genSalt() {
		String salt = null;
		try {
			salt = BCrypt.gensalt(8, random);
			salt = salt.substring(keyTool.length(), salt.length());
			
		} catch (Exception e) {
			getLogger().error("");
			throw e;
		}
		return salt;
	}
	
	/**
	 * create JWT Access Token
	 * 
	 * @param secret
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	public String genAccessToken(String secret, Payload payload, long expire) throws JsonProcessingException {
		String accessToken = null;
		
		if (payload != null && secret != null && !secret.isEmpty()) {
			Calendar calendar = Calendar.getInstance(Locale.ENGLISH);			
			long current = calendar.getTimeInMillis();
			long exp = current + expire;
			payload.setExp(exp);

			// convert obj to json
			String content = JsonUtil.convertObject2Json(payload); 

			Jwt jwt = createJwt(secret, content);
			if(jwt != null) {
				accessToken = jwt.getEncoded();
			}
		}

		return accessToken;
	}
	
	/**
	 * create JWT Access Token
	 * 
	 * @param cer
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	public String genAccessToken(Certification cer, Payload payload, long expire) throws JsonProcessingException, Exception {
		String accessToken = null;
		
		if (cer != null && payload != null) {
			Calendar calendar = Calendar.getInstance(Locale.ENGLISH);			
			long current = calendar.getTimeInMillis();
			long exp = current + expire;
			payload.setExp(exp);

			// convert obj to json
			String content = JsonUtil.convertObject2Json(payload); 

			// Get private key
			PrivateKey privateKey = PKIUtil.getPrivateKey(KeyStoreType.PKCS12
					, new File(cer.getPrivateKeyFile())
					, cer.getAlias()
					, cer.getPwd()
					);
			
			Jwt jwt = createJwt((RSAPrivateKey) privateKey, content);
			if(jwt != null) {
				accessToken = jwt.getEncoded();
			}
			
		}

		return accessToken;
	}
	
	/**
	 * Create JWT (MacSigner)
	 * 
	 * @param content
	 * @param secret
	 * @return
	 * @throws Exception
	 */
	private Jwt createJwt(String secret, String content) {
		Jwt jwt = null;
		
		if (secret != null && !secret.isEmpty() && content != null && !content.isEmpty()) {
			// create jwt
			MacSigner signer = new MacSigner(secret);
			jwt = JwtHelper.encode(content, signer);

		}

		return jwt;
	}
	
	/**
	 * Create JWT (RsaSigner)
	 * @param privateKey
	 * @param content
	 * @return
	 */
	private Jwt createJwt(RSAPrivateKey privateKey, String content) {
		Jwt jwt = null;
		if (privateKey != null && content != null && !content.isEmpty()) {
			// create jwt
			RsaSigner signer = new RsaSigner(privateKey);
			jwt = JwtHelper.encode(content, signer);
		}
		return jwt;
	}
	
	/**
	 * Verify token
	 * 
	 * @param conn
	 * @param token
	 * @param userId (Encrypted)
	 * @return
	 * @throws Exception
	 */
	public boolean validateAccessToken(String secret, String token) {
		boolean isValid = false;
		try {
			// verify token
			if (secret != null && !secret.isEmpty()) {
				boolean isTrust = verifyToken(secret, token);
				if (isTrust) {
					// validate token expire
					isValid = checkJwtExpired(token, Payload.class);
				}
			}

		} catch (Exception e) {
			getLogger().error("");
			throw e;
		}
		
		return isValid;
	}
	
	/**
	 * Verify token
	 * @param publicKey
	 * @param token
	 * @return
	 */
	public String validateAccessToken(RSAPublicKey publicKey, String token) {
		String validFlag = "V";
		try {
			// verify token
			if (publicKey != null) {
				boolean isTrust = verifyToken(publicKey, token);
				if (isTrust) {
					// validate token expire
					validFlag = checkJwtExpired(token, Payload.class)? "P":"E";
				} 
			}

		} catch (Exception e) {
			getLogger().error("");
			throw e;
		}
		
		return validFlag;
	}
	
	/**
	 * verify JWT token (MacSigner)
	 * 
	 * @param secret
	 * @param token
	 * @return
	 */
	private boolean verifyToken(String secret, String token) {
		boolean isTrust = false;
		try {
			Jwt jwt = JwtHelper.decode(token);
			SignatureVerifier verifier = new MacSigner(secret);
			jwt.verifySignature(verifier);

			isTrust = true;

		} catch (Exception e) {
			isTrust = false;
		}
		return isTrust;
	}
	
	/**
	 * verify JWT token (RsaVerifier)
	 * @param publicKey
	 * @param token
	 * @return
	 */
	private boolean verifyToken(RSAPublicKey publicKey, String token) {
		boolean isTrust = false;
		try {
			Jwt jwt = JwtHelper.decode(token);
			RsaVerifier verifier = new RsaVerifier(publicKey);
			jwt.verifySignature(verifier);

			isTrust = true;

		} catch (Exception e) {
			isTrust = false;
		}
		return isTrust;
	}
	
	/**
	 * validate JWT Token expired
	 * 
	 * @param token
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	private boolean checkJwtExpired(String token, Class<?> clazz) {
		boolean isValid = false;
		try {
			Jwt jwt = JwtHelper.decode(token);
			Payload payload = (Payload) JsonUtil.convertJson2Object(jwt.getClaims(), clazz);

			Calendar calendar = Calendar.getInstance(Locale.ENGLISH);			
			long current = calendar.getTimeInMillis();
			long exp = payload.getExp();
			
			if(getLogger().isDebugEnabled()) {
				getLogger().debug("Now [{}] ", CalendarUtil.getHH24MISS(calendar));
				getLogger().debug("Now long value [{}] ", current);
				
				calendar.setTimeInMillis(exp);
				getLogger().debug("Expire long value [{}] ", exp);
				getLogger().debug("Expire [{}] ", CalendarUtil.getHH24MISS(calendar));				
			}

			if (exp >= current) {
				isValid = true;
			}

		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
		
		return isValid;
	}
	
	/**
	 * encrypt id or pk id for search and user id
	 * 
	 * @param salt
	 * @param secret
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String encryptId(String salt, String secret, String id) {
		String encryptedId = null;
		
		if(id != null && !id.isBlank()) {
			CharSequence secretHex = Hex.toHexString(secret.getBytes());
			CharSequence saltHex = Hex.toHexString(salt.getBytes());

			TextEncryptor encryptor = Encryptors.text(secretHex, saltHex);
			encryptedId = encryptor.encrypt(id);
		}

		return encryptedId;
	}
	
	/**
	 * decrypt id & pk for search
	 * 
	 * @param salt
	 * @param secret
	 * @param encryptedId
	 * @return
	 * @throws Exception
	 */
	public String decryptId(String salt, String secret, String encryptedId) {
		String id = null;
		
		if(encryptedId != null && !encryptedId.isBlank()) {
			CharSequence secretHex = Hex.toHexString(secret.getBytes());
			CharSequence saltHex = Hex.toHexString(salt.getBytes());

			TextEncryptor encryptor = Encryptors.text(secretHex, saltHex);
			id = encryptor.decrypt(encryptedId);
		}

		return id;
	}
	
	/**
	 * Get token expire millisecond
	 * 
	 * @param loginExpire
	 * @return
	 */
	public long getAccessTokenExpireMillis(long loginExpire) {
		return loginExpire * 60L * 1000L;
	}
	
	
	/**
	 * Get Payload from Token and search salt & secret by userIdEncrypted (no verify)
	 * 
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	public Payload getPayloadFromToken(String accessToken) throws JsonProcessingException, IOException {
		Payload payload = null;
		
		if(accessToken != null && !accessToken.isEmpty()) {
			Jwt decrypeData = JwtHelper.decode(accessToken);
			String content = decrypeData.getClaims();			
			payload = (Payload) JsonUtil.convertJson2Object(content, Payload.class);
		}
		
		return payload;
	}	
	
	public String encodeBase64(String data) {
		return Base64.getEncoder().encodeToString(data.getBytes());
	}
	
	public String decodeBase64(String data) {
		return new String(Base64.getDecoder().decode(data), StandardCharsets.UTF_8);
	}
	
	public static void main(String[] args) {
		try {
			SecurityUtil sec = new SecurityUtil();
			String base = sec.encodeBase64("aML0cAq9TKjkkLUP3Okeb.");
			System.out.println(base);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
