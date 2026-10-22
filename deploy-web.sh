#!/bin/bash
# Builds payroll.war and copies it into Tomcat 9 (Linux / macOS). Usage: TOMCAT_HOME=/path/to/tomcat ./deploy-web.sh
cd "$(dirname "$0")"
if [ -z "$TOMCAT_HOME" ]; then echo "Set TOMCAT_HOME first, e.g. export TOMCAT_HOME=~/apache-tomcat-9.0.x"; exit 1; fi
JAR=$(ls lib/mysql-connector-j-*.jar 2>/dev/null | head -1)
if [ -z "$JAR" ]; then echo "Put mysql-connector-j-*.jar in the lib folder first."; exit 1; fi
rm -rf build payroll.war
mkdir -p build/WEB-INF/classes build/WEB-INF/lib
cp -r web/* build/
cp "$JAR" build/WEB-INF/lib/
javac -cp "$TOMCAT_HOME/lib/servlet-api.jar" -d build/WEB-INF/classes \
  src/payroll/model/*.java src/payroll/db/*.java src/payroll/dao/*.java src/payroll/servlet/*.java || exit 1
(cd build && jar cf ../payroll.war .)
cp payroll.war "$TOMCAT_HOME/webapps/"
echo "Deployed. Start Tomcat ($TOMCAT_HOME/bin/startup.sh) and open http://localhost:8080/payroll/"
