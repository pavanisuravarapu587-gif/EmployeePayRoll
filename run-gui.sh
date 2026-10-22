#!/bin/bash
# Compiles and runs the AWT desktop app (Linux / macOS)
cd "$(dirname "$0")"
JAR=$(ls lib/mysql-connector-j-*.jar 2>/dev/null | head -1)
if [ -z "$JAR" ]; then echo "Put mysql-connector-j-*.jar in the lib folder first."; exit 1; fi
mkdir -p out
javac -d out src/payroll/model/*.java src/payroll/db/*.java src/payroll/dao/*.java src/payroll/gui/*.java || exit 1
java -cp "out:$JAR" payroll.gui.PayrollApp
