<%@ page language="java" contentType="text/html; charset=UTF-8"
         import="java.util.List, payroll.model.Employee" %>
<%!
    // Declaration: helper method on the generated servlet class
    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<%
    @SuppressWarnings("unchecked")
    List<Employee> list = (List<Employee>) request.getAttribute("employees");
    if (list == null) {                       // opened directly -> go through the servlet
        response.sendRedirect(request.getContextPath() + "/employees");
        return;
    }
    double total = 0;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Employees - Payroll</title>
<style>
  body{font-family:sans-serif;margin:16px;color:#222}
  h1,h2{color:#1f4e79}
  .wrap{overflow-x:auto}
  table{border-collapse:collapse;width:100%;min-width:640px}
  td,th{border:1px solid #bbb;padding:6px;font-size:14px;text-align:left}
  th{background:#e8eef5}
  form.add{display:grid;grid-template-columns:1fr;gap:8px;max-width:420px}
  input,select,button{padding:8px;font-size:15px}
  button{background:#1f4e79;color:#fff;border:0;border-radius:6px}
  button.del{background:#b03030;padding:4px 8px;font-size:13px}
</style>
</head>
<body>
<h1>Employees &amp; Payroll</h1>
<p><a href="index.jsp">Home</a></p>

<div class="wrap">
<table>
  <tr><th>ID</th><th>Name</th><th>Dept</th><th>Basic</th><th>Gross</th><th>PF</th><th>Tax</th><th>Net Pay</th><th></th></tr>
<% for (Employee e : list) { total += e.getNetSalary(); %>
  <tr>
    <td><%= e.getId() %></td>
    <td><%= esc(e.getName()) %></td>
    <td><%= esc(e.getDepartment()) %></td>
    <td><%= String.format("%.2f", e.getBasicSalary()) %></td>
    <td><%= String.format("%.2f", e.getGross()) %></td>
    <td><%= String.format("%.2f", e.getPf()) %></td>
    <td><%= String.format("%.2f", e.getTax()) %></td>
    <td><b><%= String.format("%.2f", e.getNetSalary()) %></b></td>
    <td>
      <form method="post" action="employees" style="margin:0">
        <input type="hidden" name="action" value="delete">
        <input type="hidden" name="id" value="<%= e.getId() %>">
        <button class="del" type="submit" onclick="return confirm('Delete this employee?')">Delete</button>
      </form>
    </td>
  </tr>
<% } %>
  <tr><td colspan="7"><b>Total net payout</b></td><td colspan="2"><b><%= String.format("%.2f", total) %></b></td></tr>
</table>
</div>

<h2>Add employee</h2>
<form class="add" method="post" action="employees">
  <input name="name" placeholder="Name" required>
  <select name="department">
    <option>HR</option><option>Finance</option><option>IT</option><option>Sales</option><option>Operations</option>
  </select>
  <input name="designation" placeholder="Designation">
  <select name="gender"><option>Male</option><option>Female</option></select>
  <input name="basic" type="number" step="0.01" min="1" placeholder="Basic salary" required>
  <label><input type="checkbox" name="bonus"> Eligible for bonus</label>
  <input name="bonusPercent" type="number" min="0" max="30" value="10" placeholder="Bonus %">
  <button type="submit">Add</button>
</form>
</body>
</html>
