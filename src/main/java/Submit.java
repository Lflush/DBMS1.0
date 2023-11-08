import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.Sheet;

public class Submit {
    public static String submit() {
        //返回值：用户名，登录成功；null，登录失败
        //接收用户、密码
        System.out.print("请输入用户名：");
        Scanner scanner = new Scanner(System.in);
        String user = scanner.nextLine();

        System.out.print("请输入密码：");
        String password = scanner.nextLine();

        //检查用户、密码
        String currentDir = System.getProperty("user.dir");
        String dataPath = currentDir + "\\data";
        String systemPath = dataPath + "\\system.xlsx";
        try (FileInputStream systemFile = new FileInputStream(systemPath)) {
            try (XSSFWorkbook systemWorkbook = new XSSFWorkbook(systemFile)) {
                Sheet userSheet = systemWorkbook.getSheet("user");
                Row userRow;
                Cell userCell;
                for (int i = 1; i < userSheet.getLastRowNum() + 1; i++) {
                    userRow = userSheet.getRow(i);
                    userCell = userRow.getCell(0);
                    if (Objects.equals(user, userCell.getStringCellValue())) {
                        userCell = userRow.getCell(1);
                        if (Objects.equals(password, userCell.getStringCellValue())) {
                            System.out.println("登录成功！");
                            return user;
                        } else {
                            System.out.println("密码错误！");
                            return null;
                        }
                    }
                }
                systemWorkbook.close();
                System.out.println("用户不存在！");
                return null;
            }
        }
        catch (IOException e) {
            e.fillInStackTrace();
            System.out.println("打开system时出现错误！");
            return null;
        }
    }
}
