import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class passwordEncoder {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "123"; // 原始密码
        String encryptedPassword = encoder.encode(rawPassword);
        System.out.println("加密后的密码: " + encryptedPassword);
    }
}
