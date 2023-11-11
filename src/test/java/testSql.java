import java.io.IOException;

public class testSql {
    public void testUser(){
        try {
            SqlFunction.createUser("test", "123");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
