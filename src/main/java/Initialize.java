import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Initialize {
    //创建后续要使用的目录结构以及admin等基本信息
    public static int initialize() {
        //返回值：0，一切正常；1，初始化失败
        String currentDir = System.getProperty("user.dir");

        String sysPath = currentDir + "\\sys";
        File sys = new File(sysPath);
        String usersPath = sysPath + "\\user.xlsx";
        File users = new File(usersPath);

        String tbInformationPath = currentDir + "\\tbInformation";
        File tbInformation = new File(tbInformationPath);
        String exampleDbPath = tbInformationPath + "\\exampleDb";
        File exampleDb = new File(exampleDbPath);
        String exampleTbPath = tbInformationPath + "\\exampleTb.xlsx";
        File exampleTb = new File(exampleTbPath);

        String dataPath = currentDir + "\\data";
        File data = new File(dataPath);
        String exampleDbDataPath = dataPath + "\\exampleDb";
        File exampleDbData = new File(exampleDbDataPath);
        String exampleTbDataPath = dataPath + "\\exampleTb.xlsx";
        File exampleTbData = new File(exampleTbDataPath);

        //sys初始化
        if (sys.exists()) {
            System.out.println("system已存在！");
        } else {
            System.out.println("sys创建成功！");

            //创建sys文件夹
            if (sys.mkdir()) {
                //创建users工作簿
                try (XSSFWorkbook usersWorkbook = new XSSFWorkbook()) {
                    try (FileOutputStream outputStream = new FileOutputStream(usersPath)) {
                        System.out.println("users创建成功！");

                        //设置up表，用于存储用户名和密码
                        Sheet upSheet = usersWorkbook.createSheet("up");
                        Row upRow = upSheet.createRow(0);
                        Cell upCell = upRow.createCell(0);
                        upCell.setCellValue("userName");
                        upCell = upRow.createCell(1);
                        upCell.setCellValue("password");

                        //设置admin用户
                        upRow = upSheet.createRow(1);
                        upCell = upRow.createCell(0);
                        upCell.setCellValue("admin");
                        upCell = upRow.createCell(1);
                        upCell.setCellValue("admin");

                        //设置root用户
                        upRow = upSheet.createRow(2);
                        upCell = upRow.createCell(0);
                        upCell.setCellValue("root");
                        upCell = upRow.createCell(1);
                        upCell.setCellValue("root");

                        //每个用户都开一个表，表中存储该用户的操作权限
                        String[] userList = {"admin", "root"};
                        String[] permissionList = {"table", "select", "insert", "update", "delete", "create", "drop", "alter", "all privileges"};
                        Map<String, String[]> permissionMap = new HashMap<>();
                        String[] tbList = {"exampleTb"};
                        permissionMap.put("exampleTb", new String[]{"1", "1", "1", "1", "1", "1", "0", "0"});
                        Sheet userSheet;
                        Row userRow;
                        Cell userCell;
                        for (String s : userList) {
                            userSheet = usersWorkbook.createSheet(s);
                            userRow = userSheet.createRow(0);
                            for (int j = 0; j < permissionList.length; j++) {
                                userCell = userRow.createCell(j);
                                userCell.setCellValue(permissionList[j]);
                            }

                            for (int k = 0; k < permissionMap.size(); k++) {
                                userRow = userSheet.createRow(k + 1);
                                userCell = userRow.createCell(0);
                                userCell.setCellValue(tbList[k]);
                                String[] permissions = permissionMap.get(tbList[k]);

                                for (int m = 0; m < permissionList.length; m++) {
                                    userCell = userRow.createCell(m + 1);
                                    userCell.setCellValue(permissions[m]);
                                }
                            }
                        }

                        //保存工作簿到文件
                        usersWorkbook.write(outputStream);
                        System.out.println("users初始化成功！");
                        System.out.println("sys初始化成功！");
                    }
                } catch (IOException e) {
                    System.out.println("users创建失败！");
                    e.fillInStackTrace();
                    System.out.println("初始化失败！");
                    return 1;
                }
            } else {
                System.out.println("sys创建失败！");
                System.out.println("初始化失败！");
                return 1;
            }
        }

        if (data.exists() && data.isDirectory()) {
            System.out.println("data已存在！");
        } else if (data.exists() && !data.isDirectory()) {
            if (data.delete()) {
                if (data.mkdir()) {
                    System.out.println("data创建成功！");
                } else {
                    System.out.println("data创建失败！");
                    System.out.println("初始化失败！");
                    return 1;
                }
            } else {
                System.out.println("data删除失败！");
                System.out.println("初始化失败！");
                return 1;
            }
        } else {
            if (data.mkdir()) {
                System.out.println("data创建成功！");
            } else {
                System.out.println("data创建失败！");
                System.out.println("初始化失败！");
                return 1;
            }
        }


        //tbInformation初始化
        if (tbInformation.exists()) {
            System.out.println("tableInformation已存在！");
        } else {
            //创建tbInformation文件夹
            if (tbInformation.mkdir()) {
                System.out.println("tbInformation创建成功！");

                //创建数据库文件夹
                if (exampleDb.mkdir()) {
                    System.out.println("exampleDb创建成功！");

                    //创建表工作簿
                    try (XSSFWorkbook exampleTbWorkbook = new XSSFWorkbook()) {
                        try (FileOutputStream outputStream = new FileOutputStream(exampleTbPath)) {
                            System.out.println("exampleTb创建成功！");

                            //设置model表，存储example表模式
                            Sheet modelSheet = exampleTbWorkbook.createSheet("model");
                            String[] cnList = {"cnList", "type", "null", "unique", "primary key", "foreign key"};
                            String[] cnNameList = {"学号", "姓名", "年龄"};
                            Map<String, String[]> cnMap = new HashMap<>();
                            cnMap.put("学号", new String[]{"char", "0", "1", "1", "null"});
                            cnMap.put("姓名", new String[]{"char", "0", "0", "0", "null"});
                            cnMap.put("年龄", new String[]{"int", "1", "0", "0", "null"});
                            Row modelRow;
                            Cell modelCell;

                            modelRow = modelSheet.createRow(0);
                            for (int i = 0; i < cnList.length; i++) {
                                modelCell = modelRow.createCell(i);
                                modelCell.setCellValue(cnList[i]);
                            }

                            for (int j = 0; j < cnNameList.length; j++) {
                                modelRow = modelSheet.createRow(j + 1);
                                modelCell = modelRow.createCell(0);
                                modelCell.setCellValue(cnNameList[j]);
                                String[] constrain = cnMap.get(cnNameList[j]);

                                for (int k = 0; k < cnList.length; k++) {
                                    modelCell = modelRow.createCell(k + 1);
                                    modelCell.setCellValue(constrain[k]);
                                }
                            }

                            //设置check表
                            Sheet checkSheet = exampleTbWorkbook.createSheet("check");
                            String[] ckList = {"cnName"};

                            Map<String, String[]> ckMap = new HashMap<>();
                            cnMap.put("学号", new String[]{">2020", "<2022"});
                            cnMap.put("姓名", new String[]{});
                            cnMap.put("年龄", new String[]{">18", "<24"});
                            Row checkRow;
                            Cell checkCell;

                            checkRow = modelSheet.createRow(0);
                            for (int i = 0; i < ckList.length; i++) {
                                checkCell = checkRow.createCell(i);
                                checkCell.setCellValue(ckList[i]);
                            }

                            for (int j = 0; j < cnNameList.length; j++) {
                                checkRow = checkSheet.createRow(j + 1);
                                checkCell = checkRow.createCell(0);
                                checkCell.setCellValue(cnNameList[j]);
                                String[] check = ckMap.get(cnNameList[j]);

                                for (int k = 0; k < check.length; k++) {
                                    modelCell = modelRow.createCell(k + 1);
                                    modelCell.setCellValue(check[k]);
                                }
                            }

                            //保存工作簿到文件
                            exampleTbWorkbook.write(outputStream);
                            System.out.println("exampleTb初始化成功！");
                            System.out.println("tbInformation初始化成功！");
                        }
                    } catch (IOException e) {
                        System.out.println("exampleTb创建失败！");
                        System.out.println("初始化失败！");
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("exampleDb创建失败！");
                    System.out.println("初始化失败！");
                    return 1;
                }
            } else {
                System.out.println("tbInformation创建失败！");
                System.out.println("初始化失败！");
                return 1;
            }
        }

        //data初始化
        if (tbInformation.exists()) {
            System.out.println("tableInformation已存在！");
        } else {
            //创建tbInformation文件夹
            if (tbInformation.mkdir()) {
                System.out.println("tbInformation创建成功！");

                //创建数据库文件夹
                if (exampleDb.mkdir()) {
                    System.out.println("exampleDb创建成功！");

                    //创建表工作簿
                    try (XSSFWorkbook exampleTbWorkbook = new XSSFWorkbook()) {
                        try (FileOutputStream outputStream = new FileOutputStream(exampleTbPath)) {
                            System.out.println("exampleTb创建成功！");

                            //设置model表，存储example表模式
                            Sheet modelSheet = exampleTbWorkbook.createSheet("model");
                            String[] cnList = {"cnList", "type", "null", "unique", "primary key", "foreign key"};
                            String[] cnNameList = {"学号", "姓名", "年龄"};
                            Map<String, String[]> cnMap = new HashMap<>();
                            cnMap.put("学号", new String[]{"char", "0", "1", "1", "null"});
                            cnMap.put("姓名", new String[]{"char", "0", "0", "0", "null"});
                            cnMap.put("年龄", new String[]{"int", "1", "0", "0", "null"});
                            Row modelRow;
                            Cell modelCell;

                            modelRow = modelSheet.createRow(0);
                            for (int i = 0; i < cnList.length; i++) {
                                modelCell = modelRow.createCell(i);
                                modelCell.setCellValue(cnList[i]);
                            }

                            for (int j = 0; j < cnNameList.length; j++) {
                                modelRow = modelSheet.createRow(j + 1);
                                modelCell = modelRow.createCell(0);
                                modelCell.setCellValue(cnNameList[j]);
                                String[] constrain = cnMap.get(cnNameList[j]);

                                for (int k = 0; k < cnList.length; k++) {
                                    modelCell = modelRow.createCell(k + 1);
                                    modelCell.setCellValue(constrain[k]);
                                }
                            }

                            //设置check表
                            Sheet checkSheet = exampleTbWorkbook.createSheet("check");
                            String[] ckList = {"cnName"};

                            Map<String, String[]> ckMap = new HashMap<>();
                            cnMap.put("学号", new String[]{">2020", "<2022"});
                            cnMap.put("姓名", new String[]{});
                            cnMap.put("年龄", new String[]{">18", "<24"});
                            Row checkRow;
                            Cell checkCell;

                            checkRow = modelSheet.createRow(0);
                            for (int i = 0; i < ckList.length; i++) {
                                checkCell = checkRow.createCell(i);
                                checkCell.setCellValue(ckList[i]);
                            }

                            for (int j = 0; j < cnNameList.length; j++) {
                                checkRow = checkSheet.createRow(j + 1);
                                checkCell = checkRow.createCell(0);
                                checkCell.setCellValue(cnNameList[j]);
                                String[] check = ckMap.get(cnNameList[j]);

                                for (int k = 0; k < check.length; k++) {
                                    modelCell = modelRow.createCell(k + 1);
                                    modelCell.setCellValue(check[k]);
                                }
                            }

                            //保存工作簿到文件
                            exampleTbWorkbook.write(outputStream);
                            System.out.println("exampleTb初始化成功！");
                            System.out.println("tbInformation初始化成功！");
                        }
                    } catch (IOException e) {
                        System.out.println("exampleTb创建失败！");
                        System.out.println("初始化失败！");
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("exampleDb创建失败！");
                    System.out.println("初始化失败！");
                    return 1;
                }
            } else {
                System.out.println("tbInformation创建失败！");
                System.out.println("初始化失败！");
                return 1;
            }
        }

        return 0;
    }

    private static Map<String, Map<String, String>> getStringMapMap() {
        Map<String, Map<String, String>> systemMap = new HashMap<>();

        Map<String, String> tableMap = new HashMap<>();
        tableMap.put("table", "permission");
        tableMap.put("columnName", "table");
        tableMap.put("type", "char[50]");
        tableMap.put("null", "NULL");
        tableMap.put("unique", "1");
        tableMap.put("primaryKey", "1");
        tableMap.put("foreignKey", "null");
        systemMap.put("table", tableMap);

        Map<String, String> selectMap = new HashMap<>();
        selectMap.put("table", "permission");
        selectMap.put("columnName", "select");
        tableMap.put("type", "char");
        selectMap.put("null", "NULL");
        selectMap.put("unique", "NULL");
        tableMap.put("primaryKey", "NULL");
        selectMap.put("foreignKey", "NULL");
        systemMap.put("select", selectMap);

        Map<String, String> insertMap = new HashMap<>();
        insertMap.put("table", "permission");
        insertMap.put("columnName", "insert");
        insertMap.put("type", "char");
        insertMap.put("null", "NULL");
        insertMap.put("unique", "NULL");
        insertMap.put("primaryKey", "NULL");
        insertMap.put("foreignKey", "NULL");
        systemMap.put("insert", insertMap);

        Map<String, String> updateMap = new HashMap<>();
        updateMap.put("table", "permission");
        updateMap.put("columnName", "update");
        updateMap.put("type", "char");
        updateMap.put("null", "NULL");
        updateMap.put("unique", "NULL");
        updateMap.put("primaryKey", "NULL");
        updateMap.put("foreignKey", "NULL");
        systemMap.put("update", updateMap);

        Map<String, String> deleteMap = new HashMap<>();
        deleteMap.put("table", "permission");
        deleteMap.put("columnName", "delete");
        deleteMap.put("type", "char");
        deleteMap.put("null", "NULL");
        deleteMap.put("unique", "NULL");
        deleteMap.put("primaryKey", "NULL");
        deleteMap.put("foreignKey", "NULL");
        systemMap.put("delete", deleteMap);

        Map<String, String> createMap = new HashMap<>();
        createMap.put("table", "permission");
        createMap.put("columnName", "create");
        createMap.put("type", "char");
        createMap.put("null", "NULL");
        createMap.put("unique", "NULL");
        createMap.put("primaryKey", "NULL");
        createMap.put("foreignKey", "NULL");
        systemMap.put("create", createMap);

        Map<String, String> dropMap = new HashMap<>();
        dropMap.put("table", "permission");
        dropMap.put("columnName", "drop");
        dropMap.put("type", "char");
        dropMap.put("null", "NULL");
        dropMap.put("unique", "NULL");
        dropMap.put("primaryKey", "NULL");
        dropMap.put("foreignKey", "NULL");
        systemMap.put("drop", dropMap);

        Map<String, String> alterMap = new HashMap<>();
        alterMap.put("table", "permission");
        alterMap.put("columnName", "alter");
        alterMap.put("type", "char");
        alterMap.put("null", "NULL");
        alterMap.put("unique", "NULL");
        alterMap.put("primaryKey", "NULL");
        alterMap.put("foreignKey", "NULL");
        systemMap.put("alter", alterMap);

        Map<String, String> grantMap = new HashMap<>();
        grantMap.put("table", "permission");
        grantMap.put("columnName", "grant");
        grantMap.put("type", "char");
        grantMap.put("null", "NULL");
        grantMap.put("unique", "NULL");
        grantMap.put("primaryKey", "NULL");
        grantMap.put("foreignKey", "NULL");
        systemMap.put("grant", grantMap);

        Map<String, String> revokeMap = new HashMap<>();
        revokeMap.put("table", "permission");
        revokeMap.put("columnName", "revoke");
        revokeMap.put("type", "char");
        revokeMap.put("null", "NULL");
        revokeMap.put("unique", "NULL");
        revokeMap.put("primaryKey", "NULL");
        revokeMap.put("foreignKey", "NULL");
        systemMap.put("revoke", revokeMap);

        Map<String, String> executeMap = new HashMap<>();
        executeMap.put("table", "permission");
        executeMap.put("columnName", "execute");
        executeMap.put("type", "char");
        executeMap.put("null", "NULL");
        executeMap.put("unique", "NULL");
        executeMap.put("primaryKey", "NULL");
        executeMap.put("foreignKey", "NULL");
        systemMap.put("execute", executeMap);

        Map<String, String> referencesMap = new HashMap<>();
        referencesMap.put("table", "permission");
        referencesMap.put("columnName", "references");
        referencesMap.put("type", "char");
        referencesMap.put("null", "NULL");
        referencesMap.put("unique", "NULL");
        referencesMap.put("primaryKey", "NULL");
        referencesMap.put("foreignKey", "NULL");
        systemMap.put("references", referencesMap);

        Map<String, String> allPrivilegesMap = new HashMap<>();
        allPrivilegesMap.put("table", "permission");
        allPrivilegesMap.put("columnName", "all privileges");
        allPrivilegesMap.put("type", "char");
        allPrivilegesMap.put("null", "NULL");
        allPrivilegesMap.put("unique", "NULL");
        allPrivilegesMap.put("primaryKey", "NULL");
        allPrivilegesMap.put("foreignKey", "NULL");
        systemMap.put("all privileges", allPrivilegesMap);
        return systemMap;
    }
}