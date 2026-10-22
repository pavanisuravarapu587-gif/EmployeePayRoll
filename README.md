# Employee Payroll System (Java: AWT + JDBC + Servlets + JSP)

One database, two front ends:
- **Desktop client** - AWT GUI (`payroll.gui.PayrollApp`)
- **Web client** - Servlets + JSP on Tomcat 9

## Project layout
```
sql/schema.sql                      MySQL schema + sample data
src/payroll/model/Employee.java     JavaBean + salary rules + payslip text
src/payroll/db/DBConnection.java    JDBC driver loading + connection
src/payroll/dao/EmployeeDAO.java    CRUD with Statement / PreparedStatement / ResultSet
src/payroll/gui/PayrollApp.java     AWT application
src/payroll/servlet/*.java          3 servlet types
web/index.jsp, web/employees.jsp    JSP pages
web/WEB-INF/web.xml                 servlet mappings
```

## Salary rules
Gross = Basic + HRA (20%) + DA (10%) + Bonus (basic x bonus%)
PF = 12% of basic. Tax = 10% of gross if gross > 50,000, 5% if > 25,000, else 0.
Net = Gross - PF - Tax.

## Setup
1. Install MySQL, run `sql/schema.sql`.
2. Set your MySQL password in `DBConnection.java`.
3. Download `mysql-connector-j-8.x.jar` (JDBC driver).

## Run the AWT app (JDK 11+)
```
mkdir out
javac -d out src/payroll/model/*.java src/payroll/db/*.java src/payroll/dao/*.java src/payroll/gui/*.java
java -cp "out:mysql-connector-j-8.x.jar" payroll.gui.PayrollApp      (Windows: use ; instead of :)
```

## Run the web app (Tomcat 9, uses javax.servlet)
```
mkdir -p build/WEB-INF/classes build/WEB-INF/lib
cp -r web/* build/
cp mysql-connector-j-8.x.jar build/WEB-INF/lib/
javac -cp "<tomcat>/lib/servlet-api.jar" -d build/WEB-INF/classes \
      src/payroll/model/*.java src/payroll/db/*.java src/payroll/dao/*.java src/payroll/servlet/*.java
cp -r build <tomcat>/webapps/payroll
```
Start Tomcat, open http://localhost:8080/payroll/
(Tomcat 10+ needs `jakarta.servlet` imports instead of `javax.servlet`.)

URLs: `/employees` (HttpServlet -> JSP), `/generic` (GenericServlet), `/interface` (Servlet interface).

## Syllabus map
| Topic | Where |
|---|---|
| AWT class hierarchy | `PayrollApp extends Frame` (Object > Component > Container > Window > Frame) |
| Label, Button | header, form, dialogs |
| TextField / TextArea | form fields, report, payslip dialog |
| Checkbox, CheckboxGroup | bonus checkbox; Male/Female radio group |
| Choice | department drop-down |
| List | employee list on the left |
| Scrollbar | bonus % selector |
| Panel, ScrollPane | all panels; form wrapped in ScrollPane |
| Dialog | message, confirm (Yes/No), payslip dialogs |
| MenuBar | File / Employee / View / Help |
| FlowLayout, BorderLayout, GridLayout, CardLayout | header+buttons, frame, form grid, Form/Report cards |
| Events, sources, listeners | Action, Item, Adjustment, Window events |
| Mouse events | double-click list item; hover on buttons |
| Keyboard events | digits-only salary field; Enter moves focus |
| JDBC intro, drivers, architecture | `DBConnection` (Type-4 driver), `EmployeeDAO` |
| Servlet life cycle | `EmployeeServlet`: init -> service/doGet/doPost -> destroy |
| Types of servlet | `InterfaceServlet`, `HelloGenericServlet`, `EmployeeServlet` (HttpServlet) |
| JSP + life cycle vs servlet | `index.jsp` (table), `employees.jsp` |
| JSP architecture | Browser -> Servlet (controller) -> DAO/DB -> JSP (view) |

## JSP architecture (MVC)
Browser -> `EmployeeServlet` (controller) -> `EmployeeDAO` -> MySQL
-> `Employee` list set as request attribute -> `employees.jsp` (view) -> HTML to browser.
