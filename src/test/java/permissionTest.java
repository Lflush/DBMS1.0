import java.util.Objects;

public class permissionTest {
    public static void main(String[] args) {
        //String userName,String dbName, String tbName, String operate
        boolean re1 = Permission.permission("admin", "exampleDb", "exampleTb", "select");
        boolean re2 = Permission.permission("admin", "exampleDb", "exampleTb", "all privileges");

        System.out.println(re1+ " " + re2);
    }
}
