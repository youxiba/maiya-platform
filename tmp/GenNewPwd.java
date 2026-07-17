import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class GenNewPwd {
    public static void main(String[] a) {
        System.out.print(new BCryptPasswordEncoder().encode(a[0] + "mkb"));
    }
}
