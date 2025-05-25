package top.sboxm.auth.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * 二维码工具类
 */
public class QrCodeUtils {

    /**
     * 生成唯一的二维码内容
     * @return 二维码内容（token）
     */
    public static String generateQrCodeContent() {
        return generateSHA256Hash(UUID.randomUUID().toString());
    }
    
    /**
     * 生成SHA256哈希值
     * @param input 输入字符串
     * @return SHA256哈希值
     */
    public static String generateSHA256Hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("无法生成SHA-256哈希值", e);
        }
    }
}