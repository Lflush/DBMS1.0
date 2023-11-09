import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.Sheet;

public class Submit {
    //将userName和password与sys文件夹下的users工作簿中的up表进行对比
    public static String submit() {
        //返回值：用户名，登录成功；null，登录失败
        //接收用户、密码
        System.out.print("请输入用户名：");
        Scanner scanner = new Scanner(System.in);
        String userName = scanner.nextLine();

        System.out.print("请输入密码：");
        String password = scanner.nextLine();

        //检查用户、密码
        String currentDir = System.getProperty("user.dir");
        String sysPath = currentDir + "\\sys";
        String usersPath = sysPath + "\\users.xlsx";
        try (FileInputStream usersFile = new FileInputStream(usersPath)) {
            try (XSSFWorkbook usersWorkbook = new XSSFWorkbook(usersFile)) {
                Sheet upSheet = usersWorkbook.getSheet("up");
                Row upRow;
                Cell upCell;
                for (int i = 1; i < upSheet.getLastRowNum() + 1; i++) {
                    upRow = upSheet.getRow(i);
                    upCell = upRow.getCell(0);
                    if (Objects.equals(userName, upCell.getStringCellValue())) {
                        upCell = upRow.getCell(1);
                        if (Objects.equals(password, upCell.getStringCellValue())) {
                            System.out.println("登录成功！");
                            return userName;
                        } else {
                            System.out.println("密码错误！");
                            return null;
                        }
                    }
                }
                usersWorkbook.close();
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
