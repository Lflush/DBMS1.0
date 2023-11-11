import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Constrain {
    public static <E> boolean constrain (String dbName, String tbName, String cnName, String dataType, E data) {
        //返回值：true，符合约束，false，不符合约束或程序出错
        //打开model，查看约束表
        //约束数量
        int constrainNum = 5;
        String currentDir = System.getProperty("user.dir");
        String tbInformationPath = currentDir + "\\tbInformation\\" + dbName + "\\" + tbName + ".xlsx";
        File tbInformationFile = new File(tbInformationPath);

        boolean re = false;
        boolean re1 = true;
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
                                case 1: {
                                    String constrainType = modelCell.getStringCellValue();
                                    re = typeConstrain(constrainType, dataType);

                                    //如果有一个条件不满足则直接返回false
                                    if (!re) {
                                        tbInformationWorkbook.close();
                                        System.out.println("不符合类型约束");
                                        return re;
                                    }
                                }break;

                                case 2: {
                                    re = nullConstrain(data);

                                    if (!re) {
                                        System.out.println("不符合null约束");
                                        tbInformationWorkbook.close();
                                        return re;
                                    }
                                }break;

                                case 3: {
                                    modelCell = modelRow.getCell(1);
                                    String constrainType = modelCell.getStringCellValue();
                                    re = uniqueConstrain(dbName, tbName, cnName, data, constrainType);

                                    if (!re) {
                                        System.out.println("不符合unique约束");
                                        tbInformationWorkbook.close();
                                        return re;
                                    }
                                }break;

                                case 4: {
                                    modelCell = modelRow.getCell(1);
                                        String constrainType = modelCell.getStringCellValue();
                                        re = primaryKeyConstrain(dbName, tbName, cnName, data, constrainType);

                                    if (!re) {
                                        System.out.println("不符合primaryKey约束");
                                        tbInformationWorkbook.close();
                                        return re;
                                    }
                                    }break;

                                case 5: {
                                    modelCell = modelRow.getCell(1);
                                    String constrainType = modelCell.getStringCellValue();
                                    modelCell = modelRow.getCell(5);
                                    String foreignKey = modelCell.getStringCellValue();
                                    re = foreignKeyConstrain(dbName, data, constrainType, foreignKey);

                                    if (!re) {
                                        System.out.println("不符合foreignKey约束");
                                        tbInformationWorkbook.close();
                                        return re;
                                    }
                                }break;
                            }
                        }
                    }
                }
            }

            //到此说明约束全部通过
            tbInformationWorkbook.close();
            return true;

        } catch (InvalidFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean typeConstrain (String constrainType, String type) {
        //constrainType由命令得出
       return Objects.equals(constrainType, type);
    }

    public static <E> boolean nullConstrain (E data) {
        //若数据为空，则将数据定义为null，据此判断；
        return data != null;
    }

    public static <E> boolean uniqueConstrain (String dbName, String tbName, String cnName, E data, String constrainType) {
        String currentDir = System.getProperty("user.dir");
        String dbPath = currentDir + "\\data\\" + dbName + ".xlsx";
        File dbFile = new File(dbPath);

        try (XSSFWorkbook dbWorkbook = new XSSFWorkbook(dbFile)) {
            Sheet tbSheet = dbWorkbook.getSheet(tbName);
            Row tbRow;
            Cell tbCell;

            int cnNum = 0;
            tbRow = tbSheet.getRow(0);
            for (int i = 0; i < tbRow.getLastCellNum() + 1; i ++) {
                tbCell = tbRow.getCell(i);
                if (Objects.equals(cnName, tbCell.getStringCellValue())) {
                    cnNum = i;
                    break;
                }
            }

            dbWorkbook.close();
            return getCnList(data, constrainType, tbSheet, cnNum);
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }


    public static <E> boolean primaryKeyConstrain (String dbName, String tbName, String cnName, E data, String constrainType) {
        return uniqueConstrain(dbName, tbName, cnName, data, constrainType) && nullConstrain(data);
    }

    public static <E> boolean foreignKeyConstrain (String dbName, E data, String constrainType, String foreignKey) {
        if (data != null) {
            String currentDir = System.getProperty("user.dir");
            String[] foreignKeyList = foreignKey.split(":");
            String dbPath = currentDir + "\\data\\" + dbName + ".xlsx";
            File dbFile = new File(dbPath);

            try (XSSFWorkbook dbWorkbook = new XSSFWorkbook(dbFile)) {
                Sheet tbSheet = dbWorkbook.getSheet(foreignKeyList[0]);
                Row tbRow;
                Cell tbCell;

                int cnNum = 0;
                tbRow = tbSheet.getRow(0);
                for (int i = 0; i < tbRow.getLastCellNum() + 1; i++) {
                    tbCell = tbRow.getCell(i);
                    if (Objects.equals(foreignKeyList[1], tbCell.getStringCellValue())) {
                        cnNum = i;
                        break;
                    }
                }

                boolean re = getCnList(data, constrainType, tbSheet, cnNum);
                dbWorkbook.close();
                return re;

            } catch (IOException | InvalidFormatException e) {
                throw new RuntimeException(e);
            }
        } else {
            return true;
        }
    }

    private static <E> boolean getCnList(E data, String constrainType, Sheet tbSheet, int cnNum) {
        Row tbRow;
        Cell tbCell;
        if (Objects.equals(constrainType, "char")) {
            if (tbSheet.getLastRowNum() > 1) {
                List<String> dataList = new ArrayList<>();
                for (int j = 1; j < tbSheet.getLastRowNum() + 1; j++) {
                    tbRow = tbSheet.getRow(j);
                    tbCell = tbRow.getCell(cnNum);
                    dataList.add(tbCell.getStringCellValue());
                }

                return !dataList.contains(data.toString());
            }
            else {
                return true;
            }

        } else if (Objects.equals(constrainType, "int")) {
            if (tbSheet.getLastRowNum() > 1) {
                List<Integer> dataList = new ArrayList<>();
                for (int j = 1; j < tbSheet.getLastRowNum() + 1; j++) {
                    tbRow = tbSheet.getRow(j);
                    tbCell = tbRow.getCell(cnNum);
                    dataList.add((int)tbCell.getNumericCellValue());
                }

                return !dataList.contains((Integer) data);
            } else {
                return true;
            }
        } else {
            if (tbSheet.getLastRowNum() > 1) {
                List<Double> dataList = new ArrayList<>();
                for (int j = 1; j < tbSheet.getLastRowNum() + 1; j++) {
                    tbRow = tbSheet.getRow(j);
                    tbCell = tbRow.getCell(cnNum);
                    dataList.add(tbCell.getNumericCellValue());
                }

                return !dataList.contains((Double) data);
            } else {
                return true;
            }
        }
    }
}
