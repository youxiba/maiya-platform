import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class GenPwd {
    public static void main(String[] a) {
        System.out.print(new BCryptPasswordEncoder().encode("adminmkb"));
        System.out.print("|");
        System.out.print(new BCryptPasswordEncoder().encode("admin123mkb"));
    }
}
