# SETUP - everything you need

## 1. Download and install
| What | Version | Link |
|---|---|---|
| JDK (has javac) | 11 or newer (17/21 fine) | https://adoptium.net |
| MySQL Server | 8.x | https://dev.mysql.com/downloads/mysql/ |
| MySQL Connector/J (JDBC driver jar) | 8.x - choose "Platform Independent", unzip, take mysql-connector-j-8.x.x.jar | https://dev.mysql.com/downloads/connector/j/ |
| Apache Tomcat (web part only) | 9.x (NOT 10 or 11) - download the zip/tar.gz | https://tomcat.apache.org/download-90.cgi |

Check JDK: open a terminal and run `javac -version`.

## 2. Create the database
mysql -u root -p < sql/schema.sql
(or open sql/schema.sql in MySQL Workbench and run it)

## 3. Set your MySQL password
Edit src/payroll/db/DBConnection.java and change PASSWORD (and USER if not root).

## 4. Put the JDBC jar in lib/
Copy mysql-connector-j-8.x.x.jar into the lib folder of this project.

## 5A. Run the desktop (AWT) app
Windows: double-click run-gui.bat
Linux/Mac: ./run-gui.sh

## 5B. Run the web (Servlet + JSP) app
1. Unzip Tomcat 9 somewhere.
2. Set TOMCAT_HOME to that folder
   Windows:    set TOMCAT_HOME=C:\apache-tomcat-9.0.x
   Linux/Mac:  export TOMCAT_HOME=~/apache-tomcat-9.0.x
3. Run deploy-web.bat (Windows) or ./deploy-web.sh (Linux/Mac)
4. Start Tomcat: bin\startup.bat or bin/startup.sh
5. Open http://localhost:8080/payroll/

## Troubleshooting
- "driver not found": jar is missing from lib/ (desktop) - rerun the script.
- "Access denied": wrong USER/PASSWORD in DBConnection.java.
- "Unknown database payroll_db": schema.sql was not run.
- Web app 404: use Tomcat 9, and make sure payroll.war appears in webapps/ and unpacks to webapps/payroll.
- Port 8080 busy: change the port in Tomcat conf/server.xml.
