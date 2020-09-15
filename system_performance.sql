/*
1. Install mysql-version 5.7.31
2. Create database name as system_performance
3. Run > use system_performance;
4. source system_performance.sql;
5. The port,user_name,password all configured in classes of dao package
*/
CREATE TABLE Configurations(ConfigID varchar(10) PRIMARY KEY,Name varchar(20));
INSERT INTO Configurations(ConfigID,Name)VALUES('0','Alert');
CREATE TABLE Employees(EmpID varchar(10) PRIMARY KEY,Password varchar(30) NOT NULL,Name varchar(30),Designation varchar(20),Place varchar(20));
INSERT INTO Employees(EmpID,Password,Name,Designation,Place)VALUES('1','Hari','Hari','Team Head','Chennai');
CREATE TABLE Machine(SystemName varchar(15) NOT NULL,UUID varchar(40) PRIMARY KEY,Config varchar(10) NOT NULL,MaxCpuUsage varchar(10),MaxUsedRAM varchar(10));
CREATE TABLE Performance(ID int PRIMARY KEY AUTO_INCREMENT,UUID varchar(40),TotalRAM varchar(15),UsedRAM varchar(15),CpuUsage varchar(15),TimeStamp datetime,FOREIGN KEY (UUID) REFERENCES Machine(UUID));