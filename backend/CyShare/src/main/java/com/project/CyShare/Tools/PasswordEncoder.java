package com.project.CyShare.Tools;

import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * This Class helps to Encode and Decode the password according to the user generated key.
 */
public class PasswordEncoder {
    /**
     * Format of the Stored Password in the Datavase
     */
    private static final String UNICODE_FORMAT = "UTF8";
    /**
     * Encryption Scheme for the Passoword, can be changed according to the client
     */
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    /**
     * This variable acts as a CryptoGraphic Key
     */
    private KeySpec ks;
    /**
     * Key factory to generate the user Desired key
     */
    private SecretKeyFactory skf;
    /**
     * The algorithm that Encrypts the key
     */
    private Cipher cipher;
    /**
     * An array to store the Bytes
     */
    byte[] arrayBytes;
    /**
     * The Encryption key that acts as a salt to generate a password
     */
    private String myEncryptionKey;
    /**
     * Variable to represent the Scheme.
     */
    private String myEncryptionScheme;
    /**
     * Key that is generated after applying the Salt and encryption
     */
    SecretKey key;

    /**
     * The Method that sets up the Encryption Scheme for the Password and accepts the salt
     * which generates the Encryption key
     * @throws Exception
     */
    public PasswordEncoder() throws Exception {
        myEncryptionKey = "JoshBhuwanHugo###@&&2021";
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        ks = new DESedeKeySpec(arrayBytes);
        skf = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = skf.generateSecret(ks);
    }

    /**
     * The methods converts a normal String to an Encoded String
     * @param unencryptedString That is given by the user
     * @return Encrypted key in UTF-8 Form
     */

    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = new String(Base64.encodeBase64(encryptedText));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }

    /**
     * The Decodes an encrypted String using the salt provided by the Admin
     * @param encryptedString in the form of UTF-8
     * @return Decrypted String
     */

    public String decrypt(String encryptedString) {
        String decryptedText=null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decodeBase64(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText= new String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }





}

