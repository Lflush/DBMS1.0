# DBMS1.0
<<<<<<< HEAD
数据库课程设置
=======
创建用户：create user {userName} identified by {password};

用户授权：grant {privilegesCode} on {dbName.tableName} to {userName} identified by {password};

	privilegesCode：[all privileges]、[select]、[delete]、[update]、[create]、[drop]

	dbName.tableName：[dbName.*]、[dbName.tableName]

查看用户权限：show grants for {userName};

取消用户权限：revoke {privilegesCode} on {daName.tableName} from {userName};

修改用户密码：updata password {userName} {password} {newPassword};

删除用户：drop user {userName};

查看所有数据库：show database;

创建数据库：create database {dbName};

删除数据库：drop database {dbName};

使用数据库：use database {dbName};

查看所有表：show tables;

创建表：create table {tbName} ({cnName} {type} {constrain} {check} {foreign key}, ...);

	type：char、int、
	
	constrain：null、unique、primary key(pkName)、foreign key（fkName）、
	
	check：

查看表模式：show table {tbName};

删除表：drop table {tbName};

表添加字段：alter table {tbName} add ({cnName} {type} {constrain} {check}  {foreign key}, ...);

表删除字段：alter table {tbName} drop ({cnName}, ...);

表修改字段：alter table {tbName} change ({cnName}, ...) to ({cnName} {type} {constrain} {check}  {foreign key}, ...);

表插入数据：insert into {tbName} ({cnName}, ...) value ({data}, ...)

表删除数据：delete from {tbName} where {clause};

	clause：like、

表更改数据：updata tbName set ({cnName} = {data}, ...) where {clause};

表查找数据：select ({cnName}, ...) from ({tbName}, ...) order by ({cnName}, ...) {sort} where {clause}; 

	sort：desc、asc

创建视图：create view {vwName} as {select ...};

更改视图：replace view {vwName} as {selece ...};

查看视图创建语句：show view {vwName};

删除视图：drop view {vwName}

视图查找数据：select ({cnName}, ...) from view ({tbName}, ...) order by ({cnName}, ...) {sort} where {clause}; 

内连接：select {cnName} from {tbName} inner join {tbName} on {clause}

外连接：select {cnName} from {tbName} left inner join {tbName} on {clause}

        select {cnName} from {tbName} right inner join {tbName} on {clause}

当前用户：select user();

当前数据库：select database();

>>>>>>> d89666ec28762a9236fe574f6b7ffc0f26e3aa24
