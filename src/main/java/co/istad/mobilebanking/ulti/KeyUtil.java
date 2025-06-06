package co.istad.mobilebanking.ulti;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Component
public class KeyUtil {

    private Environment environment;

    @Value("${access-token.private}")
    private String accessTokenPrivateKeyPart;

    @Value("${access-token.public}")
    private String accessTokenPublicKeyPart;

    @Value("${refresh-token.private}")
    public String refreshTokenPrivateKeyPath;

    @Value("${refresh-token.public}")
    private String refreshTokenPublicKeyPath;

    private KeyPair accessTokenKeyPair;

    private KeyPair refreshTokenKeyPair;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public KeyPair getAccessTokenKeyPair() {
        if (Objects.isNull(accessTokenKeyPair)) {
            accessTokenKeyPair = getKeyPair(accessTokenPublicKeyPart, accessTokenPrivateKeyPart);
        }
        return accessTokenKeyPair;
    }

    public KeyPair getRefreshTokenKeyPair() {
        if (Objects.isNull(refreshTokenKeyPair)) {
            refreshTokenKeyPair = getKeyPair(refreshTokenPublicKeyPath, refreshTokenPrivateKeyPath);
        }
        return refreshTokenKeyPair;
    }

    private KeyPair getKeyPair(String publicKeyPart, String privateKeyPart){

        KeyPair keyPair;

        File publicKeyFile = new File(publicKeyPart);
        File privateKeyFile = new File(privateKeyPart);

        if (publicKeyFile.exists() && privateKeyFile.exists()) {
            try {
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
                EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
                PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

                byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

                keyPair = new KeyPair(publicKey, privateKey);

                return keyPair;

            }catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e){
                throw new RuntimeException(e);
            }
        } else {
            if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
                throw new RuntimeException("Public and private keys not available");
            }

            File directory = new File("access-refresh-token-keys");
            System.out.println(directory.exists());
            System.out.println(directory.getAbsolutePath());
            if (!directory.exists()) {
                directory.mkdirs();
            }

            try {

                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                keyPair = keyPairGenerator.generateKeyPair();

                try (FileOutputStream fos = new FileOutputStream(publicKeyPart)) {
                    log.info("FOS: {}", fos);
                    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
                    fos.write(keySpec.getEncoded());
                }

                try (FileOutputStream fos = new FileOutputStream(privateKeyPart)) {
                    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
                    fos.write(keySpec.getEncoded());
                }

            }catch (NoSuchAlgorithmException | IOException e){
                throw new RuntimeException(e);
            }

            return keyPair;
        }
    }
    public RSAPublicKey getAccessTokenPublicKey() {
        return (RSAPublicKey) getAccessTokenKeyPair().getPublic();
    }

    public RSAPrivateKey getAccessTokenPrivateKey() {
        return (RSAPrivateKey) getAccessTokenKeyPair().getPrivate();
    }

    public RSAPublicKey getRefreshTokenPublicKey() {
        return (RSAPublicKey) getRefreshTokenKeyPair().getPublic();
    }

    public RSAPrivateKey getRefreshTokenPrivateKey() {
        return (RSAPrivateKey) getRefreshTokenKeyPair().getPrivate();
    }

}
