import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.IOException;
import java.util.Objects;
import java.io.File;
import java.util.function.Function;

//解析命令以及预处理工作
public class Interpreter {
    // 当前使用的数据库名称
    static String usingDbName = "";
    // 当前使用的数据库文件句柄
    static XSSFWorkbook usingDb = null;

    public static int query(String sql, String user) {
        // 返回值：0，一切正常；1，命令语法错误；2，命令执行失败
        // sql语句
        String[] sqlWord = sql.split(" ");
        // 没有长度小于2的命令
        if (sqlWord.length < 2) {
            System.out.println("命令错误！");
            return 1;
        }
        // 语句第一个词为操作码
        String operate = sqlWord[0];

        if (Objects.equals(operate, "use")) {
            // 使用数据库 using database {databaseName}
            if (Objects.equals(sqlWord[1], "database")) {
                // 如果已经打开过一个数据库，则把该数据库关闭
                if (usingDb != null) {
                    try {
                        usingDb.close();
                    } catch (IOException e) {
                        e.fillInStackTrace();
                        System.out.println("关闭数据库{" + usingDbName + "}时出现错误！");
                        return 0;
                    }
                    usingDb = null;
                    usingDbName = "";
                }
                usingDb = SqlFunction.useDataBase(sqlWord[2]);
                if (usingDb != null) {
                    usingDbName = sqlWord[2];
                    return 0;
                } else {
                    return 2;
                }
            } else {
                System.out.println("命令语法错误！");
                return 1;
            }
        } else if (Objects.equals(operate, "create")) {
            // 创建数据库 create database {databaseName}
            if (Objects.equals(sqlWord[1], "database")) {
                String createDbName = sqlWord[2];

                int re = SqlFunction.createDatabase(createDbName);

                if (re == 0) {
                    return 0;
                } else {
                    return 2;
                }
            }
            // 创建表 creat table {tableName}
            else if (Objects.equals(sqlWord[1], "table")) {
                String createTbName = sqlWord[2];

            } else {
                System.out.println("命令语法错误！");
                return 1;
            }
        }
        // 删除数据库 drop database {databaseName}
        else if (Objects.equals(operate, "drop")) {
            if (Objects.equals(sqlWord[1], "database")) {
                String dropDbName = sqlWord[2];

                int re = SqlFunction.dropDatabase(dropDbName);

                if (re == 0) {
                    return 0;
                } else {
                    return 2;
                }
            } else {
                System.out.println("命令语法错误！");
                return 1;
            }
        }

        // 语句匹配失败
        System.out.println("命令语法错误！");
        return 1;
    }}

    // grantprivilege
    // // 检查用户的合法性(用户是否存在)
    // XSSFWorkbook Users = new XSSFWorkbook("../../../sys/users.xlsx");

    // XSSFSheet userSheet = Users.getSheet("up");
    // boolean userFlag = false;for(
    // Row row:userSheet)
    // {
    //     if (row.getCell(0).getStringCellValue().equals(userName)) {
    //         userFlag = true;
    //     }
    // }Users.close();if(!userFlag)
    // {
    //     System.out.println("用户不存在,授权失败");
    //     return 2;
    // }

    // // 检查权限的合法性
    // String[] privilegdeStrings = new String[] { "select", "insert", "update", "delete", "create", "drop", "alter" };
    // HashSet<String> privilegde = new HashSet<>();Collections.addAll(privilegde,privilegdeStrings);if(!privilegde.contains(privilegesCode))
    // {
    //     System.out.println("权限错误,授权失败");
    //     return 2;
    // }

    // // 检查数据库和表存在(不完整)
    // try
    // {
    //     XSSFWorkbook dB = new XSSFWorkbook("../../../data/" + dbName);
    //     XSSFSheet sheet = dB.getSheet(tableName);
    //     dB.close();
    // }catch(
    // Exception e)
    // {
    //     // TODO Auto-generated catch block
    //     e.printStackTrace();
    //     System.out.println("数据库不存在,授权失败");
    //     return 2;
    // }finally{
            
    //     }