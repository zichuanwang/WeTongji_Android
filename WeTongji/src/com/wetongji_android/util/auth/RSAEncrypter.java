package com.wetongji_android.util.auth;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.wetongji_android.R;

import android.content.Context;
import android.util.Base64;

public class RSAEncrypter {
	
	public static String encrypt(String origin, Context context){
		InputStream instream=context.getResources().openRawResource(R.raw.wetongji_public_key);
		try {
			CertificateFactory cf=CertificateFactory.getInstance("X.509");
			X509Certificate cert=(X509Certificate) cf.generateCertificate(instream);
			instream.close();
			PublicKey pkPublic=cert.getPublicKey();
			Cipher pkCipher=Cipher.getInstance("RSA/ECB/PKCS1PADDING");
			pkCipher.init(Cipher.ENCRYPT_MODE, pkPublic);
			byte[] encryptedInByte=pkCipher.doFinal(origin.getBytes());
			return Base64.encodeToString(encryptedInByte, Base64.NO_WRAP);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		}
		return null;
	}

}
