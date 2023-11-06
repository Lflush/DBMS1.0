import java.util.Objects;
import java.util.Scanner;

public class run {
    public static void main(String[] args) {
        int re;
        String user = null;
        //初始化
        re = Initialize.initialize();

        if (re == 0) {
            //登录
            do {
                user = Submit.submit();
            } while (user == null);

            String sql = null;
            do {
                Scanner scanner = new Scanner(System.in);
                sql = scanner.nextLine();

                if (Objects.equals(sql, "exit")) {
                    break;
                }
                else {
                    Interpreter.query(sql, user);
                }
            } while (sql.equals("exit"));
        }
        else {
            return;
        }
    }
}
