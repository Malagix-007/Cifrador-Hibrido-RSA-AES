package com.cifrador.utils;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.io.*;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil {
    public static final String AES_ALGO = "AES";
    public static final String AES_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int AES_KEY_SIZE = 256; // bits
    private static final int GCM_IV_LENGTH = 12; // bytes
    private static final int GCM_TAG_LENGTH = 128; // bits

    public static SecretKey generateKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(AES_ALGO);
        kg.init(AES_KEY_SIZE, SecureRandom.getInstanceStrong());
        return kg.generateKey();
    }

    public static byte[] generateIV() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public static void encryptFile(File inFile, File outFile, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        try (FileOutputStream fos = new FileOutputStream(outFile);
             CipherOutputStream cos = new CipherOutputStream(fos, cipher);
             FileInputStream fis = new FileInputStream(inFile)) {
            fos.write(iv); // escribe IV al inicio
            byte[] buffer = new byte[4096];
            int r;
            while ((r = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, r);
            }
        }
    }

    public static void decryptFile(File inFile, File outFile, SecretKey key) throws Exception {
        try (FileInputStream fis = new FileInputStream(inFile)) {
            byte[] iv = new byte[GCM_IV_LENGTH];
            int read = fis.read(iv);
            if (read != GCM_IV_LENGTH) throw new IOException("IV no disponible o archivo corrupto");

            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            try (CipherInputStream cis = new CipherInputStream(fis, cipher);
                 FileOutputStream fos = new FileOutputStream(outFile)) {
                byte[] buffer = new byte[4096];
                int r;
                while ((r = cis.read(buffer)) != -1) {
                    fos.write(buffer, 0, r);
                }
            }
        }
    }

    public static String keyToBase64(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static SecretKey keyFromBase64(String b64) {
        byte[] decoded = Base64.getDecoder().decode(b64);
        return new javax.crypto.spec.SecretKeySpec(decoded, AES_ALGO);
    }
}
