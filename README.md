# SystemPerformance-Backend

This is an java project developed using plain java and servlet which is the backend for system-performance application.

This application has a cpp service which constantly gets the system's RAM Usage,CPU Usage and sends the data (through http) -> java -> database.
From the front-end the user can able to view the statistics as a chart view.

Have a look on `Architecture Diagram.png` in this repo for clear understanding.

The master branch is the old code which is like a prototype with only core logic (java to cpp connection is by JNI (Java Native Interface)).

The masterv1 branch is the updated code with all the features included (java to cpp connection is through Http request).

# Prerequisites

1. Install Java (openjdk version 11.0.8)
2. Install maven (version 3.6.0)
3. Install mysql (version 5.7.31)
4. Install Elastic Search (version 7.9.1)

# Steps to Installation and Run

1. `git clone https://github.com/HarikumarG/SystemPerformance-Backend.git && cd SystemPerformance-Backend`
2. Run `mvn clean install` command to download the required dependencies from pom.xml
3. Login to Mysql and create a database with the command `create database system_performance` and `use system_performance`
4. Run the command `source system_performance.sql`
5. Setup Elastic Search (version 7.9.0) either in docker or in system, but configure the host and port as "localhost","9200"
6. Copy the "jar file" which is generated in the "target" folder to "tomcat->webapps" folder
7. Start the tomcat server
8. For Step 6 & 7, there are two files named "start.sh" and "stop.sh" in that there are commands which does those steps. Have a look on that.

To Store the statistics of your machine in database
1. There is a endpoint given in the code to Sign Up for a new machine
2. Using postman hit a post request for http://localhost:8080/SystemPerformance-Backend/machineSignUp with the request body as `{"SystemName" : "SYSTEM-1","UUID": "<your-machine-UUID>"}`
3. After signing up new machine run the cpp to store the live statistics in your database

For Cpp to Run (For Ubuntu)
1. After starting the server open another terminal and compile the cpp file with the command `g++ service.cpp -lcurl -pthread`
2. Run `./a.out`
3. The cpp program will ask for UUID of your machine to store the statistics in database.
4. For Ubuntu type the command `sudo dmidecode -s system-uuid`
5. For Windows type the command `wmic csproduct get UUID`


Note: To check the project working
Have a look on front-end code.

https://github.com/HarikumarG/SystemPerformance-Frontend.git

Run the front-end code and for sample login
"Employee-ID" - "Hari" and 
"Password" - "Hari"
