package ru.siberia.LibraryAPI.SecurePassword;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

@PropertySource("classpath:secret.properties")
public class HashPassword {

    private static final int ITERATIONS = 30000;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";

    @Value("${secret.key}")
    private static final String KEY = "92d43ad783746ba7150c5f09de922a786e21860df283de1df07f7ce41892f703913c1e4f535c1319d2d86ad61f49ef7dfe5a634c36ca6faf4ffb7c06bc4fe161";

    public String getHashPassword (String password) {

        char[] chars = password.toCharArray();

        byte[] bytes = KEY.getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);

        Arrays.fill(chars, Character.MIN_VALUE);

        try {
            SecretKeyFactory fac = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] securePassword = fac.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(securePassword);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            System.err.println("Exception encountered in hashPassword()");
            return "";

        } finally {
            spec.clearPassword();
        }
    }

}
