package com.woodrice.util;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DES {

	public static int _DES = 1;
	public static int _DESede = 2;
	public static int _Blowfish = 3;

	private Cipher p_Cipher;
	private SecretKey p_Key;
	private String p_Algorithm;

	private void selectAlgorithm(int al) {
		switch (al) {
		default:
		case 1:
			this.p_Algorithm = "DES";
			break;
		case 2:
			this.p_Algorithm = "DESede";
			break;
		case 3:
			this.p_Algorithm = "Blowfish";
			break;
		}
	}

	public DES(int algorithm) throws Exception {
		this.selectAlgorithm(algorithm);
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		this.p_Cipher = Cipher.getInstance(this.p_Algorithm);
	}

	public byte[] getKey() {
		return this.checkKey().getEncoded();
	}

	private SecretKey checkKey() {
		try {
			if (this.p_Key == null) {
				KeyGenerator keygen = KeyGenerator
						.getInstance(this.p_Algorithm);
				this.p_Key = keygen.generateKey();
			}
		} catch (Exception nsae) {
		}
		return this.p_Key;
	}

	public void setKey(byte[] enckey) {
		this.p_Key = new SecretKeySpec(enckey, this.p_Algorithm);
	}

	public byte[] encode(byte[] data) throws Exception {
		// this.p_Cipher.init(Cipher.ENCRYPT_MODE, this.checkKey());
		this.p_Cipher.init(Cipher.ENCRYPT_MODE, this.p_Key);
		return this.p_Cipher.doFinal(data);
	}

	public byte[] decode(byte[] encdata, byte[] enckey) throws Exception {
		this.setKey(enckey);
		this.p_Cipher.init(Cipher.DECRYPT_MODE, this.p_Key);
		return this.p_Cipher.doFinal(encdata);
	}

	public String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int i = 0; i < b.length; i++) {
			stmp = Integer.toHexString(b[i] & 0xFF);
			if (stmp.length() == 1) {
				hs += "0" + stmp;
			} else {
				hs += stmp;
			}
		}
		// return hs;
		return hs.toUpperCase();
	}

	public byte[] hex2byte(String hex) throws IllegalArgumentException {
		if (hex.length() % 2 != 0) {
			throw new IllegalArgumentException();
		}
		char[] arr = hex.toCharArray();
		byte[] b = new byte[hex.length() / 2];
		for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
			String swap = "" + arr[i++] + arr[i];
			int byteint = Integer.parseInt(swap, 16) & 0xFF;
			b[j] = new Integer(byteint).byteValue();
		}
		return b;
	}

	// test
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		String info = "12345678901234567890";
		System.out.println("region string:" + info);
		byte[] key;
		DES des = new DES(DES._DESede);
		key = des.getKey();
		System.out.println("encrypted key(byte):" + new String(key));
		String hexkey = des.byte2hex(key);
		System.out.println("encrypted key(hex):" + hexkey);
		byte[] enc = des.encode(info.getBytes());
		System.out.println("encrypted string(byte):" + new String(enc));
		System.out.println("encrypted string length:" + enc.length);

		String hexenc = des.byte2hex(enc);

		System.out.println("encrypted string(hex):" + hexenc);

		byte[] dec = des.decode(enc, des.hex2byte(hexkey));
		System.out.println("decrypted string:" + new String(dec));
		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}

}
