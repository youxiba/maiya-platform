import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class GenPwd2 {
    public static void main(String[] a) {
        System.out.print(new BCryptPasswordEncoder().encode("adminmkb"));
    }
}
