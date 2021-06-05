package encryption;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Classe per la gestione della crittazione/decrittazione di dati tramite chiave.
 * @author Gabriele Mattioli
 * @version 1.0
 */
public class Encrypter {
    //attributi
    private SecretKey key;
    private Cipher cipher;

    //costruttori

    /** Costruttore, inizializza l'Encrypter. */
    public Encrypter() {
        try {
            this.cipher = Cipher.getInstance("AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //metodi

    /** Inizializza il Cipher in encrypt mode. */
    public void turnOnEncrypter() throws NoSuchAlgorithmException, InvalidKeyException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        this.key = keyGen.generateKey();
        cipher.init(Cipher.ENCRYPT_MODE, this.key);
    }

    /** Inizializza il Cipher in decrypt mode. */
    public void turnOnDecrypter(byte[] key, int length) throws InvalidAlgorithmParameterException, InvalidKeyException {
        this.key = new SecretKeySpec(key, 0,length,"AES");
        cipher.init(Cipher.DECRYPT_MODE, this.key, cipher.getParameters());
    }

    /** @return Byte criptati o decriptati a seconda della modalità attiva. */
    public byte[] update(byte[] bytes) {
        return cipher.update(bytes);
    }

    /** @return Byte del padding finale. */
    public byte[] doFinal() throws BadPaddingException, IllegalBlockSizeException {
            return cipher.doFinal();

    }

    //getter

    /** @return Chiave di crittazione in byte. */
    public byte[] getKey() {
        return key.getEncoded();
    }
}
