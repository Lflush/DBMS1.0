import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashSet;

public class SqlFunction {

    // 当前用户和当前使用的数据库
    public static String currentUser = "root";
    public static String currentDataBase = "exampleDb";

    // 帮助页
    public static void help() {
        System.out.println("\"\"\"\n" +
                "    ## 登录管理员\n" +
                "    username:admin\n" +
                "    username:admin\n" +
                "\n" +
                "    ## 创建数据库\n" +
                "    create database {database_name}\n" +
                "    eg.: create database test_db\n" +
                "\n" +
                "    ## 删除数据库\n" +
                "    drop database {database_name}\n" +
                "    eg.: drop database test_db\n" +
                "\n" +
                "    ## 使用数据库\n" +
                "    use database {database_name}\n" +
                "    eg.: use database test_db\n" +
                "\n" +
                "    ## 创建表\n" +
                "    create table {table_name} ({column_name} {data_type} {PK,null...},{column_name} {data_type} {PK,null...}...)\n"
                +
                "    eg.: create table test (v1 int PK null,v2 int)\n" +
                "\n" +
                "    ## 删除表\n" +
                "    drop table {table_name}\n" +
                "    eg.: drop table test\n" +
                "\n" +
                "    ## 添加字段\n" +
                "    alter {table_name} add ({column_name} {data_type} {PK,null...})\n" +
                "    eg.: alter test add (v3 int)\n" +
                "\n" +
                "    ## 删除字段\n" +
                "    alter {table_name} drop ({column_name})\n" +
                "    eg.: alter test drop (v3)\n" +
                "\n" +
                "    ## 修改字段\n" +
                "    alter {table_name} modify {alter_field_name} ({column_name} {data_type} {PK,null...}) \n" +
                "    eg.: alter test modify v1 (v3 int PK null)\n" +
                "    \n" +
                "    ## 记录插入\n" +
                "    insert into {table_name} {column_name=value,column_name=value,...)\n" +
                "    eg.: insert into test v1=1,v2=2\n" +
                "\n" +
                "    ## 记录插入（多重）\n" +
                "    insert into {table_name} {column_name=value,column_name=value,...&column_name=value,column_name=value,...)\n"
                +
                "    eg.: insert into test v3=2,v2=4&v3=3,v2=5\n" +
                "\n" +
                "    ## 记录删除\n" +
                "    delete on {table_name} where {column_name=value或column_name>value或column_name<value}\n" +
                "    eg.: delete on test where v3=1\n" +
                "\n" +
                "    ## 记录删除（多重）\n" +
                "    delete on {table_name} where {column_name=value或column_name>value或column_name<value&column_name=value或column_name>value或column_name<value&..}\n"
                +
                "    eg.: delete on test where v3=1&v2=2\n" +
                "\n" +
                "    ## 记录修改\n" +
                "    update {table_name} set column_name=value,column_name=value,... where {column_name=value或column_name>value或column_name<value（可多重）}\n"
                +
                "    eg.: update test set v3=4,v2=3 where v3=2\n" +
                "\n" +
                "    ## 选择全部\n" +
                "    select * from {table_name}\n" +
                "    eg.: select * from test\n" +
                "\n" +
                "    ## 选择指定列\n" +
                "    select {column_name} from {table_name}\n" +
                "    eg.:select v3 from test\n" +
                "\n" +
                "    ## 选择where条件\n" +
                "    select * 或{column_name} from {table_name} where {column_name=value或column_name>value或column_name<value（可多重）}\n"
                +
                "    eg.: select * from test where v3=4\n" +
                "\n" +
                "    ## 注册用户\n" +
                "    signup {username} {password}\n" +
                "    eg.: signup admin admin\n" +
                "\n" +
                "    ## 读取脚本\n" +
                "    load {script_name}\n" +
                "    eg.: load test.txt\n" +
                "\n" +
                "    ## 创建视图\n" +
                "    create view {view_name} as select * 或{column_name} from {table_name}\n" +
                "    eg.: create view test as select * from test\n" +
                "\n" +
                "    ## 赋予权限\n" +
                "    grant {action} on {database_name} for {username}\n" +
                "    eg.: grant select on test_db for aaa\n" +
                "\n" +
                "    ## 收回权限\n" +
                "    revoke {action} on {database_name} for {username}\n" +
                "    eg.: revoke select on test_db for aaa\n" +
                "\n" +
                "    \"\"\"");
    }

    // 使用数据库 using database {databaseName}
    public static XSSFWorkbook useDataBase(String usingDbName) {
        // 返回值：0，一切正常；1，数据库不存在
        String currentDir = System.getProperty("user.dir");
        String usingDbPath = currentDir + "\\data\\" + usingDbName + ".xlsx";
        File usingDb = new File(usingDbPath);
        if (usingDb.exists()) {
            try (FileInputStream usingDbFile = new FileInputStream(usingDbPath)) {
                // 打开使用的数据库
                XSSFWorkbook usingDbWorkbook = new XSSFWorkbook(usingDbFile);

                System.out.println("数据库{" + usingDbName + "}使用成功！");

                currentDataBase = usingDbName;

                return usingDbWorkbook;
            } catch (IOException e) {
                e.fillInStackTrace();
                System.out.println("数据库{" + usingDbName + "}使用失败！");
                return null;
            }
        } else {
            System.out.println("数据库{" + usingDbName + "}不存在！");
            return null;
        }
    }

    // 创建数据库 create database {databaseName}
    public static int createDatabase(String createDbName) {
        // 返回值：0，一切正常；1，数据库已存在；2，文件创建失败
        String currentDir = System.getProperty("user.dir");
        String createDbPath = currentDir + "\\data\\" + createDbName + ".xlsx";
        File createDb = new File(createDbPath);

        if (createDb.exists()) {
            System.out.println("数据库" + createDbName + "已存在！");
            return 1;
        } else {
            try (XSSFWorkbook createDbWorkbook = new XSSFWorkbook()) {
                // 保存工作簿到文件
                try (FileOutputStream outputStream = new FileOutputStream(createDbPath)) {
                    createDbWorkbook.write(outputStream);
                    System.out.println("数据库" + createDbName + "创建成功！");
                    return 0;
                }
            } catch (IOException e) {
                System.out.println("数据库" + createDbName + "创建失败！");
                e.fillInStackTrace();
                return 2;
            }
        }
    }

    // 删除数据库 drop database {databaseName}
    public static int dropDatabase(String dropDbName) {
        // 返回值：0，一切正常；1，数据库不存在；2，文件删除失败
        String currentDir = System.getProperty("user.dir");
        String dropDbPath = currentDir + "\\data\\" + dropDbName + ".xlsx";
        File dropDb = new File(dropDbPath);

        if (dropDb.exists()) {
            if (dropDb.delete()) {
                System.out.println("数据库{" + dropDbName + "}删除成功！");
                return 0;
            } else {
                System.out.println("数据库{" + dropDbName + "}删除失败！");
                return 2;
            }
        } else {
            System.out.println("数据库{" + dropDbName + "}不存在！");
            return 1;
        }
    }

    // 创建表 creat table {tableName}
    // public static int createTable(String usingDbName, String createTbName) {
    // //返回值：0，一切正常；1，数据库不存在；2，创建失败
    //
    // }

    // 返回值：0，一切正常；1，数据库不存在；2，创建失败
    /**
     * 创建用户
     * 
     * @param userName 创建的用户名
     * @param password 密码
     * @return 0，创建正常；2，创建失败
     * @throws IOException
     */
    public static int createUser(String userName, String password) throws IOException {
        // 用户名或者密码为空返回创建失败
        if (userName == null || password == null) {
            return 2;
        }
        XSSFWorkbook Users = new XSSFWorkbook("../../../sys/users.xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream("../../../sys/users.xlsx");
        XSSFSheet sheet = Users.getSheet("up");
        for (Row row : sheet) {
            if (row.getCell(0).getStringCellValue().equals(userName)) {
                // 存在同名的用户，创建失败
                Users.close();
                System.out.println("存在同名的用户，创建失败");
                return 2;
            }
        }
        int insertRownum = sheet.getLastRowNum() + 1;
        XSSFRow insertRow = sheet.createRow(insertRownum);
        XSSFCell username = insertRow.createCell(0);
        username.setCellValue(userName);
        XSSFCell psw = insertRow.createCell(1);
        psw.setCellValue(password);
        Users.write(fileOutputStream);
        fileOutputStream.close();
        Users.close();
        System.out.println("创建成功");

        // 复制默认用户的表
        FileOutputStream fos = new FileOutputStream("../../../sys/" + userName + ".xlsx");
        FileInputStream fis = new FileInputStream("../../../sys/default.xlsx");

        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = fis.read(bytes)) != -1) {
            fos.write(bytes, 0, len);
        }
        fis.close();
        fos.close();
        return 0;
    }

    /**
     * 对用户授权
     * 
     * @param privilegesCode 权限名,一个
     * @param dbName         数据库名
     * @param tableName      表名
     * @param userName       用户名
     * @return 返回值,0,正常授权,2,授权失败
     * @throws IOException
     */
    public static int grantPrivilegde(String privilegesCode, String dbName, String tableName, String userName)
            throws IOException {
        if (privilegesCode == null || dbName == null || tableName == null || userName == null) {
            System.out.println("输入数据有误,授权失败");
            return 2;
        }

        FileOutputStream fos = new FileOutputStream("../../../sys" + userName + ".xlsx");
        XSSFWorkbook sys = new XSSFWorkbook("../../../sys" + userName + ".xlsx");
        XSSFSheet sheet = sys.getSheet(dbName);
        int priviegdeNum = 1;
        Row firstRow = sheet.getRow(0);
        for (Cell cell : firstRow) {
            if (cell.getStringCellValue().equals(privilegesCode)) {
                priviegdeNum = cell.getColumnIndex();
                break;
            }
        }
        for (Row table : sheet) {
            if (table.getCell(0).getStringCellValue().equals(tableName)) {
                table.getCell(priviegdeNum).setCellValue("1");
                break;
            }
        }
        sys.write(fos);
        sys.close();
        fos.close();
        System.out.println("授权成功");
        return 0;
    }

    /**
     * 展示当前用户
     */
    public static void showUser() {
        System.out.println("CurrentUser:" + currentUser);
    }

    /**
     * 展示当前使用数据库
     */
    public static void showdatabase() {
        System.out.println("CurrentDatabase:" + currentDataBase);
    }

    /**
     * 查看用户权限
     * 
     * @param userName 用户名
     * @throws IOException
     */
    public static void showGrants(String userName) throws IOException {
        if (userName == null) {
            System.out.println("用户名为空");
            return;
        }

        // 运行终端的路径为DBMS1.0
        XSSFWorkbook Users = new XSSFWorkbook("./sys/" + userName + ".xlsx");
        XSSFSheet sheet = Users.getSheet(currentDataBase);
        for (Row row : sheet) {
            for (Cell cell : row) {
                System.out.print(cell.getStringCellValue());
                System.out.print("\t");
            }
            System.out.println();
        }

        Users.close();
    }

    /**
     * 取消用户权限
     * 
     * @param privilegesCode 权限名,一个
     * @param dbName         数据库名
     * @param tableName      表名
     * @param userName       用户名
     * @return 返回值 0,操作正常,2,操作失败
     * @throws IOException
     */
    public static int revokePrivilegde(String privilegesCode, String dbName, String tableName, String userName)
            throws IOException {
        if (privilegesCode == null || dbName == null || tableName == null || userName == null) {
            System.out.println("参数错误,有参数为空,操作失败");
            return 2;
        }
        FileOutputStream fos = new FileOutputStream("./sys" + userName + ".xlsx");
        XSSFWorkbook sys = new XSSFWorkbook("./sys" + userName + ".xlsx");
        XSSFSheet sheet = sys.getSheet(dbName);
        int priviegdeNum = 1;
        Row firstRow = sheet.getRow(0);
        for (Cell cell : firstRow) {
            if (cell.getStringCellValue().equals(privilegesCode)) {
                priviegdeNum = cell.getColumnIndex();
                break;
            }
        }
        for (Row table : sheet) {
            if (table.getCell(0).getStringCellValue().equals(tableName)) {
                table.getCell(priviegdeNum).setCellValue("0");
                break;
            }
        }
        sys.write(fos);
        sys.close();
        fos.close();
        System.out.println("取消授权成功");
        return 0;
    }

    /**
     * 修改用户密码
     * 
     * @param userName    用户名
     * @param password    原密码
     * @param newPassword 新密码
     * @return 返回值 0,操作正常,2,操作失败
     * @throws IOException
     */
    public static int modifyUserPassword(String userName, String password, String newPassword) throws IOException {
        if (userName == null || password == null || newPassword == null) {
            System.out.println("输入数据错误,操作失败");
            return 2;
        }
        FileOutputStream fos = new FileOutputStream("./sys/users.xlsx");
        XSSFWorkbook Users = new XSSFWorkbook("./sys/users.xlsx");
        XSSFSheet up = Users.getSheet("up");
        // 检查密码正确
        for (Row row : up) {
            if (row.getCell(0).getStringCellValue().equals(userName)) {
                if (row.getCell(1).getStringCellValue().equals(password)) {
                    row.getCell(1).setCellValue(newPassword);
                    Users.write(fos);
                    System.out.println("修改密码成功");
                } else {
                    System.out.println("密码错误");
                }
                break;
            }
        }
        fos.close();
        Users.close();
        return 0;
    }

    /**
     * 删除用户
     * 
     * @param userName 用户名
     * @return 返回值 0,操作正常,2,操作失败
     * @throws IOException
     */
    public static int dropUser(String userName) throws IOException {
        if (userName == null) {
            System.out.println("输入用户名为空，删除失败");
            return 2;
        }
        FileOutputStream fos = new FileOutputStream("./sys/users.xlsx");
        XSSFWorkbook Users = new XSSFWorkbook("./sys/users.xlsx");
        XSSFSheet up = Users.getSheet("up");
        for (Row row : up) {
            if (row.getCell(0).getStringCellValue().equals(userName)) {
                row.getCell(0).setCellValue("null");
                row.getCell(1).setCellValue("null");
                Users.write(fos);
                System.out.println("删除用户成功");
            }
        }

        fos.close();
        Users.close();
        File userFile = new File("../../../sys/" + userName + ".xlsx");
        userFile.delete();

        return 0;
    }


    /**
     * 查看所有数据库
     */
    public static void showDataBases(){
        File sys=new File("./data");
        File[] listFiles = sys.listFiles();
        for (File f : listFiles) {
            if(f.isFile()){
                System.out.println(f.getName());
            }
        }
        return;
    }

    /**
     * 查看所有表(当前数据库下)
     * @throws IOException
     */
    public static void showTables() throws IOException{
        XSSFWorkbook db=new XSSFWorkbook("./data/"+currentDataBase+".xlsx");
        int numberOfSheets = db.getNumberOfSheets();
        for(int i=0;i<numberOfSheets;i++){
            String sheetName = db.getSheetName(i);
            System.out.println(sheetName);
        }
        db.close();
    }
}
