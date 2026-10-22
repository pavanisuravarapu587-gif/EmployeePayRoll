<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.Date" %>
<%!
    // DECLARATION: becomes a field/method of the generated servlet class
    private int hits = 0;
    public void jspInit()    { System.out.println("index.jsp -> jspInit() called once"); }
    public void jspDestroy() { System.out.println("index.jsp -> jspDestroy() called once"); }
%>
<% hits++; /* SCRIPTLET: goes inside _jspService() */ %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Employee Payroll System</title>
<style>
  body{font-family:sans-serif;max-width:760px;margin:24px auto;padding:0 14px;color:#222}
  h1{color:#1f4e79} a.btn{display:inline-block;background:#1f4e79;color:#fff;padding:8px 14px;
  border-radius:6px;text-decoration:none;margin:4px 6px 4px 0}
  table{border-collapse:collapse;width:100%} td,th{border:1px solid #bbb;padding:6px;text-align:left;font-size:14px}
  th{background:#e8eef5}
</style>
</head>
<body>
<h1>Employee Payroll System</h1>
<p>Server time (JSP expression): <b><%= new Date() %></b></p>
<p>Page hits since jspInit(): <b><%= hits %></b></p>

<a class="btn" href="employees">Employees (Servlet + JSP)</a>
<a class="btn" href="generic">GenericServlet</a>
<a class="btn" href="interface">Servlet interface</a>

<h2>Servlet vs JSP life cycle</h2>
<table>
<tr><th>Servlet</th><th>JSP</th></tr>
<tr><td>-</td><td>1. Translation (.jsp to .java servlet)</td></tr>
<tr><td>-</td><td>2. Compilation (.java to .class)</td></tr>
<tr><td>1. Load class</td><td>3. Load class</td></tr>
<tr><td>2. Instantiate</td><td>4. Instantiate</td></tr>
<tr><td>3. init()</td><td>5. jspInit()</td></tr>
<tr><td>4. service() / doGet() / doPost()</td><td>6. _jspService()</td></tr>
<tr><td>5. destroy()</td><td>7. jspDestroy()</td></tr>
</table>
</body>
</html>
