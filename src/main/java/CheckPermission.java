import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class CheckPermission {
    public static boolean checkPermission (String operate, String tableName, String user) {
        //检查用户、密码
        String currentDir = System.getProperty("user.dir");
        String systemPath = currentDir + "\\data\\system.xlsx";
        try (FileInputStream systemFile = new FileInputStream(systemPath)) {
            try (XSSFWorkbook systemWorkbook = new XSSFWorkbook(systemFile)) {
                Sheet permissionSheet = systemWorkbook.getSheet("permission");
                Row permissionRow;
                Cell permissionCell;

                for (int i = 0; i < permissionSheet.getLastRowNum() + 1; i++) {
                    permissionRow = permissionSheet.getRow(i);
                    permissionCell = permissionRow.getCell(0);
                    if (Objects.equals(tableName, permissionCell.getStringCellValue())) {
                        permissionRow = permissionSheet.getRow(0);
                        for (int j = 1; j < permissionRow.getLastCellNum() + 1; j++) {
                            permissionCell = permissionRow.getCell(j);
                            if (Objects.equals(operate, permissionCell.getStringCellValue())) {
                                permissionRow = permissionSheet.getRow(i);
                                permissionCell = permissionRow.getCell(j);
                                String[] permissionUser = permissionCell.getStringCellValue().split(",");

                                return Arrays.asList(permissionUser).contains(user);
                            }
                        }

                    }
                }
            }
        }
        catch (IOException e) {
            e.fillInStackTrace();
            System.out.println("打开system时出现错误！");
            return false;
        }
        return false;
    }
}
