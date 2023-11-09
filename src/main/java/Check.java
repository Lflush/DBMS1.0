import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Check {
    public static boolean check (String dbName, String tbName, String cnName, String data) throws IOException {
        //返回值：true，符合约束，false，不符合约束或程序出错
        //打开check，查看约束表
        //约束数量
        String currentDir = System.getProperty("user.dir");
        String tbInformationPath = currentDir + "//tbInformation" + "//" + dbName + "//" + tbName + "//.xlsx";
        File tbInformationFile = new File(tbInformationPath);

        boolean re = false;
        try (XSSFWorkbook tbInformationWorkbook = new XSSFWorkbook(tbInformationFile)) {
            Sheet checkSheet = tbInformationWorkbook.getSheet("check");
            Row checkRow;
            Cell checkCell;
            String ckValue = "";
            //寻找该列的约束
            for (int i = 1; i < checkSheet.getLastRowNum() + 1; i++) {
                checkRow = checkSheet.getRow(i);
                checkCell = checkRow.getCell(0);
                if (Objects.equals(cnName, checkCell.getStringCellValue())) {
                    for (int j = 0; j < checkRow.getLastCellNum() + 1; j ++) {
                        checkCell = checkRow.getCell(j + 1);
                        ckValue = checkCell.getStringCellValue();
                        re = checkRe(dbName, tbName, cnName, data, ckValue);
                    }
                }
            }

            tbInformationWorkbook.close();
            return re;

        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkRe (String dbName, String tbName, String cnName, String data, String ckValue) {

        return false;
    }
}
