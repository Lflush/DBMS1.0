import java.io.IOException;

public class testSql {
    public static void main(String[] args) {
        try {
            testShow();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    private static void testShow() throws IOException {
        SqlFunction.showGrants("root");
        SqlFunction.showDataBases();
        SqlFunction.showTables();
        SqlFunction.showUser();
        SqlFunction.showdatabase();
        SqlFunction.showTableModel("exampleTb");
    }


    
}
