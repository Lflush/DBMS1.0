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
    public static int initialize() {
        //返回值：0，一切正常；1，初始化失败
        String currentDir = System.getProperty("user.dir");

        String dataPath = currentDir + "\\data";
        File data = new File(dataPath);

        String tableInformationPath = dataPath + "\\tableInformation.xlsx";
        File tableInformation = new File(tableInformationPath);

        String systemPath = dataPath + "\\system.xlsx";
        File system = new File(systemPath);

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

        if (system.exists()) {
            System.out.println("system已存在！");
        } else {
            //创建system工作簿
            try (XSSFWorkbook systemWorkbook = new XSSFWorkbook()) {
                try (FileOutputStream outputStream = new FileOutputStream(systemPath)) {
                    System.out.println("system创建成功！");
                    //设置user表
                    Sheet user = systemWorkbook.createSheet("user");
                    Row userRow = user.createRow(0);
                    Cell userCell = userRow.createCell(0);
                    userCell.setCellValue("userName");
                    userCell = userRow.createCell(1);
                    userCell.setCellValue("password");

                    userRow = user.createRow(1);
                    userCell = userRow.createCell(0);
                    userCell.setCellValue("admin");
                    userCell = userRow.createCell(1);
                    userCell.setCellValue("admin");

                    //设置permission表
                    Sheet permission = systemWorkbook.createSheet("permission");
                    Row permissionRow = permission.createRow(0);
                    Cell permissionCell;
                    String[] permissionList = {"table", "select", "insert", "update", "delete", "create", "drop", "alter",
                            "grant", "revoke", "execute", "references", "all privileges"};
                    for (int i = 0; i < 12; i++) {
                        permissionCell = permissionRow.createCell(i);
                        permissionCell.setCellValue(permissionList[i]);
                    }

                    String[] tableList = {"system", "tableInformation"};

                    for (int i = 1; i <= 2; i++) {
                        permissionRow = permission.createRow(i);
                        permissionCell = permissionRow.createCell(0);
                        permissionCell.setCellValue(tableList[i - 1]);

                        for (int j = 1; j < permissionList.length; j++) {
                            permissionCell = permissionRow.createCell(j);
                            permissionCell.setCellValue("admin,root");
                        }
                    }

                    //保存工作簿到文件
                    systemWorkbook.write(outputStream);
                    System.out.println("system初始化成功！");
                }
            } catch (IOException e) {
                System.out.println("system创建失败！");
                e.fillInStackTrace();
                System.out.println("初始化失败！");
                return 1;
            }
        }

        if (tableInformation.exists()) {
            System.out.println("tableInformation已存在！");
        } else {
            //创建tableInformation工作簿
            try (XSSFWorkbook tableInformationWorkbook = new XSSFWorkbook()) {
                try (FileOutputStream outputStream = new FileOutputStream(tableInformationPath)) {
                    System.out.println("tableInformation创建成功！");
                    //初始化tableInformation
                    String[] tableInformationList = {"table", "columnName", "type", "null", "unique", "primaryKey",
                            "foreignKey"};
                    //设置system表
                    Sheet systemTable = tableInformationWorkbook.createSheet("system");
                    String[] columnName = {"table", "select", "insert", "update", "delete", "create", "drop", "alter",
                            "grant", "revoke", "execute", "references", "all privileges"};

                    Map<String, Map<String, String>> systemMap = getStringMapMap();

                    Row systemRow = systemTable.createRow(0);
                    Cell systemCell;
                    for (int i = 0; i < tableInformationList.length; i++) {
                        systemCell = systemRow.createCell(i);
                        systemCell.setCellValue(tableInformationList[i]);
                    }

                    for (int i = 0; i < columnName.length; i++) {
                        systemRow = systemTable.createRow(i + 1);
                        Map<String, String> temp0 = systemMap.get(columnName[i]);

                        for (int j = 0; j < tableInformationList.length; j++) {
                            String temp1 = temp0.get(tableInformationList[j]);
                            systemCell = systemRow.createCell(j);
                            systemCell.setCellValue(temp1);
                        }
                    }

                    // 保存工作簿到文件
                    tableInformationWorkbook.write(outputStream);
                    System.out.println("tableInformation初始化成功！");
                }
            } catch (IOException e) {
                System.out.println("tableInformation创建失败！");
                e.fillInStackTrace();
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