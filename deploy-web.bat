@echo off
rem Builds payroll.war and copies it into Tomcat 9 (Windows). First: set TOMCAT_HOME=C:\path\to\apache-tomcat-9.0.x
cd /d %~dp0
if "%TOMCAT_HOME%"=="" (
  echo Set TOMCAT_HOME first, e.g. set TOMCAT_HOME=C:\apache-tomcat-9.0.x
  pause
  exit /b 1
)
set JAR=
for %%f in (lib\mysql-connector-j-*.jar) do set JAR=%%f
if "%JAR%"=="" (
  echo Put mysql-connector-j-*.jar in the lib folder first.
  pause
  exit /b 1
)
if exist build rmdir /s /q build
if exist payroll.war del payroll.war
mkdir build\WEB-INF\classes
mkdir build\WEB-INF\lib
xcopy /e /y /q web\* build\ >nul
copy /y "%JAR%" build\WEB-INF\lib\ >nul
javac -cp "%TOMCAT_HOME%\lib\servlet-api.jar" -d build\WEB-INF\classes src\payroll\model\*.java src\payroll\db\*.java src\payroll\dao\*.java src\payroll\servlet\*.java
if errorlevel 1 (pause & exit /b 1)
cd build
jar cf ..\payroll.war .
cd ..
copy /y payroll.war "%TOMCAT_HOME%\webapps\" >nul
echo Deployed. Start Tomcat (bin\startup.bat) and open http://localhost:8080/payroll/
pause
