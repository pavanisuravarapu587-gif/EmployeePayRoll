@echo off
rem Compiles and runs the AWT desktop app (Windows)
cd /d %~dp0
set JAR=
for %%f in (lib\mysql-connector-j-*.jar) do set JAR=%%f
if "%JAR%"=="" (
  echo Put mysql-connector-j-*.jar in the lib folder first.
  pause
  exit /b 1
)
if not exist out mkdir out
javac -d out src\payroll\model\*.java src\payroll\db\*.java src\payroll\dao\*.java src\payroll\gui\*.java
if errorlevel 1 (pause & exit /b 1)
java -cp "out;%JAR%" payroll.gui.PayrollApp
