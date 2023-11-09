import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

public class Permission {
    public  static boolean permission(String userName,String dbName, String tbName, String operate) {
        //返回值：true，有权限，false，无权限或程序出错
        //打开user，查看权限表
        String currentDir = System.getProperty("user.dir");
        String userPath = currentDir + "\\sys\\" + userName + ".xlsx";
        File userFile = new File(userPath);

        try (XSSFWorkbook userWorkbook = new XSSFWorkbook(userFile)) {
            Sheet dbPermissionSheet = userWorkbook.getSheet(dbName);
            Row dbPermissionRow;
            Cell dbPermissionCell;
            Map<String, Integer> operateMap = new HashMap<>();
            operateMap.put("select", 1);
            operateMap.put("insert", 2);
            operateMap.put("update", 3);
            operateMap.put("delete", 4);
            operateMap.put("create", 5);
            operateMap.put("drop", 6);
            operateMap.put("alter", 7);
            operateMap.put("all privileges", 8);

            boolean re = false;
            //找到该表
            for (int i = 1; i < dbPermissionSheet.getLastRowNum() + 1; i++) {
                dbPermissionRow = dbPermissionSheet.getRow(i);
                dbPermissionCell = dbPermissionRow.getCell(0);

                if (Objects.equals(tbName, dbPermissionCell.getStringCellValue())) {
                    int operateCode = operateMap.get(operate);
                    dbPermissionCell = dbPermissionRow.getCell(operateCode);
                    re = Objects.equals("1", dbPermissionCell.getStringCellValue());
                }
            }

            userWorkbook.close();
            return re;
        } catch (InvalidFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
