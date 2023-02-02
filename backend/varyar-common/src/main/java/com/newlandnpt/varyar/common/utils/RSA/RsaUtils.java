package com.newlandnpt.varyar.common.utils.RSA;

import io.lettuce.core.dynamic.annotation.Value;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.MapUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.InputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Properties;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RsaUtils {
    // Rsa 私钥 也可固定秘钥对 若依原写法(不安全)
    public static String privateKeys = "";
    private static String publicKeyStr = "";
    private static String privateKeyStr = "";
    private static final RsaKeyPair rsaKeyPair = new RsaKeyPair();
    private static final Logger logger = LoggerFactory.getLogger(MapUtils.class);

    /**
     * 私钥解密
     *
     * @param text 待解密的文本
     * @return 解密后的文本
     */
    public static String decryptByPrivateKey(String text) throws Exception {
//        logger.info(rsaKeyPair.getPrivateKey());
        return decryptByPrivateKey(rsaKeyPair.getPrivateKey(), text);
    }

    /**
     * 公钥解密
     *
     * @param publicKeyString 公钥
     * @param text            待解密的信息
     * @return 解密后的文本
     */
    public static String decryptByPublicKey(String publicKeyString, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 私钥加密
     *
     * @param privateKeyString 私钥
     * @param text             待加密的信息
     * @return 加密后的文本
     */
    public static String encryptByPrivateKey(String privateKeyString, String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 私钥解密
     *
     * @param privateKeyString 私钥
     * @param text             待解密的文本
     * @return 解密后的文本
     */
    public static String decryptByPrivateKey(String privateKeyString, String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 公钥加密
     *
     * @param publicKeyString 公钥
     * @param text            待加密的文本
     * @return 加密后的文本
     */
    public static String encryptByPublicKey(String publicKeyString, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 构建RSA密钥对
     *
     * @return 生成后的公私钥信息
     */
    @Bean
    public void generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        String publicKeyString = Base64.encodeBase64String(rsaPublicKey.getEncoded());
        String privateKeyString = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
        rsaKeyPair.setPrivateKey(privateKeyString);
        rsaKeyPair.setPublicKey(publicKeyString);
        publicKeyStr = publicKeyString;
        privateKeyStr = privateKeyString;
    }


    public static String getPublicKey() {
        return publicKeyStr;
    }

    public static String getPrivateKey() {
        return privateKeyStr;
    }

    public static RsaKeyPair rsaKeyPair() {
        return rsaKeyPair;
    }

    /**
     * RSA密钥对对象
     */
    public static class RsaKeyPair {
        private String publicKey;
        private String privateKey;

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

        public RsaKeyPair() {

        }

        public RsaKeyPair(String publicKey, String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }
    }


    public static void main(String[] args) throws Exception {
       /* @Value("${base.publicKey}")
        public  String publicKey;
        @Value("${base.privateKey}")
        public  String privateKey;*/

       //生成公钥和私钥
       /* RsaUtils.RsaKeyPair rsaKeyPair = new RsaUtils.RsaKeyPair();
        rsaKeyPair.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDF4xuwZNWhVaQZguqxLBKhc2S1jKiJOzJS0YczxLPTH9l2Fa+zxZ/AzYci95KG9kFbvZEVyuFhR5UTsORc+ONsqTg/zx2+vJ7kI8h2/qtWNV6vH9JmgMVHl5CEpikXhZjOPutNmzcRKfzDbYwp274f6iDUi9GttlYR68qPBij8AQIDAQAB");
        rsaKeyPair.setPrivateKey("MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMXjG7Bk1aFVpBmC6rEsEqFzZLWMqIk7MlLRhzPEs9Mf2XYVr7PFn8DNhyL3kob2QVu9kRXK4WFHlROw5Fz442ypOD/PHb68nuQjyHb+q1Y1Xq8f0maAxUeXkISmKReFmM4+602bNxEp/MNtjCnbvh/qINSL0a22VhHryo8GKPwBAgMBAAECgYEArGQGKiwd45fdBpCDbmdrr5T8ZiCsbijQ1U9Njm7NDZ7QbkvELkN6wZcLJA4zazm6hHZnn2mH4Jxgpa/TsZ7tV6KocRq4W/Txxf8c/aLW7X/vm9MyWgXk90mcKM2+hJHj7hnon3KQwpLJMqCJBZXhPhXOoHAaulOPBPpamryxqBUCQQDrECPMzgos00/uL+BVHHmRN/H4X19UStNmcd+ZYVWQJ3OrONYncU++N7qovW3omVflCOSAqghBIBxu6HMR2rLfAkEA14NKc+HdR1vczxNwm66rUsJE2N0NumYQkNOpsz59gERXAQVIakdzvBhyVbBWUoWRFTEpvmcrADzGzLZSyWgNHwJBAIjgYxhVOhOUEM0vz79/z0JCg4IPdyFMWQEkegGEbiB8+JvRgtKA5uBq7yRm+eMht87EeNp7KEdT/XqPnMHeyZ0CQA3e21nd/ZnsJagGPkcm1Mv8yOWURJ/slcBcfx2UNgudWmQsbN66Po6qnCE7ujKReib9BQzGIxRe1QSz71wbIE0CQQDRU/TyLk21xJ59dDVudXEt5EtPBNAAeqWY+I0lnzxAqkeylpK8D2BxFxytLH1p/+XIg8e6CNgz/zKumu1VLvbe");
        //公钥加密
        String enPassWord = RsaUtils.encryptByPublicKey(rsaKeyPair.getPublicKey(),"1111");
        System.out.println(enPassWord);

        //私钥解密
        String unenPassWord = RsaUtils.decryptByPrivateKey(rsaKeyPair.getPrivateKey(),enPassWord);
        System.out.println(unenPassWord);*/

       Long i = 46860000L;

       long s = i/60;

        System.out.println(s);

    }
}
