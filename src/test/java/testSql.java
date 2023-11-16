import java.io.IOException;

public class testSql {
    public static void main(String[] args) {
        try {
            SqlFunction.showGrants("root");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
