package payroll.servlet;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * TYPE 2: GenericServlet (protocol independent). Only service() is abstract,
 * so it is implemented directly; there is no doGet/doPost.
 */
public class HelloGenericServlet extends GenericServlet {
    private static final long serialVersionUID = 1L;
    private int requestCount;

    @Override
    public void init() {
        requestCount = 0;
        log("HelloGenericServlet.init()");
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        requestCount++;
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println("<html><body style='font-family:sans-serif'>");
        out.println("<h2>GenericServlet - Payroll System</h2>");
        out.println("<p>Time: " + new Date() + "</p>");
        out.println("<p>Requests served since init(): " + requestCount + "</p>");
        out.println("<a href='index.jsp'>Home</a>");
        out.println("</body></html>");
    }
}
