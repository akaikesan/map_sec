package com.websarva.wings.android.mapsecond;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {
    private static final String LOG_TAG = "Encryptor";

    /** 鍵長（byte単位） */
    private static final int KEY_LENGTH_BYTES = 16;

    /** 鍵長（bit単位） */
    private static final int KEY_LENGTH_BITS = KEY_LENGTH_BYTES * 8;

    /**
     * ランダムキーを生成する
     */
    public static Key generateKey() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            generator.init(KEY_LENGTH_BITS, random);
            return generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * バイト配列を鍵に変換
     */
    public static Key getKey(byte[] bytes) {
        byte[] b = new byte[KEY_LENGTH_BYTES];
        for (int i = 0; i < KEY_LENGTH_BYTES; i++) {
            b[i] = i < bytes.length ? bytes[i] : 0; // シンプル：あいたところは0で埋める
        }
        return new SecretKeySpec(b, "AES");
    }

    public static byte[] encrypt(byte[] src, byte[] key) throws GeneralSecurityException {
        return encrypt(src, getKey(key));
    }

    public static byte[] decrypt(byte[] src, byte[] key) throws GeneralSecurityException {
        return decrypt(src, getKey(key));
    }

    public static byte[] encrypt(byte[] src, Key skey) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skey);
        return cipher.doFinal(src);
    }

    public static byte[] decrypt(byte[] src, Key skey) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skey);
        return cipher.doFinal(src);
    }
}