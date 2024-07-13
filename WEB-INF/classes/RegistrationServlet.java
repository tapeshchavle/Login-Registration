import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationServlet extends HttpServlet{
    private Connection conn;
    private PreparedStatement ps;
    public void init()throws ServletException{
        try{
            conn=DriverManager.getConnection("jdbc:oracle:thin:@//LAPTOP-S1QPSLM6:1521/xe","tapesh","abc");
            System.out.println("connected succesfully");
            ps=conn.prepareStatement("insert into users values(?,?,?)");
        }
        catch(SQLException sq){
            System.out.println("some problem in init"+sq);
            ServletException obj=new ServletException(sq.getMessage());
            throw obj;
        }
    }
    protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        resp.setContentType("text/html");
        PrintWriter pw=resp.getWriter();
        pw.println("<html><head><title>Registration Page</title></head>");
        pw.println("<body>");
        String username=req.getParameter("username");
        String userid=req.getParameter("userid");
        String pwd=req.getParameter("password");
        try{
            ps.setString(1,userid);
            ps.setString(2,pwd);
            ps.setString(3, username);
            int rs=ps.executeUpdate();
            if(rs!=0){
                pw.println("<h2> Hi "+username+" you are Sucessfully Register</h2>");
            }
        }
        catch(SQLException sq){
            System.out.println("Some Problem in dopost"+sq);
            pw.println("<h2>Sorry!</h2>");
            pw.println(("<p>Server is experiencing some issues.Try later</p>"));
        }
        pw.println("</body></html>");
        pw.close();
    
    }
    public void destroy(){
        try{
            conn.close();
        }
        catch(SQLException sq){
            System.out.println("some problem during closing the connection");
        }
    }
}