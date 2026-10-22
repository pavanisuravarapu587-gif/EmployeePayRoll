package payroll.servlet;

import payroll.dao.EmployeeDAO;
import payroll.model.Employee;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * TYPE 3: HttpServlet (HTTP specific) - the controller of the web side.
 * Life cycle: init() once -> doGet()/doPost() per request (via service()) -> destroy() once.
 */
public class EmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private EmployeeDAO dao;

    @Override
    public void init() throws ServletException {            // SLC phase 1: called once after loading
        dao = new EmployeeDAO();
        log("EmployeeServlet.init() - servlet initialised");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {            // SLC phase 2: handles GET requests
        try {
            List<Employee> list = dao.findAll();
            req.setAttribute("employees", list);               // share data with the JSP
            req.getRequestDispatcher("/employees.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {            // SLC phase 2: handles POST requests
        req.setCharacterEncoding("UTF-8");
        try {
            if ("delete".equals(req.getParameter("action"))) {
                dao.delete(Integer.parseInt(req.getParameter("id")));
            } else {
                Employee e = new Employee();
                e.setName(req.getParameter("name"));
                e.setDepartment(req.getParameter("department"));
                e.setDesignation(req.getParameter("designation"));
                e.setGender(req.getParameter("gender"));
                e.setBasicSalary(Double.parseDouble(req.getParameter("basic")));
                boolean bonus = "on".equals(req.getParameter("bonus"));
                e.setBonusEligible(bonus);
                e.setBonusPercent(bonus ? Integer.parseInt(req.getParameter("bonusPercent")) : 0);
                dao.add(e);
            }
            resp.sendRedirect(req.getContextPath() + "/employees");   // Post/Redirect/Get
        } catch (SQLException | NumberFormatException | NullPointerException e) {
            throw new ServletException("Could not process request: " + e.getMessage(), e);
        }
    }

    @Override
    public void destroy() {                                    // SLC phase 3: called once at unload
        log("EmployeeServlet.destroy() - servlet unloaded");
    }
}
