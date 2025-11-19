package com.cifrador.utils;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class RSAUtil {
    public static final String RSA_ALGO = "RSA";
    public static final String RSA_TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

    public static KeyPair generateKeyPair(int bits) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA_ALGO);
        kpg.initialize(bits, SecureRandom.getInstanceStrong());
        return kpg.generateKeyPair();
    }

    public static void savePublicKey(PublicKey pub, File outFile) throws IOException {
        String b64 = Base64.getEncoder().encodeToString(pub.getEncoded());
        try (FileWriter fw = new FileWriter(outFile)) {
            fw.write("-----BEGIN PUBLIC KEY-----\n");
            fw.write(b64);
            fw.write("\n-----END PUBLIC KEY-----\n");
        }
    }

    public static void savePrivateKey(PrivateKey priv, File outFile) throws IOException {
        String b64 = Base64.getEncoder().encodeToString(priv.getEncoded());
        try (FileWriter fw = new FileWriter(outFile)) {
            fw.write("-----BEGIN PRIVATE KEY-----\n");
            fw.write(b64);
            fw.write("\n-----END PRIVATE KEY-----\n");
        }
    }

    public static PublicKey loadPublicKey(File f) throws Exception {
        String content = new String(Files.readAllBytes(f.toPath())).replaceAll("-----.*KEY-----", "").replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(content);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance(RSA_ALGO);
        return kf.generatePublic(spec);
    }

    public static PrivateKey loadPrivateKey(File f) throws Exception {
        String content = new String(Files.readAllBytes(f.toPath())).replaceAll("-----.*KEY-----", "").replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(content);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance(RSA_ALGO);
        return kf.generatePrivate(spec);
    }

    public static byte[] encryptWithPublicKey(byte[] data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
        // Usamos la transformación que incluye OAEP SHA-256; no es necesario crear OAEPParameterSpec manualmente
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    public static byte[] decryptWithPrivateKey(byte[] data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }
}
