package payroll.servlet;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * TYPE 1: implementing the javax.servlet.Servlet interface directly.
 * All five life-cycle methods must be written by hand.
 */
public class InterfaceServlet implements Servlet {
    private ServletConfig config;

    @Override public void init(ServletConfig config) throws ServletException {
        this.config = config;
        config.getServletContext().log("InterfaceServlet.init()");
    }

    @Override public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println("<html><body style='font-family:sans-serif'>");
        out.println("<h2>Servlet interface - Payroll System</h2>");
        out.println("<p>Servlet name: " + config.getServletName() + "</p>");
        out.println("<a href='index.jsp'>Home</a></body></html>");
    }

    @Override public void destroy() { config.getServletContext().log("InterfaceServlet.destroy()"); }
    @Override public ServletConfig getServletConfig() { return config; }
    @Override public String getServletInfo() { return "Servlet interface demo"; }
}
