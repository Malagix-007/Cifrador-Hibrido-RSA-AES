package com.cifrador.encryptor;

import com.cifrador.utils.AESUtil;
import com.cifrador.utils.RSAUtil;

import javax.crypto.SecretKey;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HybridCipher {

    public static String encryptFile(File inFile, File outEncFile, File outEncKeyFile, java.security.PublicKey publicKey) throws Exception {
        SecretKey aesKey = AESUtil.generateKey();
        byte[] iv = AESUtil.generateIV();
        AESUtil.encryptFile(inFile, outEncFile, aesKey, iv);

        byte[] encryptedAesKey = RSAUtil.encryptWithPublicKey(aesKey.getEncoded(), publicKey);
        String encryptedAesKeyB64 = Base64.getEncoder().encodeToString(encryptedAesKey);

        java.nio.file.Files.write(outEncKeyFile.toPath(), encryptedAesKeyB64.getBytes(StandardCharsets.UTF_8));

        return encryptedAesKeyB64;
    }

    public static void decryptFile(File inEncFile, File inEncKeyFile, java.security.PrivateKey privateKey, File outFile) throws Exception {
        String b64 = new String(java.nio.file.Files.readAllBytes(inEncKeyFile.toPath()), StandardCharsets.UTF_8);
        byte[] encryptedAesKey = Base64.getDecoder().decode(b64);
        byte[] aesKeyBytes = RSAUtil.decryptWithPrivateKey(encryptedAesKey, privateKey);
        javax.crypto.SecretKey aesKey = new javax.crypto.spec.SecretKeySpec(aesKeyBytes, "AES");
        AESUtil.decryptFile(inEncFile, outFile, aesKey);
    }
}
