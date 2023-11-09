import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Constrain {
    public static boolean constrain (String dbName, String tbName, String cnName, String data) throws IOException {
        //返回值：true，符合约束，false，不符合约束或程序出错
        //打开model，查看约束表
        //约束数量
        int constrainNum = 5;
        String currentDir = System.getProperty("user.dir");
        String tbInformationPath = currentDir + "//tbInformation" + "//" + dbName + "//" + tbName + "//.xlsx";
        File tbInformationFile = new File(tbInformationPath);

        boolean re = false;
        try (XSSFWorkbook tbInformationWorkbook = new XSSFWorkbook(tbInformationFile)) {
            Sheet modelSheet = tbInformationWorkbook.getSheet("model");
            Row modelRow;
            Cell modelCell;
            //寻找该列的约束
            for (int i = 1; i < modelSheet.getLastRowNum() + 1; i++) {
                modelRow = modelSheet.getRow(i);
                modelCell = modelRow.getCell(0);
                if (Objects.equals(cnName, modelCell.getStringCellValue())) {
                    for (int j = 0; j < constrainNum; j ++) {
                        modelCell = modelRow.getCell(j + 1);
                        if (!(Objects.equals("0", modelCell.getStringCellValue()) ||
                                Objects.equals("null", modelCell.getStringCellValue()))) {
                            switch (j + 1) {
                                case 1: re = typeConstrain(dbName, tbName, cnName, data);break;
                                case 2: re = nullConstrain(dbName, tbName, cnName, data);break;
                                case 3: re = uniqueConstrain(dbName, tbName, cnName, data);break;
                                case 4: re = primaryKeyConstrain(dbName, tbName, cnName, data);break;
                                case 5: re = foreignConstrain(dbName, tbName, cnName, data);break;
                            }
                        }
                    }
                }
            }

            tbInformationWorkbook.close();
            return re;

        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean typeConstrain (String dbName, String tbName, String cnName, String data) {

        return false;
    }

    public static boolean nullConstrain (String dbName, String tbName, String cnName, String data) {

        return false;
    }

    public static boolean uniqueConstrain (String dbName, String tbName, String cnName, String data) {

        return false;
    }

    public static boolean primaryKeyConstrain (String dbName, String tbName, String cnName, String data) {

        return false;
    }

    public static boolean foreignConstrain (String dbName, String tbName, String cnName, String data) {

        return false;
    }
}
