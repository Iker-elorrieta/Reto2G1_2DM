package serverSocket;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


public class CryptoUtils {
    private static final String SECRET = "S3cr3tKeyForDemo"; // 16 chars
    private static final String IV = "RandomInitVector"; // 16 bytes IV

    private static SecretKeySpec getKey() throws Exception {
        byte[] keyBytes = SECRET.getBytes("UTF-8");
        if (!(keyBytes.length == 16 || keyBytes.length == 24 || keyBytes.length == 32)) {
            throw new IllegalStateException("Invalid AES key length: " + keyBytes.length + " bytes. Key must be 16, 24, or 32 bytes.");
        }
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static String encrypt(String value) {
        if (value == null) return null;
        try {
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));
            SecretKeySpec skeySpec = getKey();

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
            return value;
        }
    }

    public static String decrypt(String encrypted) {
        if (encrypted == null) return null;
        try {
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));
            SecretKeySpec skeySpec = getKey();

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception ex) {
            return encrypted;
        }
    }
}
