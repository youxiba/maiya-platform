import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class TestBc {
    public static void main(String[] a) {
        String hash = "$2a$10$Yi663lFtFnOWHozMCc8NWO9b7mPBh5/uaba92nRKKZMeDfLDXRMzG";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("adminmkb: " + encoder.matches("adminmkb", hash));
        System.out.println("admin123mkb: " + encoder.matches("admin123mkb", hash));
    }
}
